package com.sa.healthplan.service;

import com.sa.healthplan.dto.CustomerImportResult;
import com.sa.healthplan.dto.CustomerImportResult.RowError;
import com.sa.healthplan.dto.CustomerRequest;
import com.sa.healthplan.model.DocumentType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

/**
 * Carga masiva de clientes (solo titulares) desde un Excel (.xlsx). Cada fila es
 * un cliente; las que fallan no detienen al resto y se reportan con su número de
 * fila. Reutiliza CustomerService.createCustomer para la lógica de alta y el
 * Validator de Jakarta para validar los mismos campos que la API REST.
 */
@Service
public class CustomerImportService {

    /** Orden de columnas esperado en el Excel (fila 0 = encabezado). */
    private static final int COL_FIRST_NAME = 0;
    private static final int COL_LAST_NAME = 1;
    private static final int COL_DOCUMENT_TYPE = 2;
    private static final int COL_DOCUMENT_NUMBER = 3;
    private static final int COL_BIRTH_DATE = 4;
    private static final int COL_EMAIL = 5;
    private static final int COL_PHONE = 6;

    private final CustomerService customerService;
    private final Validator validator;
    private final DataFormatter dataFormatter = new DataFormatter();

    public CustomerImportService(CustomerService customerService, Validator validator) {
        this.customerService = customerService;
        this.validator = validator;
    }

    public CustomerImportResult importFromExcel(InputStream inputStream) {
        Workbook workbook;
        try {
            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException | RuntimeException e) {
            throw new IllegalArgumentException("El archivo no es un Excel (.xlsx) válido.");
        }

        List<RowError> errors = new ArrayList<>();
        int created = 0;
        int totalRows = 0;

        try (workbook) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null || isEmptyRow(row)) {
                    continue;
                }
                totalRows++;
                int excelRow = r + 1; // 1-based, como lo ve el usuario en Excel
                try {
                    CustomerRequest request = parseRow(row);
                    String validationError = validate(request);
                    if (validationError != null) {
                        errors.add(new RowError(excelRow, validationError));
                        continue;
                    }
                    customerService.createCustomer(request);
                    created++;
                } catch (Exception e) {
                    errors.add(new RowError(excelRow, e.getMessage()));
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("No se pudo leer el archivo Excel.");
        }

        return new CustomerImportResult(totalRows, created, errors);
    }

    /** Genera una plantilla .xlsx con los encabezados y una fila de ejemplo. */
    public byte[] generateTemplate() {
        String[] headers = {
                "Nombre", "Apellido", "TipoDocumento (DNI/PASSPORT/OTHER)", "NumeroDocumento",
                "FechaNacimiento (AAAA-MM-DD)", "Email (opcional)", "Telefono (opcional)"
        };
        String[] example = { "Juan", "Pérez", "DNI", "30111222", "1985-04-12", "juan@example.com", "1144556677" };

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Clientes");
            Row headerRow = sheet.createRow(0);
            Row exampleRow = sheet.createRow(1);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
                exampleRow.createCell(i).setCellValue(example[i]);
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo generar la plantilla", e);
        }
    }

    private CustomerRequest parseRow(Row row) {
        return new CustomerRequest(
                getString(row, COL_FIRST_NAME),
                getString(row, COL_LAST_NAME),
                parseDocumentType(getString(row, COL_DOCUMENT_TYPE)),
                getString(row, COL_DOCUMENT_NUMBER),
                parseDate(row.getCell(COL_BIRTH_DATE)),
                getString(row, COL_EMAIL),
                getString(row, COL_PHONE),
                List.of());
    }

    private String validate(CustomerRequest request) {
        Set<ConstraintViolation<CustomerRequest>> violations = validator.validate(request);
        if (violations.isEmpty()) {
            return null;
        }
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
    }

    private DocumentType parseDocumentType(String value) {
        if (value == null || value.isBlank()) {
            return null; // lo marca @NotNull en la validación
        }
        try {
            return DocumentType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Tipo de documento inválido: '" + value + "'. Use DNI, PASSPORT u OTHER.");
        }
    }

    private LocalDate parseDate(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }
        String text = dataFormatter.formatCellValue(cell).trim();
        if (text.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(text, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e2) {
                throw new IllegalArgumentException(
                        "Fecha de nacimiento inválida: '" + text + "'. Use el formato AAAA-MM-DD.");
            }
        }
    }

    private String getString(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) {
            return null;
        }
        String value = switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                double d = cell.getNumericCellValue();
                yield d == Math.floor(d) && !Double.isInfinite(d)
                        ? String.valueOf((long) d)
                        : String.valueOf(d);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
        return value.isBlank() ? null : value;
    }

    private boolean isEmptyRow(Row row) {
        for (int c = COL_FIRST_NAME; c <= COL_PHONE; c++) {
            if (getString(row, c) != null) {
                return false;
            }
        }
        return true;
    }
}

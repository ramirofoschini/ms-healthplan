package com.sa.healthplan.controller;

import com.sa.healthplan.dto.CustomerDTO;
import com.sa.healthplan.dto.CustomerImportResult;
import com.sa.healthplan.dto.CustomerRequest;
import com.sa.healthplan.dto.DependentRequest;
import com.sa.healthplan.service.CustomerImportService;
import com.sa.healthplan.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerImportService customerImportService;

    public CustomerController(CustomerService customerService,
                             CustomerImportService customerImportService) {
        this.customerService = customerService;
        this.customerImportService = customerImportService;
    }

    @Operation(summary = "Da de alta un cliente con su grupo familiar")
    @PostMapping
    public ResponseEntity<CustomerDTO> create(@Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(request));
    }

    @Operation(summary = "Devuelve un cliente por ID")
    @GetMapping("/{id}")
    public CustomerDTO getOne(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }

    @Operation(summary = "Lista los clientes")
    @GetMapping
    public List<CustomerDTO> list() {
        return customerService.listCustomers();
    }

    @Operation(summary = "Actualiza un cliente y reemplaza su grupo familiar")
    @PutMapping("/{id}")
    public CustomerDTO update(@PathVariable Long id, @Valid @RequestBody CustomerRequest request) {
        return customerService.updateCustomer(id, request);
    }

    @Operation(summary = "Agrega un integrante al grupo familiar de un cliente")
    @PostMapping("/{id}/dependents")
    public ResponseEntity<CustomerDTO> addDependent(@PathVariable Long id,
                                                    @Valid @RequestBody DependentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.addDependent(id, request));
    }

    @Operation(summary = "Carga masiva de clientes (solo titulares) desde un Excel (.xlsx)")
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CustomerImportResult importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío.");
        }
        return customerImportService.importFromExcel(file.getInputStream());
    }

    @Operation(summary = "Descarga la plantilla Excel para la carga masiva de clientes")
    @GetMapping("/import/template")
    public ResponseEntity<byte[]> importTemplate() {
        byte[] body = customerImportService.generateTemplate();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=plantilla-clientes.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(body);
    }
}

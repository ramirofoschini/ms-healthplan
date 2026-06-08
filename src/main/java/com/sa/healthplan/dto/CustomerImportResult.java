package com.sa.healthplan.dto;

import java.util.List;

/**
 * Resultado de una carga masiva de clientes: cuántas filas se procesaron,
 * cuántas se crearon y el detalle de las que fallaron (con el número de fila
 * del Excel para que el usuario las ubique).
 */
public record CustomerImportResult(int totalRows, int created, List<RowError> errors) {

    public record RowError(int row, String message) {
    }
}

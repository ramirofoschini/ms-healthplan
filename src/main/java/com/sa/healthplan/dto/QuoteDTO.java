package com.sa.healthplan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Resultado de una cotización: el desglose por integrante y el total.
 */
public record QuoteDTO(
        Long planId,
        String planName,
        Long customerId,
        String customerName,
        LocalDate date,
        List<QuoteLineDTO> lines,
        BigDecimal total
) {
}

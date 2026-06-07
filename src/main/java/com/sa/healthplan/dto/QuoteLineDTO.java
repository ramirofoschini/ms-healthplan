package com.sa.healthplan.dto;

import java.math.BigDecimal;

/**
 * Una línea de la cotización: el precio de un integrante del grupo familiar
 * según la franja etaria en la que cae a la fecha tasada.
 */
public record QuoteLineDTO(
        String memberType,
        String fullName,
        int age,
        String ageBandName,
        BigDecimal amount
) {
}

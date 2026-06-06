package com.sa.healthplan.dto;

import com.sa.healthplan.model.PlanPrice;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Representación de salida de un precio. Aplana las relaciones (incluye nombre
 * de plan y franja) para que el cliente no tenga que resolver más llamadas.
 */
public record PlanPriceDTO(
        Long id,
        Long planId,
        String planName,
        Long ageBandId,
        String ageBandName,
        BigDecimal amount,
        LocalDate validFrom,
        LocalDate validTo
) {

    public static PlanPriceDTO from(PlanPrice p) {
        return new PlanPriceDTO(
                p.getId(),
                p.getPlan().getId(),
                p.getPlan().getName(),
                p.getAgeBand().getId(),
                p.getAgeBand().getName(),
                p.getAmount(),
                p.getValidFrom(),
                p.getValidTo());
    }
}

package com.sa.healthplan.dto;

import com.sa.healthplan.model.PrecioPlan;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Representación de salida de un precio. Aplana las relaciones (incluye nombre
 * de plan y franja) para que el cliente no tenga que resolver más llamadas.
 */
public record PrecioPlanDTO(
        Long id,
        Long planId,
        String planNombre,
        Long franjaId,
        String franjaNombre,
        BigDecimal monto,
        LocalDate vigenciaDesde,
        LocalDate vigenciaHasta
) {

    public static PrecioPlanDTO from(PrecioPlan p) {
        return new PrecioPlanDTO(
                p.getId(),
                p.getPlan().getId(),
                p.getPlan().getNombre(),
                p.getFranja().getId(),
                p.getFranja().getNombre(),
                p.getMonto(),
                p.getVigenciaDesde(),
                p.getVigenciaHasta());
    }
}

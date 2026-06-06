package com.sa.healthplan.dto;

import com.sa.healthplan.model.FranjaEtaria;

/**
 * Representación de salida de una franja etaria para la API.
 */
public record FranjaEtariaDTO(Long id, String nombre, int edadDesde, int edadHasta) {

    public static FranjaEtariaDTO from(FranjaEtaria f) {
        return new FranjaEtariaDTO(f.getId(), f.getNombre(), f.getEdadDesde(), f.getEdadHasta());
    }
}

package com.sa.healthplan.dto;

import com.sa.healthplan.model.AgeBand;

/**
 * Representación de salida de una franja etaria para la API.
 */
public record AgeBandDTO(Long id, String name, int ageFrom, int ageTo) {

    public static AgeBandDTO from(AgeBand ageBand) {
        return new AgeBandDTO(ageBand.getId(), ageBand.getName(), ageBand.getAgeFrom(), ageBand.getAgeTo());
    }
}

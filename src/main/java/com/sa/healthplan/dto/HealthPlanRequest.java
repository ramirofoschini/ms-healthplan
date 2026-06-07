package com.sa.healthplan.dto;

import com.sa.healthplan.model.CoverageLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Datos de entrada para crear o modificar un plan. No incluye id (lo genera la
 * base) ni enlaces HATEOAS: es el contrato limpio de la API.
 */
public record HealthPlanRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @NotNull(message = "El nivel de cobertura es obligatorio")
        CoverageLevel coverageLevel,

        boolean active,

        String documentPath,

        String clinics,

        String comments
) {
}

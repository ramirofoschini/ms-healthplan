package com.sa.healthplan.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Datos de entrada para cargar un precio de un plan. Las anotaciones de
 * validación las verifica Spring con @Valid; si fallan, el GlobalExceptionHandler
 * responde 400 con el detalle por campo. No incluye planId: viene en la URL.
 */
public record PlanPriceRequest(

        @NotNull(message = "La franja etaria es obligatoria")
        Long ageBandId,

        @NotNull(message = "El monto es obligatorio")
        @Positive(message = "El monto debe ser mayor a cero")
        BigDecimal amount,

        @NotNull(message = "La vigencia desde es obligatoria")
        LocalDate validFrom,

        LocalDate validTo
) {
}

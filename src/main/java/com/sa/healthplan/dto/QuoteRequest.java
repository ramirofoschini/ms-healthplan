package com.sa.healthplan.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Datos de entrada para simular una cotización. La fecha es opcional: si no se
 * envía, se tasa a la fecha de hoy (afecta la edad y la vigencia del precio).
 */
public record QuoteRequest(

        @NotNull(message = "El plan es obligatorio")
        Long planId,

        @NotNull(message = "El cliente es obligatorio")
        Long customerId,

        LocalDate date
) {
}

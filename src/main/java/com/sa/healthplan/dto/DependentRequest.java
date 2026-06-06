package com.sa.healthplan.dto;

import com.sa.healthplan.model.Relationship;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

/**
 * Datos de entrada de un integrante del grupo familiar.
 */
public record DependentRequest(

        @NotNull(message = "El parentesco es obligatorio")
        Relationship relationship,

        @NotBlank(message = "El nombre es obligatorio")
        String firstName,

        @NotBlank(message = "El apellido es obligatorio")
        String lastName,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
        LocalDate birthDate
) {
}

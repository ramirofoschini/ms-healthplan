package com.sa.healthplan.dto;

import com.sa.healthplan.model.DocumentType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import java.util.List;

/**
 * Datos de entrada para dar de alta un cliente junto con su grupo familiar.
 * {@code @Valid} en la lista propaga la validación a cada integrante.
 */
public record CustomerRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String firstName,

        @NotBlank(message = "El apellido es obligatorio")
        String lastName,

        @NotNull(message = "El tipo de documento es obligatorio")
        DocumentType documentType,

        @NotBlank(message = "El número de documento es obligatorio")
        String documentNumber,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
        LocalDate birthDate,

        @Email(message = "El email no es válido")
        String email,

        String phone,

        @Valid
        List<DependentRequest> dependents
) {
}

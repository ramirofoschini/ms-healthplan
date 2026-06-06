package com.sa.healthplan.dto;

import com.sa.healthplan.model.Dependent;
import com.sa.healthplan.model.Relationship;
import java.time.LocalDate;

/**
 * Representación de salida de un integrante del grupo familiar.
 */
public record DependentDTO(
        Long id,
        Relationship relationship,
        String firstName,
        String lastName,
        LocalDate birthDate
) {

    public static DependentDTO from(Dependent d) {
        return new DependentDTO(d.getId(), d.getRelationship(), d.getFirstName(), d.getLastName(), d.getBirthDate());
    }
}

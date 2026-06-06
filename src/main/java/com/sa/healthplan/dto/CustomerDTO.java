package com.sa.healthplan.dto;

import com.sa.healthplan.model.Customer;
import com.sa.healthplan.model.DocumentType;
import java.time.LocalDate;
import java.util.List;

/**
 * Representación de salida de un cliente con su grupo familiar.
 */
public record CustomerDTO(
        Long id,
        String firstName,
        String lastName,
        DocumentType documentType,
        String documentNumber,
        LocalDate birthDate,
        String email,
        String phone,
        List<DependentDTO> dependents
) {

    public static CustomerDTO from(Customer c) {
        List<DependentDTO> dependents = c.getDependents().stream()
                .map(DependentDTO::from)
                .toList();
        return new CustomerDTO(
                c.getId(),
                c.getFirstName(),
                c.getLastName(),
                c.getDocumentType(),
                c.getDocumentNumber(),
                c.getBirthDate(),
                c.getEmail(),
                c.getPhone(),
                dependents);
    }
}

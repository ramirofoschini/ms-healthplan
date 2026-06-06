package com.sa.healthplan.repository;

import com.sa.healthplan.model.Customer;
import com.sa.healthplan.model.DocumentType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByDocumentTypeAndDocumentNumber(DocumentType documentType, String documentNumber);

    boolean existsByDocumentTypeAndDocumentNumber(DocumentType documentType, String documentNumber);
}

package com.sa.healthplan.service;

import com.sa.healthplan.dto.CustomerDTO;
import com.sa.healthplan.dto.CustomerRequest;
import com.sa.healthplan.dto.DependentRequest;
import com.sa.healthplan.exception.DuplicateResourceException;
import com.sa.healthplan.exception.ResourceNotFoundException;
import com.sa.healthplan.model.Customer;
import com.sa.healthplan.model.Dependent;
import com.sa.healthplan.repository.CustomerRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Gestión de clientes (titulares) y su grupo familiar. El cliente es la raíz del
 * agregado: los dependientes se guardan en cascada al persistir el cliente.
 */
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerDTO createCustomer(CustomerRequest request) {
        if (customerRepository.existsByDocumentTypeAndDocumentNumber(request.documentType(), request.documentNumber())) {
            throw new DuplicateResourceException(
                    "Ya existe un cliente con documento " + request.documentType() + " " + request.documentNumber());
        }

        Customer customer = new Customer();
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setDocumentType(request.documentType());
        customer.setDocumentNumber(request.documentNumber());
        customer.setBirthDate(request.birthDate());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());

        if (request.dependents() != null) {
            request.dependents().forEach(d -> customer.addDependent(toDependent(d)));
        }

        return CustomerDTO.from(customerRepository.save(customer));
    }

    @Transactional(readOnly = true)
    public CustomerDTO getCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("el cliente", id));
        return CustomerDTO.from(customer);
    }

    @Transactional(readOnly = true)
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll().stream()
                .map(CustomerDTO::from)
                .toList();
    }

    @Transactional
    public CustomerDTO addDependent(Long customerId, DependentRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("el cliente", customerId));
        customer.addDependent(toDependent(request));
        return CustomerDTO.from(customerRepository.save(customer));
    }

    private Dependent toDependent(DependentRequest request) {
        Dependent dependent = new Dependent();
        dependent.setRelationship(request.relationship());
        dependent.setFirstName(request.firstName());
        dependent.setLastName(request.lastName());
        dependent.setBirthDate(request.birthDate());
        return dependent;
    }
}

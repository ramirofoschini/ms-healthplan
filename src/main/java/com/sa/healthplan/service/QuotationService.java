package com.sa.healthplan.service;

import com.sa.healthplan.dto.QuoteDTO;
import com.sa.healthplan.dto.QuoteLineDTO;
import com.sa.healthplan.dto.QuoteRequest;
import com.sa.healthplan.exception.QuotationException;
import com.sa.healthplan.exception.ResourceNotFoundException;
import com.sa.healthplan.model.AgeBand;
import com.sa.healthplan.model.Customer;
import com.sa.healthplan.model.Dependent;
import com.sa.healthplan.model.HealthPlan;
import com.sa.healthplan.model.PlanPrice;
import com.sa.healthplan.repository.AgeBandRepository;
import com.sa.healthplan.repository.CustomerRepository;
import com.sa.healthplan.repository.HealthPlanRepository;
import com.sa.healthplan.repository.PlanPriceRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Motor de tasación: dado un plan, un cliente (con su grupo familiar) y una
 * fecha, calcula el precio de cada integrante según su franja etaria y la lista
 * de precios vigente, y devuelve la cotización con el total. No persiste nada
 * (eso es la Etapa 4); es una simulación.
 */
@Service
public class QuotationService {

    private final HealthPlanRepository planRepository;
    private final CustomerRepository customerRepository;
    private final AgeBandRepository ageBandRepository;
    private final PlanPriceRepository planPriceRepository;

    public QuotationService(HealthPlanRepository planRepository,
                            CustomerRepository customerRepository,
                            AgeBandRepository ageBandRepository,
                            PlanPriceRepository planPriceRepository) {
        this.planRepository = planRepository;
        this.customerRepository = customerRepository;
        this.ageBandRepository = ageBandRepository;
        this.planPriceRepository = planPriceRepository;
    }

    @Transactional(readOnly = true)
    public QuoteDTO simulate(QuoteRequest request) {
        LocalDate date = request.date() != null ? request.date() : LocalDate.now();

        HealthPlan plan = planRepository.findById(request.planId())
                .orElseThrow(() -> new ResourceNotFoundException("el plan", request.planId()));
        if (!plan.isActive()) {
            throw new QuotationException("El plan '" + plan.getName() + "' no está activo");
        }

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("el cliente", request.customerId()));

        List<AgeBand> bands = ageBandRepository.findAll();
        List<QuoteLineDTO> lines = new ArrayList<>();

        // Titular
        lines.add(buildLine("TITULAR", fullName(customer.getFirstName(), customer.getLastName()),
                customer.ageOn(date), plan.getId(), bands, date));

        // Grupo familiar
        for (Dependent dependent : customer.getDependents()) {
            lines.add(buildLine(dependent.getRelationship().name(),
                    fullName(dependent.getFirstName(), dependent.getLastName()),
                    dependent.ageOn(date), plan.getId(), bands, date));
        }

        BigDecimal total = lines.stream()
                .map(QuoteLineDTO::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new QuoteDTO(plan.getId(), plan.getName(), customer.getId(),
                fullName(customer.getFirstName(), customer.getLastName()), date, lines, total);
    }

    private QuoteLineDTO buildLine(String memberType, String fullName, int age, Long planId,
                                   List<AgeBand> bands, LocalDate date) {
        AgeBand band = bands.stream()
                .filter(b -> b.containsAge(age))
                .findFirst()
                .orElseThrow(() -> new QuotationException("No hay franja etaria para la edad " + age));

        PlanPrice price = planPriceRepository.findValid(planId, band.getId(), date).stream()
                .findFirst()
                .orElseThrow(() -> new QuotationException(
                        "No hay precio vigente para la franja " + band.getName() + " a la fecha " + date));

        return new QuoteLineDTO(memberType, fullName, age, band.getName(), price.getAmount());
    }

    private String fullName(String firstName, String lastName) {
        return firstName + " " + lastName;
    }
}

package com.sa.healthplan.service;

import com.sa.healthplan.dto.AgeBandDTO;
import com.sa.healthplan.dto.PlanPriceDTO;
import com.sa.healthplan.dto.PlanPriceRequest;
import com.sa.healthplan.exception.ResourceNotFoundException;
import com.sa.healthplan.model.AgeBand;
import com.sa.healthplan.model.HealthPlan;
import com.sa.healthplan.model.PlanPrice;
import com.sa.healthplan.repository.AgeBandRepository;
import com.sa.healthplan.repository.HealthPlanRepository;
import com.sa.healthplan.repository.PlanPriceRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Gestión del tarifario: franjas etarias y precios de los planes.
 * El mapeo entidad → DTO ocurre dentro de métodos transaccionales para poder
 * resolver las relaciones LAZY de PlanPrice.
 */
@Service
public class PricingService {

    private final HealthPlanRepository planRepository;
    private final AgeBandRepository ageBandRepository;
    private final PlanPriceRepository planPriceRepository;

    public PricingService(HealthPlanRepository planRepository,
                          AgeBandRepository ageBandRepository,
                          PlanPriceRepository planPriceRepository) {
        this.planRepository = planRepository;
        this.ageBandRepository = ageBandRepository;
        this.planPriceRepository = planPriceRepository;
    }

    @Transactional(readOnly = true)
    public List<AgeBandDTO> listAgeBands() {
        return ageBandRepository.findAll().stream()
                .map(AgeBandDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PlanPriceDTO> listPlanPrices(Long planId) {
        if (!planRepository.existsById(planId)) {
            throw new ResourceNotFoundException("el plan", planId);
        }
        return planPriceRepository.findByPlanId(planId).stream()
                .map(PlanPriceDTO::from)
                .toList();
    }

    @Transactional
    public PlanPriceDTO createPlanPrice(Long planId, PlanPriceRequest request) {
        HealthPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("el plan", planId));
        AgeBand ageBand = ageBandRepository.findById(request.ageBandId())
                .orElseThrow(() -> new ResourceNotFoundException("la franja etaria", request.ageBandId()));

        if (request.validTo() != null && request.validTo().isBefore(request.validFrom())) {
            throw new IllegalArgumentException("validTo no puede ser anterior a validFrom");
        }

        PlanPrice price = new PlanPrice();
        price.setPlan(plan);
        price.setAgeBand(ageBand);
        price.setAmount(request.amount());
        price.setValidFrom(request.validFrom());
        price.setValidTo(request.validTo());

        return PlanPriceDTO.from(planPriceRepository.save(price));
    }
}

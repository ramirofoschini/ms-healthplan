package com.sa.healthplan.controller;

import com.sa.healthplan.dto.PlanPriceDTO;
import com.sa.healthplan.dto.PlanPriceRequest;
import com.sa.healthplan.service.PricingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/plans/{planId}/prices")
public class PlanPriceController {

    private final PricingService pricingService;

    public PlanPriceController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @Operation(summary = "Lista los precios cargados de un plan")
    @GetMapping
    public List<PlanPriceDTO> list(@PathVariable Long planId) {
        return pricingService.listPlanPrices(planId);
    }

    @Operation(summary = "Carga un precio para un plan (solo ADMIN o SUPERVISOR)")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @PostMapping
    public ResponseEntity<PlanPriceDTO> create(@PathVariable Long planId,
                                               @Valid @RequestBody PlanPriceRequest request) {
        PlanPriceDTO created = pricingService.createPlanPrice(planId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}

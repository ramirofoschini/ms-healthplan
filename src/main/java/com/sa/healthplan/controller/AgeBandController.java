package com.sa.healthplan.controller;

import com.sa.healthplan.dto.AgeBandDTO;
import com.sa.healthplan.service.PricingService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/age-bands")
public class AgeBandController {

    private final PricingService pricingService;

    public AgeBandController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @Operation(summary = "Lista las franjas etarias disponibles para tarifar")
    @GetMapping
    public List<AgeBandDTO> list() {
        return pricingService.listAgeBands();
    }
}

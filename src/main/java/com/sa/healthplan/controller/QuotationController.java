package com.sa.healthplan.controller;

import com.sa.healthplan.dto.QuoteDTO;
import com.sa.healthplan.dto.QuoteRequest;
import com.sa.healthplan.service.QuotationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/quotes")
public class QuotationController {

    private final QuotationService quotationService;

    public QuotationController(QuotationService quotationService) {
        this.quotationService = quotationService;
    }

    @Operation(summary = "Simula una cotización para un plan y un cliente (no la persiste)")
    @PostMapping("/simulate")
    public QuoteDTO simulate(@Valid @RequestBody QuoteRequest request) {
        return quotationService.simulate(request);
    }
}

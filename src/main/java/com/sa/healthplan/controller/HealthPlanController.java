package com.sa.healthplan.controller;

import com.sa.healthplan.dto.HealthPlanRequest;
import com.sa.healthplan.dto.HealthPlanResponse;
import com.sa.healthplan.service.HealthPlanService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health-plans")
public class HealthPlanController {

    private final HealthPlanService healthPlanService;

    public HealthPlanController(HealthPlanService healthPlanService) {
        this.healthPlanService = healthPlanService;
    }

    @Operation(summary = "Lista todos los planes")
    @GetMapping
    public List<HealthPlanResponse> list() {
        return healthPlanService.findAll();
    }

    @Operation(summary = "Devuelve un plan por ID")
    @GetMapping("/{id}")
    public HealthPlanResponse getOne(@PathVariable Long id) {
        return healthPlanService.findById(id);
    }

    @Operation(summary = "Busca planes por texto (cartilla o comentarios), paginado")
    @GetMapping("/search")
    public Page<HealthPlanResponse> search(@RequestParam String filter, Pageable pageable) {
        return healthPlanService.search(filter, pageable);
    }

    @Operation(summary = "Crea un plan (solo ADMIN o SUPERVISOR)")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @PostMapping
    public ResponseEntity<HealthPlanResponse> create(@Valid @RequestBody HealthPlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(healthPlanService.create(request));
    }

    @Operation(summary = "Modifica un plan (solo ADMIN o SUPERVISOR)")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @PutMapping("/{id}")
    public HealthPlanResponse update(@PathVariable Long id, @Valid @RequestBody HealthPlanRequest request) {
        return healthPlanService.update(id, request);
    }

    @Operation(summary = "Elimina un plan (solo ADMIN o SUPERVISOR)")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        healthPlanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

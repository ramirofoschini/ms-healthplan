package com.sa.healthplan.controller;

import com.sa.healthplan.dto.PrecioPlanDTO;
import com.sa.healthplan.dto.PrecioPlanRequest;
import com.sa.healthplan.service.TarifarioService;
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
@RequestMapping("/api/v1/planes/{planId}/precios")
public class PrecioController {

    private final TarifarioService tarifarioService;

    public PrecioController(TarifarioService tarifarioService) {
        this.tarifarioService = tarifarioService;
    }

    @Operation(summary = "Lista los precios cargados de un plan")
    @GetMapping
    public List<PrecioPlanDTO> listar(@PathVariable Long planId) {
        return tarifarioService.listarPrecios(planId);
    }

    @Operation(summary = "Carga un precio para un plan (solo ADMIN o SUPERVISOR)")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @PostMapping
    public ResponseEntity<PrecioPlanDTO> crear(@PathVariable Long planId,
                                               @Valid @RequestBody PrecioPlanRequest request) {
        PrecioPlanDTO creado = tarifarioService.crearPrecio(planId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
}

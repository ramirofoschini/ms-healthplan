package com.sa.healthplan.controller;

import com.sa.healthplan.dto.FranjaEtariaDTO;
import com.sa.healthplan.service.TarifarioService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/franjas")
public class FranjaEtariaController {

    private final TarifarioService tarifarioService;

    public FranjaEtariaController(TarifarioService tarifarioService) {
        this.tarifarioService = tarifarioService;
    }

    @Operation(summary = "Lista las franjas etarias disponibles para tarifar")
    @GetMapping
    public List<FranjaEtariaDTO> listar() {
        return tarifarioService.listarFranjas();
    }
}

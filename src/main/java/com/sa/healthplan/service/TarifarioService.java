package com.sa.healthplan.service;

import com.sa.healthplan.dto.FranjaEtariaDTO;
import com.sa.healthplan.dto.PrecioPlanDTO;
import com.sa.healthplan.dto.PrecioPlanRequest;
import com.sa.healthplan.exception.ResourceNotFoundException;
import com.sa.healthplan.model.FranjaEtaria;
import com.sa.healthplan.model.HealthPlan;
import com.sa.healthplan.model.PrecioPlan;
import com.sa.healthplan.repository.FranjaEtariaRepository;
import com.sa.healthplan.repository.HealthPlanRepositpory;
import com.sa.healthplan.repository.PrecioPlanRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Gestión del tarifario: franjas etarias y precios de los planes.
 * El mapeo entidad → DTO ocurre dentro de métodos transaccionales para poder
 * resolver las relaciones LAZY de PrecioPlan.
 */
@Service
public class TarifarioService {

    private final HealthPlanRepositpory planRepository;
    private final FranjaEtariaRepository franjaRepository;
    private final PrecioPlanRepository precioRepository;

    public TarifarioService(HealthPlanRepositpory planRepository,
                            FranjaEtariaRepository franjaRepository,
                            PrecioPlanRepository precioRepository) {
        this.planRepository = planRepository;
        this.franjaRepository = franjaRepository;
        this.precioRepository = precioRepository;
    }

    @Transactional(readOnly = true)
    public List<FranjaEtariaDTO> listarFranjas() {
        return franjaRepository.findAll().stream()
                .map(FranjaEtariaDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PrecioPlanDTO> listarPrecios(Long planId) {
        if (!planRepository.existsById(planId)) {
            throw new ResourceNotFoundException("el plan", planId);
        }
        return precioRepository.findByPlanId(planId).stream()
                .map(PrecioPlanDTO::from)
                .toList();
    }

    @Transactional
    public PrecioPlanDTO crearPrecio(Long planId, PrecioPlanRequest request) {
        HealthPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("el plan", planId));
        FranjaEtaria franja = franjaRepository.findById(request.franjaId())
                .orElseThrow(() -> new ResourceNotFoundException("la franja etaria", request.franjaId()));

        if (request.vigenciaHasta() != null && request.vigenciaHasta().isBefore(request.vigenciaDesde())) {
            throw new IllegalArgumentException("vigenciaHasta no puede ser anterior a vigenciaDesde");
        }

        PrecioPlan precio = new PrecioPlan();
        precio.setPlan(plan);
        precio.setFranja(franja);
        precio.setMonto(request.monto());
        precio.setVigenciaDesde(request.vigenciaDesde());
        precio.setVigenciaHasta(request.vigenciaHasta());

        return PrecioPlanDTO.from(precioRepository.save(precio));
    }
}

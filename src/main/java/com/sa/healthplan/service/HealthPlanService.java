package com.sa.healthplan.service;

import com.sa.healthplan.dto.HealthPlanRequest;
import com.sa.healthplan.dto.HealthPlanResponse;
import com.sa.healthplan.exception.ResourceNotFoundException;
import com.sa.healthplan.model.HealthPlan;
import com.sa.healthplan.repository.HealthPlanRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Gestión de planes (productos). Trabaja con DTOs en la frontera de la API y
 * mapea desde/hacia la entidad HealthPlan.
 */
@Service
public class HealthPlanService {

    private final HealthPlanRepository repository;

    public HealthPlanService(HealthPlanRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public HealthPlanResponse create(HealthPlanRequest request) {
        HealthPlan plan = new HealthPlan();
        apply(plan, request);
        return HealthPlanResponse.from(repository.save(plan));
    }

    @Transactional(readOnly = true)
    public List<HealthPlanResponse> findAll() {
        return repository.findAll().stream()
                .map(HealthPlanResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public HealthPlanResponse findById(Long id) {
        return HealthPlanResponse.from(getOrThrow(id));
    }

    @Transactional
    public HealthPlanResponse update(Long id, HealthPlanRequest request) {
        HealthPlan plan = getOrThrow(id);
        apply(plan, request);
        return HealthPlanResponse.from(repository.save(plan));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("el plan", id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<HealthPlanResponse> search(String filter, Pageable pageable) {
        return repository.search(filter, pageable).map(HealthPlanResponse::from);
    }

    private HealthPlan getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("el plan", id));
    }

    private void apply(HealthPlan plan, HealthPlanRequest request) {
        plan.setName(request.name());
        plan.setCoverageLevel(request.coverageLevel());
        plan.setActive(request.active());
        plan.setDocumentPath(request.documentPath());
        plan.setClinics(request.clinics());
        plan.setComments(request.comments());
    }
}

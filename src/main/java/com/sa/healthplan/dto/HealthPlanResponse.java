package com.sa.healthplan.dto;

import com.sa.healthplan.model.CoverageLevel;
import com.sa.healthplan.model.HealthPlan;

/**
 * Representación de salida de un plan (sin enlaces HATEOAS).
 */
public record HealthPlanResponse(
        Long id,
        String name,
        CoverageLevel coverageLevel,
        boolean active,
        String documentPath,
        String clinics,
        String comments
) {

    public static HealthPlanResponse from(HealthPlan p) {
        return new HealthPlanResponse(
                p.getId(),
                p.getName(),
                p.getCoverageLevel(),
                p.isActive(),
                p.getDocumentPath(),
                p.getClinics(),
                p.getComments());
    }
}

package com.sa.healthplan.model;

/**
 * Nivel de cobertura de un plan. Se persiste como texto
 * (@Enumerated(EnumType.STRING) en HealthPlan) para que la base sea legible.
 */
public enum CoverageLevel {
    BASIC,
    INTERMEDIATE,
    PREMIUM
}

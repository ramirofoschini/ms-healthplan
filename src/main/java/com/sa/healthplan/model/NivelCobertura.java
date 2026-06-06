package com.sa.healthplan.model;

/**
 * Nivel de cobertura de un plan. Se persiste como texto
 * (@Enumerated(EnumType.STRING) en HealthPlan) para que la base sea legible y
 * agregar valores nuevos no rompa los existentes.
 */
public enum NivelCobertura {
    BASICO,
    INTERMEDIO,
    PREMIUM
}

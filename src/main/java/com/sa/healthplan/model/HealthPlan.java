package com.sa.healthplan.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Plan de obra social: el producto que se tasa y se vende.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "health_plan")
public class HealthPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre comercial del plan (ej. "Plan 210"). */
    private String name;

    /** Nivel de cobertura, usado para diferenciar productos. */
    @Enumerated(EnumType.STRING)
    @Column(name = "coverage_level", length = 50)
    private CoverageLevel coverageLevel;

    /** Si está activo se puede tasar/ofrecer; si no, queda fuera de venta. */
    private boolean active;

    // Datos descriptivos del plan
    private String documentPath;
    private String clinics;
    private String comments;
}

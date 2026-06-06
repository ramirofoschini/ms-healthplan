package com.sa.healthplan.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "health_plan")
public class HealthPlan extends Base {

    /** Nombre comercial del plan (ej. "Plan 210"). */
    private String name;

    /** Nivel de cobertura, usado para diferenciar productos. */
    @Enumerated(EnumType.STRING)
    @Column(name = "coverage_level", length = 50)
    private CoverageLevel coverageLevel;

    /** Si está activo se puede tasar/ofrecer; si no, queda fuera de venta. */
    private boolean active;

    // Campos heredados del modelo original
    private String documentPath;
    private String clinics;
    private String comments;

}

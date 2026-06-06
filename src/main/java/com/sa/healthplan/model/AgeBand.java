package com.sa.healthplan.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Rango de edad usado para tarifar (ej. "0-18"). Es un catálogo compartido por
 * todos los planes; el precio concreto vive en {@link PlanPrice}.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "age_band")
public class AgeBand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "age_from", nullable = false)
    private int ageFrom;

    @Column(name = "age_to", nullable = false)
    private int ageTo;

    /** Devuelve true si la edad cae dentro de esta franja (límites incluidos). */
    public boolean containsAge(int age) {
        return age >= ageFrom && age <= ageTo;
    }
}

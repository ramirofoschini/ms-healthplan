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
 * todos los planes; el precio concreto vive en {@link PrecioPlan}.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "franja_etaria")
public class FranjaEtaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "edad_desde", nullable = false)
    private int edadDesde;

    @Column(name = "edad_hasta", nullable = false)
    private int edadHasta;

    /** Devuelve true si la edad cae dentro de esta franja (límites incluidos). */
    public boolean contieneEdad(int edad) {
        return edad >= edadDesde && edad <= edadHasta;
    }
}

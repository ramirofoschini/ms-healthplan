package com.sa.healthplan.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Precio de un plan para una franja etaria, con vigencia. Permite historial de
 * listas: al subir precios nuevos se cierra el período anterior (vigenciaHasta)
 * en vez de borrarlo. El motor de tasación busca el precio vigente a una fecha.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "precio_plan")
public class PrecioPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    private HealthPlan plan;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "franja_id", nullable = false)
    private FranjaEtaria franja;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(name = "vigencia_desde", nullable = false)
    private LocalDate vigenciaDesde;

    /** Null = vigente sin fecha de fin (lista actual). */
    @Column(name = "vigencia_hasta")
    private LocalDate vigenciaHasta;

    /** True si el precio está vigente en la fecha dada. */
    public boolean vigenteEn(LocalDate fecha) {
        boolean empezo = !fecha.isBefore(vigenciaDesde);
        boolean noTermino = vigenciaHasta == null || !fecha.isAfter(vigenciaHasta);
        return empezo && noTermino;
    }
}

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
 * listas: al subir precios nuevos se cierra el período anterior (validTo) en vez
 * de borrarlo. El motor de tasación busca el precio vigente a una fecha.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "plan_price")
public class PlanPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    private HealthPlan plan;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "age_band_id", nullable = false)
    private AgeBand ageBand;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    /** Null = vigente sin fecha de fin (lista actual). */
    @Column(name = "valid_to")
    private LocalDate validTo;

    /** True si el precio está vigente en la fecha dada. */
    public boolean isValidOn(LocalDate date) {
        boolean started = !date.isBefore(validFrom);
        boolean notEnded = validTo == null || !date.isAfter(validTo);
        return started && notEnded;
    }
}

package com.sa.healthplan.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Titular / prospecto. Es la raíz del agregado del grupo familiar: los
 * integrantes se persisten y borran a través del cliente (cascade + orphanRemoval).
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cliente",
        uniqueConstraints = @UniqueConstraint(name = "uk_cliente_documento",
                columnNames = {"tipo_documento", "numero_documento"}))
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false, length = 30)
    private TipoDocumento tipoDocumento;

    @Column(name = "numero_documento", nullable = false, length = 30)
    private String numeroDocumento;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    private String email;

    private String telefono;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Integrante> integrantes = new ArrayList<>();

    /** Agrega un integrante manteniendo la relación bidireccional consistente. */
    public void agregarIntegrante(Integrante integrante) {
        integrante.setCliente(this);
        integrantes.add(integrante);
    }

    /** Edad del titular en la fecha dada (años cumplidos). */
    public int edadEn(LocalDate fecha) {
        return Period.between(fechaNacimiento, fecha).getYears();
    }
}

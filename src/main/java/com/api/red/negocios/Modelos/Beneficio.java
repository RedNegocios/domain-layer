package com.api.red.negocios.Modelos;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Beneficio")
public class Beneficio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "beneficioId")
    private Integer beneficioId;

    @Column(name = "negocioNegocioId", nullable = false)
    private Integer negocioNegocioId;

    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;

    @Column(name = "valor", precision = 18, scale = 2)
    private BigDecimal valor;

    @Column(name = "visibleSoloAdmin", nullable = false)
    private Boolean visibleSoloAdmin = true;

    @Column(name = "tipoBeneficio", nullable = false, length = 100)
    private String tipoBeneficio;

    @Column(name = "condiciones", columnDefinition = "TEXT")
    private String condiciones;

    @Column(name = "creadoPor", nullable = false, length = 100)
    private String creadoPor;

    @Column(name = "fechaCreacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "modificadoPor", length = 100)
    private String modificadoPor;

    @Column(name = "fechaModificacion")
    private LocalDateTime fechaModificacion = LocalDateTime.now();

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "eliminadoPor", length = 100)
    private String eliminadoPor;

    @Column(name = "fechaEliminacion")
    private LocalDateTime fechaEliminacion;

    // Getters y setters
    // Constructor vacío y con parámetros
}


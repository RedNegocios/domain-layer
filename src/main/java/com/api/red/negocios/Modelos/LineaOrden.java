package com.api.red.negocios.Modelos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "LineaOrden")
public class LineaOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lineaOrdenId;

    @ManyToOne
    @JoinColumn(name = "ordenId", nullable = false)
    @JsonBackReference
    private Orden orden;

    @ManyToOne
    @JoinColumn(name = "negocioProductoId", nullable = false)
    private NegocioProducto negocioProducto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private BigDecimal precioUnitario;

    // Timestamp y gobernanza

    @Column(name = "fechaRegistro", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "creadoPor", length = 100)
    private String creadoPor;

    @Column(name = "fechaCreacion", columnDefinition = "DATETIME")
    private LocalDateTime fechaCreacion;

    @Column(name = "modificadoPor", length = 100)
    private String modificadoPor;

    @Column(name = "fechaModificacion", columnDefinition = "DATETIME")
    private LocalDateTime fechaModificacion;
}


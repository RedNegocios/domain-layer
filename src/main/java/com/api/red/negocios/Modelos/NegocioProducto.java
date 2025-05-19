package com.api.red.negocios.Modelos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
@Table(name = "NegocioProducto")
public class NegocioProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer negocioProductoId;

    @ManyToOne
    @JoinColumn(name = "negocioId", nullable = false)
    private Negocio negocio;

    @ManyToOne
    @JoinColumn(name = "productoId", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private BigDecimal precioDeVenta;

    @Column(nullable = false)
    private boolean visualizacionProducto;
}


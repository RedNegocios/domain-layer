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
@Table(name = "LineaOrden")
public class LineaOrden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lineaOrdenId;

    @ManyToOne
    @JoinColumn(name = "ordenId", nullable = false) // Configuración de la clave foránea con Orden
    private Orden orden;

    @ManyToOne
    @JoinColumn(name = "negocioProductoId", nullable = false) // Configuración de la clave foránea con NegocioProducto
    private NegocioProducto negocioProducto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private BigDecimal precioUnitario;
}

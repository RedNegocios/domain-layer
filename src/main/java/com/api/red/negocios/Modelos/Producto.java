package com.api.red.negocios.Modelos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productoId;

    // Campos de libro
    private String titulo;

    private String autor;

    private String isbn;

    private Integer año;

    private String editorial;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String imagenUrl;

    private BigDecimal precio;

    // Relación con Colecciones (género/categoría)
    @ManyToOne
    @JoinColumn(name = "coleccionId", nullable = false, foreignKey = @ForeignKey(name = "FK_Producto_Colecciones"))
    private Coleccion coleccion;

    @Column(updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private String modificadoPor;

    private LocalDateTime fechaModificacion = LocalDateTime.now();

    private Boolean activo = true;

    private String eliminadoPor;

    private LocalDateTime fechaEliminacion;
    
    @ManyToOne
    @JoinColumn(name = "categoriaId")
    private Categoria categoria;
}


package com.api.red.negocios.Modelos;

import lombok.Data;
import lombok.NoArgsConstructor;

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
@Table(name = "Colecciones")
public class Coleccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer coleccionId;

    @ManyToOne
    @JoinColumn(name = "productoId", nullable = false, foreignKey = @ForeignKey(name = "FK_Colecciones_Producto"))
    private Producto producto;

    private String titulo;

    private String autor;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String isbn;

    private Integer año;

    private String editorial;

    private String imagenUrl;

    private String tipoColeccion;

    @Column(updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private String modificadoPor;

    private LocalDateTime fechaModificacion = LocalDateTime.now();

    private Boolean activo = true;

    private String eliminadoPor;

    private LocalDateTime fechaEliminacion;
}

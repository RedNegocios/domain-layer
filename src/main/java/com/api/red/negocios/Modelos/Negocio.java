package com.api.red.negocios.Modelos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Negocio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Negocio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "negocioId")
    private Integer negocioId;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fechaCreacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "modificadoPor")
    private String modificadoPor;

    @Column(name = "fechaModificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "eliminadoPor")
    private String eliminadoPor;

    @Column(name = "fechaEliminacion")
    private LocalDateTime fechaEliminacion;
    
    @OneToMany(mappedBy = "negocio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioNegocio> usuarioNegocios;
}



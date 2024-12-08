package com.api.red.negocios.Modelos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "UsuarioNegocio")
@Data
public class UsuarioNegocio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer usuarioNegocioId;

    @ManyToOne
    @JoinColumn(name = "usuarioId", nullable = false)
    @JsonBackReference
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "negocioId", nullable = false)
    @JsonBackReference
    private Negocio negocio;

    private String creadoPor;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private String modificadoPor;

    private LocalDateTime fechaModificacion = LocalDateTime.now();

    private Boolean activo = true;

    private String eliminadoPor;

    private LocalDateTime fechaEliminacion;
}

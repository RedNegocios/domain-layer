package com.api.red.negocios.Modelos;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "MensajeOrden")
public class MensajeOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mensajeOrdenId;

    @ManyToOne
    @JoinColumn(name = "ordenId", nullable = false)
    private Orden orden;

    @ManyToOne
    @JoinColumn(name = "emisorUsuarioId")
    private Usuario emisorUsuario;

    @ManyToOne
    @JoinColumn(name = "emisorNegocioId")
    private Negocio emisorNegocio;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenido;

    private LocalDateTime fechaEnvio = LocalDateTime.now();
    private Boolean leido = false;

    // Gobernanza
    private String creadoPor;
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    private String modificadoPor;
    private LocalDateTime fechaModificacion = LocalDateTime.now();
    private Boolean activo = true;
    private String eliminadoPor;
    private LocalDateTime fechaEliminacion;

}


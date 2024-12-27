package com.api.red.negocios.Modelos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "NegocioNegocio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NegocioNegocio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "negocioNegocioId")
    private Integer negocioNegocioId;

    @ManyToOne
    @JoinColumn(name = "negocioId1", nullable = false)
    private Negocio negocio1;

    @ManyToOne
    @JoinColumn(name = "negocioId2", nullable = false)
    private Negocio negocio2;

    @ManyToOne
    @JoinColumn(name = "tipoRelacionId", nullable = false)
    private TipoRelacionNegocioNegocio tipoRelacion;

    @Column(name = "creadoPor", length = 100)
    private String creadoPor;

    @Column(name = "fechaCreacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "modificadoPor", length = 100)
    private String modificadoPor;

    @Column(name = "fechaModificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "eliminadoPor", length = 100)
    private String eliminadoPor;

    @Column(name = "fechaEliminacion")
    private LocalDateTime fechaEliminacion;
}


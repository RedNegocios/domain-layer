package com.api.red.negocios.Modelos;

import jakarta.persistence.*;

@Entity
@Table(name = "TipoRelacionNegocioNegocio")
public class TipoRelacionNegocioNegocio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipoRelacionId")
    private Integer tipoRelacionId;

    @Column(name = "descripcion", length = 100, nullable = false)
    private String descripcion;

    // Getters y setters
    public Integer getTipoRelacionId() {
        return tipoRelacionId;
    }

    public void setTipoRelacionId(Integer tipoRelacionId) {
        this.tipoRelacionId = tipoRelacionId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

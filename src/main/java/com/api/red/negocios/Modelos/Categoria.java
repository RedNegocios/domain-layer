package com.api.red.negocios.Modelos;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Categoria")
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoriaId;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "descripcion", length = 255)
    private String descripcion;
    
    @Column(name = "activo")
    private Boolean activo;
    
    @Column(name = "creadoPor", length = 100)
    private String creadoPor;
    
    @Column(name = "fechaCreacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "modificadoPor", length = 100)
    private String modificadoPor;
    
    @Column(name = "fechaModificacion")
    private LocalDateTime fechaModificacion;
    
    @Column(name = "eliminadoPor", length = 100)
    private String eliminadoPor;
    
    @Column(name = "fechaEliminacion")
    private LocalDateTime fechaEliminacion;
    
    // Constructors
    public Categoria() {}
    
    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaModificacion = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Integer getCategoriaId() {
        return categoriaId;
    }
    
    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    public String getCreadoPor() {
        return creadoPor;
    }
    
    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public String getModificadoPor() {
        return modificadoPor;
    }
    
    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }
    
    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }
    
    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
    
    public String getEliminadoPor() {
        return eliminadoPor;
    }
    
    public void setEliminadoPor(String eliminadoPor) {
        this.eliminadoPor = eliminadoPor;
    }
    
    public LocalDateTime getFechaEliminacion() {
        return fechaEliminacion;
    }
    
    public void setFechaEliminacion(LocalDateTime fechaEliminacion) {
        this.fechaEliminacion = fechaEliminacion;
    }
}
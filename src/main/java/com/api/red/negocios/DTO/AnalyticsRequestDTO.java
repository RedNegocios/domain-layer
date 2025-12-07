package com.api.red.negocios.DTO;

import java.time.LocalDate;
import java.util.List;

public class AnalyticsRequestDTO {
    private List<Integer> negocioIds;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String periodo; // "MENSUAL", "SEMANAL", "DIARIO"
    private Integer limit;
    private LocalDate fechaComparacionInicio;
    private LocalDate fechaComparacionFin;

    // Constructors
    public AnalyticsRequestDTO() {}

    public AnalyticsRequestDTO(List<Integer> negocioIds, LocalDate fechaInicio, LocalDate fechaFin) {
        this.negocioIds = negocioIds;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Getters and Setters
    public List<Integer> getNegocioIds() {
        return negocioIds;
    }

    public void setNegocioIds(List<Integer> negocioIds) {
        this.negocioIds = negocioIds;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public LocalDate getFechaComparacionInicio() {
        return fechaComparacionInicio;
    }

    public void setFechaComparacionInicio(LocalDate fechaComparacionInicio) {
        this.fechaComparacionInicio = fechaComparacionInicio;
    }

    public LocalDate getFechaComparacionFin() {
        return fechaComparacionFin;
    }

    public void setFechaComparacionFin(LocalDate fechaComparacionFin) {
        this.fechaComparacionFin = fechaComparacionFin;
    }
}
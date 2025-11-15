package com.api.red.negocios.DTO;

import java.math.BigDecimal;

public class KPIComparativoDTO {
    private BigDecimal ventasActual;
    private BigDecimal ventasAnterior;
    private Double crecimientoVentas;
    private Integer ordenesActual;
    private Integer ordenesAnterior;
    private Double crecimientoOrdenes;
    private Integer clientesNuevos;
    private Integer clientesNuevosAnterior;
    private Double crecimientoClientes;

    // Constructors
    public KPIComparativoDTO() {}

    public KPIComparativoDTO(BigDecimal ventasActual, BigDecimal ventasAnterior, Double crecimientoVentas,
                           Integer ordenesActual, Integer ordenesAnterior, Double crecimientoOrdenes,
                           Integer clientesNuevos, Integer clientesNuevosAnterior, Double crecimientoClientes) {
        this.ventasActual = ventasActual;
        this.ventasAnterior = ventasAnterior;
        this.crecimientoVentas = crecimientoVentas;
        this.ordenesActual = ordenesActual;
        this.ordenesAnterior = ordenesAnterior;
        this.crecimientoOrdenes = crecimientoOrdenes;
        this.clientesNuevos = clientesNuevos;
        this.clientesNuevosAnterior = clientesNuevosAnterior;
        this.crecimientoClientes = crecimientoClientes;
    }

    // Getters and Setters
    public BigDecimal getVentasActual() {
        return ventasActual;
    }

    public void setVentasActual(BigDecimal ventasActual) {
        this.ventasActual = ventasActual;
    }

    public BigDecimal getVentasAnterior() {
        return ventasAnterior;
    }

    public void setVentasAnterior(BigDecimal ventasAnterior) {
        this.ventasAnterior = ventasAnterior;
    }

    public Double getCrecimientoVentas() {
        return crecimientoVentas;
    }

    public void setCrecimientoVentas(Double crecimientoVentas) {
        this.crecimientoVentas = crecimientoVentas;
    }

    public Integer getOrdenesActual() {
        return ordenesActual;
    }

    public void setOrdenesActual(Integer ordenesActual) {
        this.ordenesActual = ordenesActual;
    }

    public Integer getOrdenesAnterior() {
        return ordenesAnterior;
    }

    public void setOrdenesAnterior(Integer ordenesAnterior) {
        this.ordenesAnterior = ordenesAnterior;
    }

    public Double getCrecimientoOrdenes() {
        return crecimientoOrdenes;
    }

    public void setCrecimientoOrdenes(Double crecimientoOrdenes) {
        this.crecimientoOrdenes = crecimientoOrdenes;
    }

    public Integer getClientesNuevos() {
        return clientesNuevos;
    }

    public void setClientesNuevos(Integer clientesNuevos) {
        this.clientesNuevos = clientesNuevos;
    }

    public Integer getClientesNuevosAnterior() {
        return clientesNuevosAnterior;
    }

    public void setClientesNuevosAnterior(Integer clientesNuevosAnterior) {
        this.clientesNuevosAnterior = clientesNuevosAnterior;
    }

    public Double getCrecimientoClientes() {
        return crecimientoClientes;
    }

    public void setCrecimientoClientes(Double crecimientoClientes) {
        this.crecimientoClientes = crecimientoClientes;
    }
}
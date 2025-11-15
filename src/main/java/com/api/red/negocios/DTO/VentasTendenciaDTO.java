package com.api.red.negocios.DTO;

import java.math.BigDecimal;
import java.util.List;

public class VentasTendenciaDTO {
    private List<String> periodos;
    private List<BigDecimal> ventasTotales;
    private List<Integer> cantidadOrdenes;
    private List<BigDecimal> ticketPromedio;
    private List<BigDecimal> crecimientoVentas;
    private List<BigDecimal> crecimientoOrdenes;

    // Constructors
    public VentasTendenciaDTO() {}

    public VentasTendenciaDTO(List<String> periodos, List<BigDecimal> ventasTotales, List<Integer> cantidadOrdenes, 
                              List<BigDecimal> ticketPromedio, List<BigDecimal> crecimientoVentas, List<BigDecimal> crecimientoOrdenes) {
        this.periodos = periodos;
        this.ventasTotales = ventasTotales;
        this.cantidadOrdenes = cantidadOrdenes;
        this.ticketPromedio = ticketPromedio;
        this.crecimientoVentas = crecimientoVentas;
        this.crecimientoOrdenes = crecimientoOrdenes;
    }

    // Getters and Setters
    public List<String> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<String> periodos) {
        this.periodos = periodos;
    }

    public List<BigDecimal> getVentasTotales() {
        return ventasTotales;
    }

    public void setVentasTotales(List<BigDecimal> ventasTotales) {
        this.ventasTotales = ventasTotales;
    }

    public List<Integer> getCantidadOrdenes() {
        return cantidadOrdenes;
    }

    public void setCantidadOrdenes(List<Integer> cantidadOrdenes) {
        this.cantidadOrdenes = cantidadOrdenes;
    }

    public List<BigDecimal> getTicketPromedio() {
        return ticketPromedio;
    }

    public void setTicketPromedio(List<BigDecimal> ticketPromedio) {
        this.ticketPromedio = ticketPromedio;
    }

    public List<BigDecimal> getCrecimientoVentas() {
        return crecimientoVentas;
    }

    public void setCrecimientoVentas(List<BigDecimal> crecimientoVentas) {
        this.crecimientoVentas = crecimientoVentas;
    }

    public List<BigDecimal> getCrecimientoOrdenes() {
        return crecimientoOrdenes;
    }

    public void setCrecimientoOrdenes(List<BigDecimal> crecimientoOrdenes) {
        this.crecimientoOrdenes = crecimientoOrdenes;
    }
}
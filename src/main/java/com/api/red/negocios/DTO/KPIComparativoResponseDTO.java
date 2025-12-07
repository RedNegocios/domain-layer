package com.api.red.negocios.DTO;

import java.math.BigDecimal;

public class KPIComparativoResponseDTO {
    private PeriodoKPI periodoActual;
    private PeriodoKPI periodoAnterior;
    private VariacionesKPI variaciones;

    // Constructor vacío
    public KPIComparativoResponseDTO() {}

    // Constructor completo
    public KPIComparativoResponseDTO(PeriodoKPI periodoActual, PeriodoKPI periodoAnterior, VariacionesKPI variaciones) {
        this.periodoActual = periodoActual;
        this.periodoAnterior = periodoAnterior;
        this.variaciones = variaciones;
    }

    // Getters y Setters
    public PeriodoKPI getPeriodoActual() { return periodoActual; }
    public void setPeriodoActual(PeriodoKPI periodoActual) { this.periodoActual = periodoActual; }

    public PeriodoKPI getPeriodoAnterior() { return periodoAnterior; }
    public void setPeriodoAnterior(PeriodoKPI periodoAnterior) { this.periodoAnterior = periodoAnterior; }

    public VariacionesKPI getVariaciones() { return variaciones; }
    public void setVariaciones(VariacionesKPI variaciones) { this.variaciones = variaciones; }

    // Clase interna para períodos
    public static class PeriodoKPI {
        private BigDecimal ventasTotales;
        private Integer cantidadOrdenes;
        private Integer clientesUnicos;
        private BigDecimal ticketPromedio;
        private BigDecimal ingresosDiarios;

        public PeriodoKPI() {}

        public PeriodoKPI(BigDecimal ventasTotales, Integer cantidadOrdenes, Integer clientesUnicos, 
                         BigDecimal ticketPromedio, BigDecimal ingresosDiarios) {
            this.ventasTotales = ventasTotales;
            this.cantidadOrdenes = cantidadOrdenes;
            this.clientesUnicos = clientesUnicos;
            this.ticketPromedio = ticketPromedio;
            this.ingresosDiarios = ingresosDiarios;
        }

        // Getters y Setters
        public BigDecimal getVentasTotales() { return ventasTotales; }
        public void setVentasTotales(BigDecimal ventasTotales) { this.ventasTotales = ventasTotales; }

        public Integer getCantidadOrdenes() { return cantidadOrdenes; }
        public void setCantidadOrdenes(Integer cantidadOrdenes) { this.cantidadOrdenes = cantidadOrdenes; }

        public Integer getClientesUnicos() { return clientesUnicos; }
        public void setClientesUnicos(Integer clientesUnicos) { this.clientesUnicos = clientesUnicos; }

        public BigDecimal getTicketPromedio() { return ticketPromedio; }
        public void setTicketPromedio(BigDecimal ticketPromedio) { this.ticketPromedio = ticketPromedio; }

        public BigDecimal getIngresosDiarios() { return ingresosDiarios; }
        public void setIngresosDiarios(BigDecimal ingresosDiarios) { this.ingresosDiarios = ingresosDiarios; }
    }

    // Clase interna para variaciones
    public static class VariacionesKPI {
        private BigDecimal ventasTotales;
        private BigDecimal cantidadOrdenes;
        private BigDecimal clientesUnicos;
        private BigDecimal ticketPromedio;
        private BigDecimal ingresosDiarios;

        public VariacionesKPI() {}

        public VariacionesKPI(BigDecimal ventasTotales, BigDecimal cantidadOrdenes, BigDecimal clientesUnicos, 
                             BigDecimal ticketPromedio, BigDecimal ingresosDiarios) {
            this.ventasTotales = ventasTotales;
            this.cantidadOrdenes = cantidadOrdenes;
            this.clientesUnicos = clientesUnicos;
            this.ticketPromedio = ticketPromedio;
            this.ingresosDiarios = ingresosDiarios;
        }

        // Getters y Setters
        public BigDecimal getVentasTotales() { return ventasTotales; }
        public void setVentasTotales(BigDecimal ventasTotales) { this.ventasTotales = ventasTotales; }

        public BigDecimal getCantidadOrdenes() { return cantidadOrdenes; }
        public void setCantidadOrdenes(BigDecimal cantidadOrdenes) { this.cantidadOrdenes = cantidadOrdenes; }

        public BigDecimal getClientesUnicos() { return clientesUnicos; }
        public void setClientesUnicos(BigDecimal clientesUnicos) { this.clientesUnicos = clientesUnicos; }

        public BigDecimal getTicketPromedio() { return ticketPromedio; }
        public void setTicketPromedio(BigDecimal ticketPromedio) { this.ticketPromedio = ticketPromedio; }

        public BigDecimal getIngresosDiarios() { return ingresosDiarios; }
        public void setIngresosDiarios(BigDecimal ingresosDiarios) { this.ingresosDiarios = ingresosDiarios; }
    }
}
package com.api.red.negocios.DTO;

import java.math.BigDecimal;
import java.util.List;

public class EmbudoConversionResponseDTO {
    private Integer visitantes;
    private Integer interesados;
    private Integer ordenesCreadas;
    private Integer ordenesCompletadas;
    private BigDecimal tasaInteres;
    private BigDecimal tasaConversion;
    private BigDecimal tasaCompletacion;
    private List<EmbudoDetalleDTO> embudoDetalle;

    // Constructor vacío
    public EmbudoConversionResponseDTO() {}

    // Constructor completo
    public EmbudoConversionResponseDTO(Integer visitantes, Integer interesados, Integer ordenesCreadas, 
                                     Integer ordenesCompletadas, BigDecimal tasaInteres, BigDecimal tasaConversion,
                                     BigDecimal tasaCompletacion, List<EmbudoDetalleDTO> embudoDetalle) {
        this.visitantes = visitantes;
        this.interesados = interesados;
        this.ordenesCreadas = ordenesCreadas;
        this.ordenesCompletadas = ordenesCompletadas;
        this.tasaInteres = tasaInteres;
        this.tasaConversion = tasaConversion;
        this.tasaCompletacion = tasaCompletacion;
        this.embudoDetalle = embudoDetalle;
    }

    // Getters y Setters
    public Integer getVisitantes() { return visitantes; }
    public void setVisitantes(Integer visitantes) { this.visitantes = visitantes; }

    public Integer getInteresados() { return interesados; }
    public void setInteresados(Integer interesados) { this.interesados = interesados; }

    public Integer getOrdenesCreadas() { return ordenesCreadas; }
    public void setOrdenesCreadas(Integer ordenesCreadas) { this.ordenesCreadas = ordenesCreadas; }

    public Integer getOrdenesCompletadas() { return ordenesCompletadas; }
    public void setOrdenesCompletadas(Integer ordenesCompletadas) { this.ordenesCompletadas = ordenesCompletadas; }

    public BigDecimal getTasaInteres() { return tasaInteres; }
    public void setTasaInteres(BigDecimal tasaInteres) { this.tasaInteres = tasaInteres; }

    public BigDecimal getTasaConversion() { return tasaConversion; }
    public void setTasaConversion(BigDecimal tasaConversion) { this.tasaConversion = tasaConversion; }

    public BigDecimal getTasaCompletacion() { return tasaCompletacion; }
    public void setTasaCompletacion(BigDecimal tasaCompletacion) { this.tasaCompletacion = tasaCompletacion; }

    public List<EmbudoDetalleDTO> getEmbudoDetalle() { return embudoDetalle; }
    public void setEmbudoDetalle(List<EmbudoDetalleDTO> embudoDetalle) { this.embudoDetalle = embudoDetalle; }

    // Clase interna para el detalle del embudo
    public static class EmbudoDetalleDTO {
        private String etapa;
        private Integer cantidad;
        private BigDecimal porcentaje;
        private BigDecimal conversion;

        public EmbudoDetalleDTO() {}

        public EmbudoDetalleDTO(String etapa, Integer cantidad, BigDecimal porcentaje, BigDecimal conversion) {
            this.etapa = etapa;
            this.cantidad = cantidad;
            this.porcentaje = porcentaje;
            this.conversion = conversion;
        }

        // Getters y Setters
        public String getEtapa() { return etapa; }
        public void setEtapa(String etapa) { this.etapa = etapa; }

        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

        public BigDecimal getPorcentaje() { return porcentaje; }
        public void setPorcentaje(BigDecimal porcentaje) { this.porcentaje = porcentaje; }

        public BigDecimal getConversion() { return conversion; }
        public void setConversion(BigDecimal conversion) { this.conversion = conversion; }
    }
}
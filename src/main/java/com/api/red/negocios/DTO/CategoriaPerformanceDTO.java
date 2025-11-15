package com.api.red.negocios.DTO;

import java.math.BigDecimal;

public class CategoriaPerformanceDTO {
    private Integer categoriaId;
    private String nombreCategoria;
    private BigDecimal ventasTotales;
    private Integer cantidadVendida;
    private BigDecimal margenPromedio;
    private BigDecimal participacionVentas;
    private BigDecimal crecimientoVentas;
    private String color;

    // Constructor vacío
    public CategoriaPerformanceDTO() {}

    // Constructor completo
    public CategoriaPerformanceDTO(Integer categoriaId, String nombreCategoria, BigDecimal ventasTotales, 
                                   Integer cantidadVendida, BigDecimal margenPromedio, BigDecimal participacionVentas, 
                                   BigDecimal crecimientoVentas, String color) {
        this.categoriaId = categoriaId;
        this.nombreCategoria = nombreCategoria;
        this.ventasTotales = ventasTotales;
        this.cantidadVendida = cantidadVendida;
        this.margenPromedio = margenPromedio;
        this.participacionVentas = participacionVentas;
        this.crecimientoVentas = crecimientoVentas;
        this.color = color;
    }

    // Getters y Setters
    public Integer getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }

    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }

    public BigDecimal getVentasTotales() { return ventasTotales; }
    public void setVentasTotales(BigDecimal ventasTotales) { this.ventasTotales = ventasTotales; }

    public Integer getCantidadVendida() { return cantidadVendida; }
    public void setCantidadVendida(Integer cantidadVendida) { this.cantidadVendida = cantidadVendida; }

    public BigDecimal getMargenPromedio() { return margenPromedio; }
    public void setMargenPromedio(BigDecimal margenPromedio) { this.margenPromedio = margenPromedio; }

    public BigDecimal getParticipacionVentas() { return participacionVentas; }
    public void setParticipacionVentas(BigDecimal participacionVentas) { this.participacionVentas = participacionVentas; }

    public BigDecimal getCrecimientoVentas() { return crecimientoVentas; }
    public void setCrecimientoVentas(BigDecimal crecimientoVentas) { this.crecimientoVentas = crecimientoVentas; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
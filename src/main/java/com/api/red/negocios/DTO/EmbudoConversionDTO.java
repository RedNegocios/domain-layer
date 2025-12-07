package com.api.red.negocios.DTO;

public class EmbudoConversionDTO {
    private Integer visitantes;
    private Integer productosVistos;
    private Integer agregarCarrito;
    private Integer checkoutIniciado;
    private Integer comprasCompletadas;

    // Constructors
    public EmbudoConversionDTO() {}

    public EmbudoConversionDTO(Integer visitantes, Integer productosVistos, Integer agregarCarrito, 
                             Integer checkoutIniciado, Integer comprasCompletadas) {
        this.visitantes = visitantes;
        this.productosVistos = productosVistos;
        this.agregarCarrito = agregarCarrito;
        this.checkoutIniciado = checkoutIniciado;
        this.comprasCompletadas = comprasCompletadas;
    }

    // Getters and Setters
    public Integer getVisitantes() {
        return visitantes;
    }

    public void setVisitantes(Integer visitantes) {
        this.visitantes = visitantes;
    }

    public Integer getProductosVistos() {
        return productosVistos;
    }

    public void setProductosVistos(Integer productosVistos) {
        this.productosVistos = productosVistos;
    }

    public Integer getAgregarCarrito() {
        return agregarCarrito;
    }

    public void setAgregarCarrito(Integer agregarCarrito) {
        this.agregarCarrito = agregarCarrito;
    }

    public Integer getCheckoutIniciado() {
        return checkoutIniciado;
    }

    public void setCheckoutIniciado(Integer checkoutIniciado) {
        this.checkoutIniciado = checkoutIniciado;
    }

    public Integer getComprasCompletadas() {
        return comprasCompletadas;
    }

    public void setComprasCompletadas(Integer comprasCompletadas) {
        this.comprasCompletadas = comprasCompletadas;
    }
}
package com.api.red.negocios.DTO;

import java.math.BigDecimal;

import com.api.red.negocios.Modelos.NegocioProducto;

import lombok.Data;

@Data
public class LineaOrdenDTO {
    private NegocioProducto negocioProducto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
}


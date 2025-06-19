package com.api.red.negocios.DTO;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class IngresoAgregadoDTO {
    private Integer dia;
    private Integer mes;
    private Integer anio;
    private BigDecimal total;

    public IngresoAgregadoDTO(Integer dia, Integer mes, Integer anio, BigDecimal total) {
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
        this.total = total;
    }

    
}


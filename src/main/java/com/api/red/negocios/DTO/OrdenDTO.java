package com.api.red.negocios.DTO;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class OrdenDTO {
    private Integer negocioId;
    private BigDecimal montoTotal;
    private List<LineaOrdenDTO> lineasOrden;
}

package com.api.red.negocios.DTO;

import lombok.Data;

@Data
public class MensajeOrdenDTO {
    private Integer emisorUsuarioId;
    private Integer emisorNegocioId;
    private String contenido;
}


package com.api.red.negocios.DTO;

import lombok.Data;

@Data
public class MensajeOrdenRespuestaDTO {
    private Integer mensajeOrdenId; // <- cambia esto de Long a Integer
    private String contenido;
    private String emisorNombre;
}


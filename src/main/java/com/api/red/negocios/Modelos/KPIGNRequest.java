package com.api.red.negocios.Modelos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KPIGNRequest {
    private String token;

	private String negocioId;
}

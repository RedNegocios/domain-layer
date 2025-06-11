package com.api.red.negocios.Modelos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KPIITRequest {
	
    private String token;
    private Long negocioId;
}

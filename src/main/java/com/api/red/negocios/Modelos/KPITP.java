package com.api.red.negocios.Modelos;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KPITP {

	private Long productoId;
	private String nombre;
	private Long totalVendida;
}


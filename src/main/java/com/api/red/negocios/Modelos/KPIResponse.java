package com.api.red.negocios.Modelos;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KPIResponse {
	private KPIGNResponse kPIGNResponse;
	private KPIITResponse kPIITResponse;
	private List<KPITP> listTopMasVendidos; 
}

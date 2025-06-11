package com.api.red.negocios.Controladores;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.red.negocios.Modelos.KPIITRequest;
import com.api.red.negocios.Modelos.KPIITResponse;
import com.api.red.negocios.Repositorios.KPIITRepositorio;

@RestController
@RequestMapping("/kpis")
public class KPIITControlador {

@Autowired
	    private KPIITRepositorio ingresoRepository;

	    @PostMapping("/ingresos-totales")
	    public KPIITResponse obtenerIngresosTotales(@RequestBody KPIITRequest request) {
	        BigDecimal ganancia = ingresoRepository.obtenerIngresosTotales(
	            request.getToken(), 
	            request.getNegocioId()
	        );
	        return new KPIITResponse(ganancia);
	    }
	}

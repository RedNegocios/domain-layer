package com.api.red.negocios.Controladores;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.red.negocios.Modelos.KPIITRequest;
import com.api.red.negocios.Modelos.KPIITResponse;
import com.api.red.negocios.Repositorios.KPIITRepositorio;

@RestController
@RequestMapping("/api/kpi")
public class KPIITControlador {

    @Autowired
    private KPIITRepositorio ingresoRepository;

    @PostMapping("/ingresos-totales")
    public KPIITResponse obtenerIngresosTotales(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody KPIITRequest request) {

        // El header tiene el formato "Bearer eyJhbGciOi..."
        String token = authHeader.replace("Bearer ", "").trim();

        BigDecimal ganancia = ingresoRepository.obtenerIngresosTotales(
            token,
            request.getNegocioId()
        );

        return new KPIITResponse(ganancia);
    }
}



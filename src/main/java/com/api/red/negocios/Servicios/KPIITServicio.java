package com.api.red.negocios.Servicios;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.red.negocios.Modelos.KPIITResponse;
import com.api.red.negocios.Repositorios.KPIITRepositorio;

@Service
public class KPIITServicio {

    @Autowired
    private KPIITRepositorio ingresoRepository;

    public KPIITResponse obtenerIngresosTotales(String authHeader, Long negocioId) {
        // Extraer el token del header eliminando el prefijo "Bearer "
        String token = authHeader.replace("Bearer ", "").trim();

        BigDecimal ganancia = ingresoRepository.obtenerIngresosTotales(token, negocioId);

        return new KPIITResponse(ganancia);
    }
}


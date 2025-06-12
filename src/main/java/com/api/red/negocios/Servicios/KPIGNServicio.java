package com.api.red.negocios.Servicios;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.red.negocios.Modelos.KPIGNResponse;
import com.api.red.negocios.Repositorios.KPIGNRepositorio;

@Service
public class KPIGNServicio {

    @Autowired
    private KPIGNRepositorio gananciaRepository;

    public KPIGNResponse obtenerGananciaNeta(String token, Long negocioId) {
        BigDecimal ganancia = gananciaRepository.obtenerGananciaNeta(token, negocioId);

        return new KPIGNResponse(ganancia);
    }
}


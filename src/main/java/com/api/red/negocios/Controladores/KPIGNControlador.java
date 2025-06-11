package com.api.red.negocios.Controladores;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.red.negocios.Modelos.KPIGNRequest;
import com.api.red.negocios.Modelos.KPIGNResponse;
import com.api.red.negocios.Repositorios.KPIGNRepositorio;

@RestController
@RequestMapping("/kpis")
public class KPIGNControlador {

    @Autowired
    private KPIGNRepositorio gananciaRepository;

    @PostMapping("/ganancia-neta")
    public KPIGNResponse obtenerGananciaNeta(@RequestBody KPIGNRequest request) {
        BigDecimal ganancia = gananciaRepository.obtenerGananciaNeta(
            request.getToken(), 
            request.getNegocioId()
        );
        return new KPIGNResponse(ganancia);
    }
}

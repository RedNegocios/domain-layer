package com.api.red.negocios.Controladores;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.red.negocios.Modelos.KPIGNResponse;
import com.api.red.negocios.Modelos.KPIGlobal;
import com.api.red.negocios.Modelos.KPIITResponse;
import com.api.red.negocios.Modelos.KPIResponse;
import com.api.red.negocios.Modelos.KPITP;
import com.api.red.negocios.Servicios.KPIGNServicio;
import com.api.red.negocios.Servicios.KPIITServicio;
import com.api.red.negocios.Servicios.KPITPServicio;

@RestController
@RequestMapping("/api/kpi")
public class OperadorKPIControlador {

    @Autowired
    private KPIGNServicio gananciaServicio;

    @Autowired
    private KPIITServicio ingresoServicio;
    
    @Autowired
    private KPITPServicio totalProductoServicio;

    @PostMapping("/global")
    public KPIResponse obtenerKPIsGlobales(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody KPIGlobal request) {

        // Extraer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token inválido o ausente");
        }
        String token = authHeader.replace("Bearer ", "").trim();

        // Obtener negocioId desde el request
        Long negocioId = request.getNegocioId();

        // Usar los servicios correctamente
        KPIGNResponse gananciaResponse = gananciaServicio.obtenerGananciaNeta(token, negocioId);
        KPIITResponse ingresoResponse = ingresoServicio.obtenerIngresosTotales(token, negocioId);
        List<KPITP> totales = totalProductoServicio.obtenerTopProductos(token, negocioId);

        // Armar respuesta compuesta
        return new KPIResponse(gananciaResponse, ingresoResponse, totales);
    }
}



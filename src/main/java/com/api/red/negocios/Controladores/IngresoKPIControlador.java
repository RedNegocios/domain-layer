package com.api.red.negocios.Controladores;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.red.negocios.DTO.IngresoAgregadoDTO;
import com.api.red.negocios.Modelos.KPIIngresoPeriodoRequest;
import com.api.red.negocios.Servicios.KPIIngresoServicio;


@RestController
@RequestMapping("/api/kpi/ingresos")
public class IngresoKPIControlador {

    @Autowired
    private KPIIngresoServicio ingresoServicio;

    @PostMapping("/por-periodo")
    public ResponseEntity<List<IngresoAgregadoDTO>> obtenerIngresosPorPeriodo(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody KPIIngresoPeriodoRequest request) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        try {
            List<IngresoAgregadoDTO> ingresos = ingresoServicio.obtenerIngresosPorPeriodo(authHeader, request);
            return ResponseEntity.ok(ingresos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }
}



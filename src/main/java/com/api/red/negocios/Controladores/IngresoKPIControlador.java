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

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/kpi/ingresos")
@Slf4j                      // <― habilita log.info / log.debug / log.error …
public class IngresoKPIControlador {

    @Autowired
    private KPIIngresoServicio ingresoServicio;

    @PostMapping("/por-periodo")
    public ResponseEntity<List<IngresoAgregadoDTO>> obtenerIngresosPorPeriodo(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody KPIIngresoPeriodoRequest request) {

        /* ─────────── validación header ─────────── */
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Solicitud sin token Bearer o token mal formado");
            return ResponseEntity.badRequest().build();
        }

        // Sólo para trazas: mostrar 10 primeros chars del token
        String tokenPreview = authHeader.length() > 15
                ? authHeader.substring(7, 17) + "…"
                : authHeader.substring(7);

        log.info("► POST /api/kpi/ingresos/por-periodo " +
                 "[negocioId={}, tipo={}, fechaInicio={}, fechaFin={}, token={} ]",
                 request.getNegocioId(), request.getTipo(),
                 request.getFechaInicio(), request.getFechaFin(),
                 tokenPreview);

        try {
            List<IngresoAgregadoDTO> ingresos =
                    ingresoServicio.obtenerIngresosPorPeriodo(authHeader, request);

            log.info("◄ Respuesta OK – registros devueltos: {}", ingresos.size());
            return ResponseEntity.ok(ingresos);

        } catch (IllegalArgumentException e) {
            log.error("Argumento inválido: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Collections.emptyList());

        } catch (Exception e) {
            // cualquier otra excepción no controlada
            log.error("Error inesperado obteniendo ingresos por periodo", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}




package com.api.red.negocios.Servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.red.negocios.DTO.IngresoAgregadoDTO;
import com.api.red.negocios.Modelos.KPIIngresoPeriodoRequest;
import com.api.red.negocios.Repositorios.IngresoRepositorio;

@Service
public class KPIIngresoServicio {

    @Autowired
    private IngresoRepositorio ingresoRepository;

    /**
     * Devuelve los ingresos agregados (día, mes o año) dentro de un rango de fechas.
     *
     * @param authHeader “Bearer &lt;token&gt;”
     * @param request    cuerpo JSON con negocioId, tipo, fechaInicio, fechaFin
     */
    public List<IngresoAgregadoDTO> obtenerIngresosPorPeriodo(
            String authHeader,
            KPIIngresoPeriodoRequest request) {

        /* ───── Validar y extraer el token ───── */
        String token = Optional.ofNullable(authHeader)
                               .filter(h -> h.startsWith("Bearer "))
                               .map(h -> h.substring(7).trim())
                               .orElseThrow(() -> new IllegalArgumentException("Token inválido o ausente"));

        /* ───── Validar parámetros obligatorios ───── */
        if (request.getNegocioId()  == null ||
            request.getTipo()       == null ||
            request.getFechaInicio() == null ||
            request.getFechaFin()    == null) {
            throw new IllegalArgumentException(
                    "Faltan parámetros obligatorios: negocioId, tipo, fechaInicio o fechaFin");
        }

        /* ───── Delegar a la capa de acceso a datos ───── */
        return ingresoRepository.obtenerIngresosPorPeriodo(
                token,
                request.getNegocioId(),
                request.getTipo(),
                request.getFechaInicio(),
                request.getFechaFin());
    }
}


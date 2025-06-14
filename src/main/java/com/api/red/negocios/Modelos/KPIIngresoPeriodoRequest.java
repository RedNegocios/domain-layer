package com.api.red.negocios.Modelos;

import java.time.LocalDate;

import lombok.Data;

@Data
public class KPIIngresoPeriodoRequest {
    private Long negocioId;
    private String tipo; // "dia", "mes", "anio"
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    // Getters y setters
}


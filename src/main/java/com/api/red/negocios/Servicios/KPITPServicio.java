package com.api.red.negocios.Servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.red.negocios.Modelos.KPITP;
import com.api.red.negocios.Repositorios.KPITPRepositorio;

@Service
public class KPITPServicio {

    @Autowired
    private KPITPRepositorio repo;

    public List<KPITP> obtenerTopProductos(String token, Long negocioId) {
        List<KPITP> resultados = repo.obtenerTopProductosVendidos(token, negocioId);

        return resultados;
    }
}


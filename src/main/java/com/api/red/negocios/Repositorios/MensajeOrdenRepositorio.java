package com.api.red.negocios.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.red.negocios.Modelos.MensajeOrden;

@Repository
public interface MensajeOrdenRepositorio extends JpaRepository<MensajeOrden, Integer> {
    List<MensajeOrden> findByOrdenOrdenIdOrderByFechaEnvioAsc(Integer ordenId);
}


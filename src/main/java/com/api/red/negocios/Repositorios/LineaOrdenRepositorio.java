package com.api.red.negocios.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.red.negocios.Modelos.LineaOrden;

@Repository
public interface LineaOrdenRepositorio extends JpaRepository<LineaOrden, Integer> {
}

package com.api.red.negocios.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.red.negocios.Modelos.LineaOrden;
import com.api.red.negocios.Modelos.Orden;

@Repository
public interface LineaOrdenRepositorio extends JpaRepository<LineaOrden, Integer> {
	List<LineaOrden> findByOrden(Orden orden);
}

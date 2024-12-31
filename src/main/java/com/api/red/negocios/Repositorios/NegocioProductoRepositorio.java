package com.api.red.negocios.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.red.negocios.Modelos.NegocioProducto;

@Repository
public interface NegocioProductoRepositorio extends JpaRepository<NegocioProducto, Integer> {
}

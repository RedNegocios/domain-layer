package com.api.red.negocios.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.red.negocios.Modelos.Negocio;
import com.api.red.negocios.Modelos.NegocioProducto;

@Repository
public interface NegocioProductoRepositorio extends JpaRepository<NegocioProducto, Integer> {
    List<NegocioProducto> findByNegocio(Negocio negocio);
}

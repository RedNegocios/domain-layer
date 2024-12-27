package com.api.red.negocios.Repositorios;

import com.api.red.negocios.Modelos.NegocioNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NegocioNegocioRepositorio extends JpaRepository<NegocioNegocio, Integer> {
    List<NegocioNegocio> findByNegocio1NegocioId(Integer negocioId1);
    List<NegocioNegocio> findByNegocio2NegocioId(Integer negocioId2);
}

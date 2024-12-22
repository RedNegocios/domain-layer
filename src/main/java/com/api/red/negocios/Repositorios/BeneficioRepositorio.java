package com.api.red.negocios.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.red.negocios.Modelos.Beneficio;

import java.util.List;

@Repository
public interface BeneficioRepositorio extends JpaRepository<Beneficio, Integer> {
    List<Beneficio> findByNegocioNegocioId(Integer negocioNegocioId);

    List<Beneficio> findByActivo(Boolean activo);
}


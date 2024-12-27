package com.api.red.negocios.Repositorios;

import org.springframework.stereotype.Repository;

import com.api.red.negocios.Modelos.TipoRelacionNegocioNegocio;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface TipoRelacionNegocioNegocioRepositorio extends JpaRepository<TipoRelacionNegocioNegocio, Integer> {
    // Métodos adicionales, si es necesario
}


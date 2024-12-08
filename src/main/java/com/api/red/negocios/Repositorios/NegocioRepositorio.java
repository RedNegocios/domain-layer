package com.api.red.negocios.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.red.negocios.Modelos.Negocio;

public interface NegocioRepositorio extends JpaRepository<Negocio, Integer> {
}

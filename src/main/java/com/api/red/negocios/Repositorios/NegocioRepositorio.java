package com.api.red.negocios.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.red.negocios.Modelos.Negocio;

@Repository
public interface NegocioRepositorio extends JpaRepository<Negocio, Integer> {
}

package com.api.red.negocios.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.red.negocios.Modelos.Orden;

@Repository
public interface OrdenRepositorio extends JpaRepository<Orden, Integer> {
}
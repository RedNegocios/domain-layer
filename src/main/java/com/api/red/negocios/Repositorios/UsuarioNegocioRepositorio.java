package com.api.red.negocios.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.red.negocios.Modelos.UsuarioNegocio;

public interface UsuarioNegocioRepositorio extends JpaRepository<UsuarioNegocio, Integer> {
}

package com.api.red.negocios.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.red.negocios.Modelos.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
	Usuario findByUsername(String username);
}

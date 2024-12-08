package com.api.red.negocios.Repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.red.negocios.Modelos.UsuarioToken;

public interface UsuarioTokenRepositorio extends JpaRepository<UsuarioToken, Integer> {
    List<UsuarioToken> findByUsuarioUsuarioId(Integer usuarioId);
    Optional<UsuarioToken> findByToken(String token);
}

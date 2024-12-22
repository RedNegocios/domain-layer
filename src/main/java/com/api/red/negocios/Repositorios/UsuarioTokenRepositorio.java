package com.api.red.negocios.Repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.red.negocios.Modelos.UsuarioToken;

@Repository
public interface UsuarioTokenRepositorio extends JpaRepository<UsuarioToken, Integer> {
    List<UsuarioToken> findByUsuarioUsuarioId(Integer usuarioId);
    Optional<UsuarioToken> findByToken(String token);
}

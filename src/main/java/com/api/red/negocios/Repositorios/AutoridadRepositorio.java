package com.api.red.negocios.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.red.negocios.Modelos.Autoridad;

public interface AutoridadRepositorio extends JpaRepository<Autoridad, Integer> {
    List<Autoridad> findByUsuarioUsuarioId(Integer usuarioId);
}

package com.api.red.negocios.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.red.negocios.Modelos.Autoridad;

@Repository
public interface AutoridadRepositorio extends JpaRepository<Autoridad, Integer> {
    List<Autoridad> findByUsuarioUsuarioId(Integer usuarioId);
}

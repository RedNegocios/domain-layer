package com.api.red.negocios.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.red.negocios.Modelos.Negocio;
import com.api.red.negocios.Modelos.Usuario;
import com.api.red.negocios.Modelos.UsuarioNegocio;

@Repository
public interface UsuarioNegocioRepositorio extends JpaRepository<UsuarioNegocio, Integer> {

    // Encontrar registros por negocio y estatus
    List<UsuarioNegocio> findByNegocioAndEstatusId(Negocio negocio, Integer estatusId);

    // Encontrar registros por usuario
    List<UsuarioNegocio> findByUsuario(Usuario usuario);
}


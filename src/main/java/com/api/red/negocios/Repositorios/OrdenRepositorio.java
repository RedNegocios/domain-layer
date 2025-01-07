package com.api.red.negocios.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.red.negocios.Modelos.Negocio;
import com.api.red.negocios.Modelos.Orden;
import com.api.red.negocios.Modelos.Usuario;

@Repository
public interface OrdenRepositorio extends JpaRepository<Orden, Integer> {
    List<Orden> findByUsuario(Usuario usuario);
    List<Orden> findByNegocio(Negocio negocio);
}

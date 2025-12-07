package com.api.red.negocios.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.red.negocios.Modelos.Categoria;

@Repository
public interface CategoriaRepositorio extends JpaRepository<Categoria, Integer> {
    List<Categoria> findByActivoTrue();
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);
    List<Categoria> findByActivoTrueOrderByNombreAsc();
}
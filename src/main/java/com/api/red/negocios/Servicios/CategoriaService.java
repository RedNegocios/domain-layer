package com.api.red.negocios.Servicios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.red.negocios.Excepciones.EntityNotFoundException;
import com.api.red.negocios.Modelos.Categoria;
import com.api.red.negocios.Repositorios.CategoriaRepositorio;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    public List<Categoria> obtenerTodasLasCategorias() {
        return categoriaRepositorio.findByActivoTrueOrderByNombreAsc();
    }

    public Page<Categoria> obtenerCategoriasConPaginacion(Pageable pageable) {
        return categoriaRepositorio.findAll(pageable);
    }

    public Categoria obtenerCategoriaPorId(Integer id) {
        return categoriaRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + id));
    }

    public List<Categoria> buscarCategoriasPorNombre(String nombre) {
        return categoriaRepositorio.findByNombreContainingIgnoreCase(nombre);
    }

    public Categoria crearCategoria(Categoria categoria) {
        // Validaciones básicas
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es requerido");
        }

        // Establecer valores por defecto
        categoria.setActivo(true);
        categoria.setFechaCreacion(LocalDateTime.now());
        categoria.setFechaModificacion(LocalDateTime.now());

        return categoriaRepositorio.save(categoria);
    }

    public Categoria actualizarCategoria(Integer id, Categoria categoriaActualizada) {
        Categoria categoriaExistente = obtenerCategoriaPorId(id);

        // Actualizar campos
        categoriaExistente.setNombre(categoriaActualizada.getNombre());
        categoriaExistente.setDescripcion(categoriaActualizada.getDescripcion());
        categoriaExistente.setModificadoPor(categoriaActualizada.getModificadoPor());
        categoriaExistente.setFechaModificacion(LocalDateTime.now());

        return categoriaRepositorio.save(categoriaExistente);
    }

    public void eliminarCategoria(Integer id, String eliminadoPor) {
        Categoria categoria = obtenerCategoriaPorId(id);
        
        // Soft delete
        categoria.setActivo(false);
        categoria.setEliminadoPor(eliminadoPor);
        categoria.setFechaEliminacion(LocalDateTime.now());
        
        categoriaRepositorio.save(categoria);
    }

    public void activarCategoria(Integer id, String modificadoPor) {
        Categoria categoria = obtenerCategoriaPorId(id);
        
        categoria.setActivo(true);
        categoria.setModificadoPor(modificadoPor);
        categoria.setFechaModificacion(LocalDateTime.now());
        categoria.setEliminadoPor(null);
        categoria.setFechaEliminacion(null);
        
        categoriaRepositorio.save(categoria);
    }

    public boolean existeCategoriaPorNombre(String nombre) {
        return categoriaRepositorio.findByNombreContainingIgnoreCase(nombre).size() > 0;
    }
}
package com.api.red.negocios.Controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.red.negocios.Modelos.Categoria;
import com.api.red.negocios.Servicios.CategoriaService;

@RestController
@RequestMapping("/api/categorias")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('ADMIN_NEGOCIO')")
public class CategoriaControlador {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> obtenerTodasLasCategorias() {
        List<Categoria> categorias = categoriaService.obtenerTodasLasCategorias();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/paginado")
    public ResponseEntity<Page<Categoria>> obtenerCategoriasConPaginacion(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Categoria> categorias = categoriaService.obtenerCategoriasConPaginacion(pageable);
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerCategoriaPorId(@PathVariable Integer id) {
        Categoria categoria = categoriaService.obtenerCategoriaPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Categoria>> buscarCategorias(
            @RequestParam String nombre) {
        List<Categoria> categorias = categoriaService.buscarCategoriasPorNombre(nombre);
        return ResponseEntity.ok(categorias);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ADMIN_NEGOCIO')")
    public ResponseEntity<Categoria> crearCategoria(
            @RequestBody Categoria categoria,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        categoria.setCreadoPor(userDetails.getUsername());
        Categoria nuevaCategoria = categoriaService.crearCategoria(categoria);
        return ResponseEntity.status(201).body(nuevaCategoria);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ADMIN_NEGOCIO')")
    public ResponseEntity<Categoria> actualizarCategoria(
            @PathVariable Integer id,
            @RequestBody Categoria categoria,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        categoria.setModificadoPor(userDetails.getUsername());
        Categoria categoriaActualizada = categoriaService.actualizarCategoria(id, categoria);
        return ResponseEntity.ok(categoriaActualizada);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ADMIN_NEGOCIO')")
    public ResponseEntity<Void> eliminarCategoria(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        categoriaService.eliminarCategoria(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ADMIN_NEGOCIO')")
    public ResponseEntity<Void> activarCategoria(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        categoriaService.activarCategoria(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/existe")
    public ResponseEntity<Boolean> verificarExistencia(@RequestParam String nombre) {
        boolean existe = categoriaService.existeCategoriaPorNombre(nombre);
        return ResponseEntity.ok(existe);
    }
}
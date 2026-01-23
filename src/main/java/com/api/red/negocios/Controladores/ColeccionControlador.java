package com.api.red.negocios.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.red.negocios.Modelos.Coleccion;
import com.api.red.negocios.Repositorios.ColeccionRepositorio;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/colecciones")
public class ColeccionControlador {

    @Autowired
    private ColeccionRepositorio coleccionRepositorio;

    // Obtener todas las colecciones (sin autenticación requerida)
    @GetMapping
    public List<Coleccion> obtenerTodasLasColecciones() {
        return coleccionRepositorio.findAll();
    }

    // Obtener una colección por ID
    @GetMapping("/{id}")
    public ResponseEntity<Coleccion> obtenerColeccionPorId(@PathVariable Integer id) {
        return coleccionRepositorio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear una nueva colección
    @PostMapping
    public Coleccion crearColeccion(@RequestBody Coleccion coleccion) {
        coleccion.setFechaCreacion(LocalDateTime.now());
        coleccion.setFechaModificacion(LocalDateTime.now());
        coleccion.setActivo(true);
        return coleccionRepositorio.save(coleccion);
    }

    // Actualizar una colección
    @PutMapping("/{id}")
    public ResponseEntity<Coleccion> actualizarColeccion(@PathVariable Integer id, @RequestBody Coleccion coleccionActualizada) {
        return coleccionRepositorio.findById(id).map(coleccion -> {
            coleccion.setTitulo(coleccionActualizada.getTitulo());
            coleccion.setAutor(coleccionActualizada.getAutor());
            coleccion.setDescripcion(coleccionActualizada.getDescripcion());
            coleccion.setIsbn(coleccionActualizada.getIsbn());
            coleccion.setAño(coleccionActualizada.getAño());
            coleccion.setEditorial(coleccionActualizada.getEditorial());
            coleccion.setImagenUrl(coleccionActualizada.getImagenUrl());
            coleccion.setTipoColeccion(coleccionActualizada.getTipoColeccion());
            coleccion.setModificadoPor(coleccionActualizada.getModificadoPor());
            coleccion.setFechaModificacion(LocalDateTime.now());
            coleccion.setActivo(coleccionActualizada.getActivo());
            return ResponseEntity.ok(coleccionRepositorio.save(coleccion));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Eliminar una colección (eliminación lógica)
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarColeccion(@PathVariable Integer id) {
        return coleccionRepositorio.findById(id).map(coleccion -> {
            coleccion.setActivo(false);
            coleccion.setFechaEliminacion(LocalDateTime.now());
            coleccionRepositorio.save(coleccion);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}

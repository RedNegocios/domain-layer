package com.api.red.negocios.Controladores;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.red.negocios.Modelos.Orden;
import com.api.red.negocios.Repositorios.OrdenRepositorio;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenControlador {

    @Autowired
    private OrdenRepositorio ordenRepositorio;

    // Obtener todas las órdenes
    @GetMapping
    public List<Orden> obtenerTodasLasOrdenes() {
        return ordenRepositorio.findAll();
    }

    // Obtener una orden por ID
    @GetMapping("/{id}")
    public ResponseEntity<Orden> obtenerOrdenPorId(@PathVariable Integer id) {
        return ordenRepositorio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear una nueva orden
    @PostMapping
    public Orden crearOrden(@RequestBody Orden orden) {
        return ordenRepositorio.save(orden);
    }

    // Actualizar una orden
    @PutMapping("/{id}")
    public ResponseEntity<Orden> actualizarOrden(@PathVariable Integer id, @RequestBody Orden ordenActualizada) {
        return ordenRepositorio.findById(id).map(orden -> {
            orden.setNegocio(ordenActualizada.getNegocio());
            orden.setNumeroOrden(ordenActualizada.getNumeroOrden());
            orden.setFechaOrden(ordenActualizada.getFechaOrden());
            orden.setMontoTotal(ordenActualizada.getMontoTotal());
            orden.setEstado(ordenActualizada.getEstado());
            orden.setModificadoPor(ordenActualizada.getModificadoPor());
            orden.setFechaModificacion(ordenActualizada.getFechaModificacion());
            return ResponseEntity.ok(ordenRepositorio.save(orden));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Eliminar una orden (lógica de eliminación)
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarOrden(@PathVariable Integer id) {
        return ordenRepositorio.findById(id).map(orden -> {
            orden.setActivo(false);
            orden.setFechaEliminacion(LocalDateTime.now());
            ordenRepositorio.save(orden);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}

	package com.api.red.negocios.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.red.negocios.Modelos.LineaOrden;
import com.api.red.negocios.Repositorios.LineaOrdenRepositorio;

import java.util.List;

@RestController
@RequestMapping("/api/lineas-orden")
public class LineaOrdenControlador {

    @Autowired
    private LineaOrdenRepositorio lineaOrdenRepositorio;

    // Obtener todas las líneas de orden
    @GetMapping
    public List<LineaOrden> obtenerTodasLasLineasOrden() {
        return lineaOrdenRepositorio.findAll();
    }

    // Obtener una línea de orden por ID
    @GetMapping("/{id}")
    public ResponseEntity<LineaOrden> obtenerLineaOrdenPorId(@PathVariable Integer id) {
        return lineaOrdenRepositorio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear una nueva línea de orden
    @PostMapping
    public LineaOrden crearLineaOrden(@RequestBody LineaOrden lineaOrden) {
        return lineaOrdenRepositorio.save(lineaOrden);
    }

    // Actualizar una línea de orden
    @PutMapping("/{id}")
    public ResponseEntity<LineaOrden> actualizarLineaOrden(@PathVariable Integer id, @RequestBody LineaOrden lineaOrdenActualizada) {
        return lineaOrdenRepositorio.findById(id).map(lineaOrden -> {
            lineaOrden.setOrden(lineaOrdenActualizada.getOrden());
            lineaOrden.setNegocioProducto(lineaOrdenActualizada.getNegocioProducto());
            lineaOrden.setCantidad(lineaOrdenActualizada.getCantidad());
            lineaOrden.setPrecioUnitario(lineaOrdenActualizada.getPrecioUnitario());
            return ResponseEntity.ok(lineaOrdenRepositorio.save(lineaOrden));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Eliminar una línea de orden (eliminación física)
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarLineaOrden(@PathVariable Integer id) {
        return lineaOrdenRepositorio.findById(id).map(lineaOrden -> {
            lineaOrdenRepositorio.delete(lineaOrden);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
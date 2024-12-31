package com.api.red.negocios.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.red.negocios.Modelos.NegocioProducto;
import com.api.red.negocios.Repositorios.NegocioProductoRepositorio;

import java.util.List;

@RestController
@RequestMapping("/api/negocios-productos")
public class NegocioProductoControlador {

    @Autowired
    private NegocioProductoRepositorio negocioProductoRepositorio;

    // Obtener todos los registros de negocio-producto
    @GetMapping
    public List<NegocioProducto> obtenerTodos() {
        return negocioProductoRepositorio.findAll();
    }

    // Obtener un registro por ID
    @GetMapping("/{id}")
    public ResponseEntity<NegocioProducto> obtenerPorId(@PathVariable Integer id) {
        return negocioProductoRepositorio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear un nuevo registro de negocio-producto
    @PostMapping
    public NegocioProducto crear(@RequestBody NegocioProducto negocioProducto) {
        return negocioProductoRepositorio.save(negocioProducto);
    }

    // Actualizar un registro de negocio-producto
    @PutMapping("/{id}")
    public ResponseEntity<NegocioProducto> actualizar(@PathVariable Integer id, @RequestBody NegocioProducto negocioProductoActualizado) {
        return negocioProductoRepositorio.findById(id).map(negocioProducto -> {
            negocioProducto.setNegocio(negocioProductoActualizado.getNegocio());
            negocioProducto.setProducto(negocioProductoActualizado.getProducto());
            negocioProducto.setPrecioDeVenta(negocioProductoActualizado.getPrecioDeVenta());
            return ResponseEntity.ok(negocioProductoRepositorio.save(negocioProducto));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un registro de negocio-producto (eliminación física)
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminar(@PathVariable Integer id) {
        return negocioProductoRepositorio.findById(id).map(negocioProducto -> {
            negocioProductoRepositorio.delete(negocioProducto);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}

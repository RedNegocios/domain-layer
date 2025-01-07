package com.api.red.negocios.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.api.red.negocios.Modelos.Producto;
import com.api.red.negocios.Repositorios.ProductoRepositorio;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoControlador {

    @Autowired
    private ProductoRepositorio productoRepositorio;

    // Obtener todos los productos
    @GetMapping
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepositorio.findAll();
    }

    // Obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Integer id) {
        return productoRepositorio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear un nuevo producto
    @PostMapping
    public Producto crearProducto(@RequestBody Producto producto) {
    	producto.setFechaCreacion(LocalDateTime.now());
        producto.setActivo(true);
        return productoRepositorio.save(producto);
    }


    // Actualizar un producto
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Integer id, @RequestBody Producto productoActualizado) {
        return productoRepositorio.findById(id).map(producto -> {
            producto.setNombre(productoActualizado.getNombre());
            producto.setDescripcion(productoActualizado.getDescripcion());
            producto.setPrecio(productoActualizado.getPrecio());
            producto.setModificadoPor(productoActualizado.getModificadoPor());
            producto.setFechaModificacion(productoActualizado.getFechaModificacion());
            producto.setActivo(productoActualizado.getActivo());
            producto.setEliminadoPor(productoActualizado.getEliminadoPor());
            producto.setFechaEliminacion(productoActualizado.getFechaEliminacion());
            return ResponseEntity.ok(productoRepositorio.save(producto));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un producto (lógica de eliminación)
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarProducto(@PathVariable Integer id) {
        return productoRepositorio.findById(id).map(producto -> {
            producto.setActivo(false);
            producto.setFechaEliminacion(LocalDateTime.now());
            productoRepositorio.save(producto);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}


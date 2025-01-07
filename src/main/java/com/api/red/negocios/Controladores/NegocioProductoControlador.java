package com.api.red.negocios.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.api.red.negocios.Modelos.Negocio;
import com.api.red.negocios.Modelos.NegocioProducto;
import com.api.red.negocios.Modelos.Producto;
import com.api.red.negocios.Modelos.Usuario;
import com.api.red.negocios.Modelos.UsuarioNegocio;
import com.api.red.negocios.Repositorios.NegocioProductoRepositorio;
import com.api.red.negocios.Repositorios.NegocioRepositorio;
import com.api.red.negocios.Repositorios.ProductoRepositorio;
import com.api.red.negocios.Repositorios.UsuarioNegocioRepositorio;
import com.api.red.negocios.Repositorios.UsuarioRepositorio;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/negocios-productos")
public class NegocioProductoControlador {

	@Autowired
    private NegocioProductoRepositorio negocioProductoRepositorio;
	@Autowired
    private UsuarioNegocioRepositorio usuarioNegocioRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private NegocioRepositorio negocioRepositorio;
    @Autowired
    private ProductoRepositorio productoRepositorio;

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
    @PreAuthorize("hasAuthority('ROLE_ADMIN_NEGOCIO')")
    public NegocioProducto crear(@RequestBody NegocioProducto negocioProducto) {
        // Validar que el negocio y producto no sean nulos
        if (negocioProducto.getNegocio() == null || negocioProducto.getProducto() == null) {
            throw new IllegalArgumentException("Negocio y Producto no pueden ser nulos");
        }

        // Buscar el negocio por ID
        Negocio negocio = negocioRepositorio.findById(negocioProducto.getNegocio().getNegocioId())
                .orElseThrow(() -> new IllegalArgumentException("Negocio no encontrado"));

        // Buscar el producto por ID
        Producto producto = productoRepositorio.findById(negocioProducto.getProducto().getProductoId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        // Asociar el negocio y producto al nuevo registro
        negocioProducto.setNegocio(negocio);
        negocioProducto.setProducto(producto);
        negocioProducto.setPrecioDeVenta(negocioProducto.getPrecioDeVenta());

        return negocioProductoRepositorio.save(negocioProducto);
    }
    
    @GetMapping("/productos-por-negocio/{negocioId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_NEGOCIO')")
    public ResponseEntity<List<NegocioProducto>> obtenerProductosPorNegocio(@PathVariable Integer negocioId) {
        Optional<Negocio> negocio = negocioRepositorio.findById(negocioId);
        if (negocio.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Filtrar productos asociados al negocio específico
        List<NegocioProducto> productos = negocioProductoRepositorio.findByNegocio(negocio.get());
        return ResponseEntity.ok(productos);
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

package com.api.red.negocios.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.api.red.negocios.Modelos.Negocio;
import com.api.red.negocios.Modelos.NegocioProducto;
import com.api.red.negocios.Modelos.Usuario;
import com.api.red.negocios.Modelos.UsuarioNegocio;
import com.api.red.negocios.Repositorios.NegocioProductoRepositorio;
import com.api.red.negocios.Repositorios.NegocioRepositorio;
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
        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Obtener el usuario
        Usuario usuario = usuarioRepositorio.findByUsername(username);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        // Verificar si el usuario está asociado a un negocio
        List<UsuarioNegocio> usuarioNegocios = usuarioNegocioRepositorio.findByUsuario(usuario);
        if (usuarioNegocios.isEmpty()) {
            throw new IllegalArgumentException("El usuario no está asociado a ningún negocio");
        }

        // Asociar el primer negocio del usuario (puedes ajustar según la lógica)
        negocioProducto.setNegocio(usuarioNegocios.get(0).getNegocio());

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

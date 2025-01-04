package com.api.red.negocios.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.api.red.negocios.Modelos.Negocio;
import com.api.red.negocios.Modelos.Usuario;
import com.api.red.negocios.Modelos.UsuarioNegocio;
import com.api.red.negocios.Repositorios.NegocioRepositorio;
import com.api.red.negocios.Repositorios.UsuarioNegocioRepositorio;
import com.api.red.negocios.Repositorios.UsuarioRepositorio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuario-negocio")
@PreAuthorize("hasAuthority('ROLE_ADMIN_NEGOCIO') or hasAuthority('ROLE_USER')")
public class UsuarioNegocioControlador {

    @Autowired
    private UsuarioNegocioRepositorio repository;
    
    @Autowired
    private NegocioRepositorio negocioRepositorio;
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @GetMapping
    public List<UsuarioNegocio> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioNegocio> getById(@PathVariable Integer id) {
        Optional<UsuarioNegocio> usuarioNegocio = repository.findById(id);
        return usuarioNegocio.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public UsuarioNegocio create(@RequestBody UsuarioNegocio usuarioNegocio) {
        try {
            // Verificar que el negocio no sea nulo
            if (usuarioNegocio.getNegocio() == null || usuarioNegocio.getNegocio().getNegocioId() == null) {
                throw new IllegalArgumentException("El negocio no puede ser nulo");
            }

            // Buscar negocio en el repositorio
            Negocio negocio = negocioRepositorio.findById(usuarioNegocio.getNegocio().getNegocioId())
                    .orElseThrow(() -> new IllegalArgumentException("Negocio no encontrado"));

            // Obtener usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new IllegalArgumentException("Usuario no autenticado");
            }

            String username = authentication.getName();

            Usuario usuario = usuarioRepositorio.findByUsername(username);
            if (usuario == null) {
                throw new IllegalArgumentException("Usuario no encontrado");
            }

            // Asignar usuario y negocio a la entidad UsuarioNegocio
            usuarioNegocio.setUsuario(usuario);
            usuarioNegocio.setNegocio(negocio);
            usuarioNegocio.setFechaCreacion(LocalDateTime.now());
            usuarioNegocio.setEstatusId(1);

            // Guardar la entidad y devolverla
            return repository.save(usuarioNegocio);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioNegocio> update(@PathVariable Integer id, @RequestBody UsuarioNegocio usuarioNegocio) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioNegocio.setUsuarioNegocioId(id);
        usuarioNegocio.setFechaModificacion(LocalDateTime.now());
        return ResponseEntity.ok(repository.save(usuarioNegocio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Optional<UsuarioNegocio> usuarioNegocio = repository.findById(id);
        if (usuarioNegocio.isPresent()) {
            UsuarioNegocio entity = usuarioNegocio.get();
            entity.setActivo(false);
            entity.setFechaEliminacion(LocalDateTime.now());
            entity.setEliminadoPor("admin"); // Cambia según el usuario real
            repository.save(entity);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}

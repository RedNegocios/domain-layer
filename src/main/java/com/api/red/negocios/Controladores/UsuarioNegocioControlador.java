package com.api.red.negocios.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.api.red.negocios.Modelos.UsuarioNegocio;
import com.api.red.negocios.Repositorios.UsuarioNegocioRepositorio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuario-negocio")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class UsuarioNegocioControlador {

    @Autowired
    private UsuarioNegocioRepositorio repository;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
        usuarioNegocio.setFechaCreacion(LocalDateTime.now());
        return repository.save(usuarioNegocio);
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
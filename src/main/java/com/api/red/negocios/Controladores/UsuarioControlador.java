package com.api.red.negocios.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.api.red.negocios.Modelos.Usuario;
import com.api.red.negocios.Servicios.UsuarioServicio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioService;

    @GetMapping
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Usuario> getAllUsuarios() {
        return usuarioService.findAllUsuarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Integer id) {
        Optional<Usuario> usuario = usuarioService.findUsuarioById(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setActivo(true);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.saveUsuario(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Integer id, @RequestBody Usuario usuarioDetails) {
        Optional<Usuario> optionalUsuario = usuarioService.findUsuarioById(id);

        if (optionalUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Usuario usuario = optionalUsuario.get();
        usuario.setUsername(usuarioDetails.getUsername());
        usuario.setEmail(usuarioDetails.getEmail());
        usuario.setPassword(usuarioDetails.getPassword());
        usuario.setHabilitado(usuarioDetails.getHabilitado());
        usuario.setCuentaNoExpirada(usuarioDetails.getCuentaNoExpirada());
        usuario.setCredencialesNoExpiradas(usuarioDetails.getCredencialesNoExpiradas());
        usuario.setCuentaNoBloqueada(usuarioDetails.getCuentaNoBloqueada());
        usuario.setModificadoPor(usuarioDetails.getModificadoPor());
        usuario.setFechaModificacion(LocalDateTime.now());

        return ResponseEntity.ok(usuarioService.saveUsuario(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id, @RequestParam(required = false) String eliminadoPor) {
        Optional<Usuario> optionalUsuario = usuarioService.findUsuarioById(id);

        if (optionalUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Usuario usuario = optionalUsuario.get();
        usuario.setActivo(false);
        usuario.setEliminadoPor(eliminadoPor);
        usuario.setFechaEliminacion(LocalDateTime.now());

        usuarioService.saveUsuario(usuario);
        return ResponseEntity.noContent().build();
    }
}

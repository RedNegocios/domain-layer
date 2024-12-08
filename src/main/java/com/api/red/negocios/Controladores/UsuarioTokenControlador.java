package com.api.red.negocios.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.red.negocios.Modelos.UsuarioToken;
import com.api.red.negocios.Repositorios.UsuarioTokenRepositorio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuario-tokens")
public class UsuarioTokenControlador {

    @Autowired
    private UsuarioTokenRepositorio usuarioTokenRepositorio;

    @GetMapping
    public List<UsuarioToken> getAllUsuarioTokens() {
        return usuarioTokenRepositorio.findAll();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<UsuarioToken> getTokensByUsuarioId(@PathVariable Integer usuarioId) {
        return usuarioTokenRepositorio.findByUsuarioUsuarioId(usuarioId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioToken> getUsuarioTokenById(@PathVariable Integer id) {
        Optional<UsuarioToken> usuarioToken = usuarioTokenRepositorio.findById(id);
        return usuarioToken.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<UsuarioToken> createUsuarioToken(@RequestBody UsuarioToken usuarioToken) {
        usuarioToken.setFechaCreacion(LocalDateTime.now());
        usuarioToken.setActivo(true);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioTokenRepositorio.save(usuarioToken));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioToken> updateUsuarioToken(@PathVariable Integer id, @RequestBody UsuarioToken usuarioTokenDetails) {
        Optional<UsuarioToken> optionalUsuarioToken = usuarioTokenRepositorio.findById(id);

        if (optionalUsuarioToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        UsuarioToken usuarioToken = optionalUsuarioToken.get();
        usuarioToken.setToken(usuarioTokenDetails.getToken());
        usuarioToken.setFechaExpiracion(usuarioTokenDetails.getFechaExpiracion());
        usuarioToken.setHabilitado(usuarioTokenDetails.getHabilitado());
        usuarioToken.setModificadoPor(usuarioTokenDetails.getModificadoPor());
        usuarioToken.setFechaModificacion(LocalDateTime.now());

        return ResponseEntity.ok(usuarioTokenRepositorio.save(usuarioToken));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuarioToken(@PathVariable Integer id, @RequestParam(required = false) String eliminadoPor) {
        Optional<UsuarioToken> optionalUsuarioToken = usuarioTokenRepositorio.findById(id);

        if (optionalUsuarioToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        UsuarioToken usuarioToken = optionalUsuarioToken.get();
        usuarioToken.setActivo(false);
        usuarioToken.setEliminadoPor(eliminadoPor);
        usuarioToken.setFechaEliminacion(LocalDateTime.now());

        usuarioTokenRepositorio.save(usuarioToken);
        return ResponseEntity.noContent().build();
    }
}

package com.api.red.negocios.Controladores;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.red.negocios.Modelos.Autoridad;
import com.api.red.negocios.Repositorios.AutoridadRepositorio;

@RestController
@RequestMapping("/api/autoridades")
public class AutoridadControlador {

    @Autowired
    private AutoridadRepositorio autoridadRepository;

    @GetMapping
    public List<Autoridad> getAllAutoridades() {
        return autoridadRepository.findAll();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Autoridad> getAutoridadesByUsuarioId(@PathVariable Integer usuarioId) {
        return autoridadRepository.findByUsuarioUsuarioId(usuarioId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Autoridad> getAutoridadById(@PathVariable Integer id) {
        Optional<Autoridad> autoridad = autoridadRepository.findById(id);
        return autoridad.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Autoridad> createAutoridad(@RequestBody Autoridad autoridad) {
        autoridad.setFechaCreacion(LocalDateTime.now());
        autoridad.setActivo(true);
        return ResponseEntity.status(HttpStatus.CREATED).body(autoridadRepository.save(autoridad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Autoridad> updateAutoridad(@PathVariable Integer id, @RequestBody Autoridad autoridadDetails) {
        Optional<Autoridad> optionalAutoridad = autoridadRepository.findById(id);

        if (optionalAutoridad.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Autoridad autoridad = optionalAutoridad.get();
        autoridad.setAutoridad(autoridadDetails.getAutoridad());
        autoridad.setModificadoPor(autoridadDetails.getModificadoPor());
        autoridad.setFechaModificacion(LocalDateTime.now());

        return ResponseEntity.ok(autoridadRepository.save(autoridad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAutoridad(@PathVariable Integer id, @RequestParam(required = false) String eliminadoPor) {
        Optional<Autoridad> optionalAutoridad = autoridadRepository.findById(id);

        if (optionalAutoridad.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Autoridad autoridad = optionalAutoridad.get();
        autoridad.setActivo(false);
        autoridad.setEliminadoPor(eliminadoPor);
        autoridad.setFechaEliminacion(LocalDateTime.now());

        autoridadRepository.save(autoridad);
        return ResponseEntity.noContent().build();
    }
}

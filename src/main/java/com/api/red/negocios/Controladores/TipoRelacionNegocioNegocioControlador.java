package com.api.red.negocios.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.red.negocios.Modelos.TipoRelacionNegocioNegocio;
import com.api.red.negocios.Repositorios.TipoRelacionNegocioNegocioRepositorio;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tipos-relacion")
public class TipoRelacionNegocioNegocioControlador {

    @Autowired
    private TipoRelacionNegocioNegocioRepositorio tipoRelacionRepositorio;

    // Obtener todos los tipos de relación
    @GetMapping
    public ResponseEntity<List<TipoRelacionNegocioNegocio>> getAllTiposRelacion() {
        List<TipoRelacionNegocioNegocio> tiposRelacion = tipoRelacionRepositorio.findAll();
        return ResponseEntity.ok(tiposRelacion);
    }

    // Obtener un tipo de relación por su ID
    @GetMapping("/{id}")
    public ResponseEntity<TipoRelacionNegocioNegocio> getTipoRelacionById(@PathVariable Integer id) {
        Optional<TipoRelacionNegocioNegocio> tipoRelacion = tipoRelacionRepositorio.findById(id);
        return tipoRelacion.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Crear un nuevo tipo de relación
    @PostMapping
    public ResponseEntity<TipoRelacionNegocioNegocio> createTipoRelacion(@RequestBody TipoRelacionNegocioNegocio tipoRelacion) {
        TipoRelacionNegocioNegocio nuevoTipoRelacion = tipoRelacionRepositorio.save(tipoRelacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTipoRelacion);
    }

    // Actualizar un tipo de relación existente
    @PutMapping("/{id}")
    public ResponseEntity<TipoRelacionNegocioNegocio> updateTipoRelacion(
            @PathVariable Integer id,
            @RequestBody TipoRelacionNegocioNegocio tipoRelacionDetails) {

        Optional<TipoRelacionNegocioNegocio> optionalTipoRelacion = tipoRelacionRepositorio.findById(id);

        if (optionalTipoRelacion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        TipoRelacionNegocioNegocio tipoRelacion = optionalTipoRelacion.get();
        tipoRelacion.setDescripcion(tipoRelacionDetails.getDescripcion());

        return ResponseEntity.ok(tipoRelacionRepositorio.save(tipoRelacion));
    }

    // Eliminar un tipo de relación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoRelacion(@PathVariable Integer id) {
        if (!tipoRelacionRepositorio.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        tipoRelacionRepositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}


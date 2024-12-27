package com.api.red.negocios.Controladores;

import com.api.red.negocios.Modelos.NegocioNegocio;
import com.api.red.negocios.Repositorios.NegocioNegocioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/negocioNegocio")
public class NegocioNegocioControlador {

    private final NegocioNegocioRepositorio negocioNegocioRepositorio;

    @Autowired
    public NegocioNegocioControlador(NegocioNegocioRepositorio negocioNegocioRepositorio) {
        this.negocioNegocioRepositorio = negocioNegocioRepositorio;
    }

    @GetMapping("/{id}")
    public ResponseEntity<NegocioNegocio> getNegocioNegocioById(@PathVariable Integer id) {
        return negocioNegocioRepositorio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<NegocioNegocio>> getAllNegocioNegocio() {
        return ResponseEntity.ok(negocioNegocioRepositorio.findAll());
    }

    @PostMapping
    public ResponseEntity<NegocioNegocio> createNegocioNegocio(@RequestBody NegocioNegocio NegocioNegocio) {
        return ResponseEntity.ok(negocioNegocioRepositorio.save(NegocioNegocio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NegocioNegocio> updateNegocioNegocio(@PathVariable Integer id, @RequestBody NegocioNegocio NegocioNegocio) {
        return negocioNegocioRepositorio.findById(id)
                .map(existing -> {
                    NegocioNegocio.setNegocioNegocioId(existing.getNegocioNegocioId());
                    return ResponseEntity.ok(negocioNegocioRepositorio.save(NegocioNegocio));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteNegocioNegocio(@PathVariable Integer id) {
        return negocioNegocioRepositorio.findById(id)
                .map(existing -> {
                    negocioNegocioRepositorio.delete(existing);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}


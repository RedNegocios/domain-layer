package com.api.red.negocios.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.red.negocios.Modelos.Beneficio;
import com.api.red.negocios.Repositorios.BeneficioRepositorio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/beneficios")
public class BeneficioControlador {

    @Autowired
    private BeneficioRepositorio beneficioRepositorio;

    // Obtener todos los beneficios
    @GetMapping
    public ResponseEntity<List<Beneficio>> getAllBeneficios() {
        List<Beneficio> beneficios = beneficioRepositorio.findAll();
        return ResponseEntity.ok(beneficios);
    }

    // Obtener beneficios por negocioNegocioId
    @GetMapping("/negocio/{negocioNegocioId}")
    public ResponseEntity<List<Beneficio>> getBeneficiosByNegocioId(@PathVariable Integer negocioNegocioId) {
        List<Beneficio> beneficios = beneficioRepositorio.findByNegocioNegocioId(negocioNegocioId);
        return ResponseEntity.ok(beneficios);
    }

    // Obtener un beneficio por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Beneficio> getBeneficioById(@PathVariable Integer id) {
        Optional<Beneficio> beneficio = beneficioRepositorio.findById(id);
        return beneficio.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Crear un nuevo beneficio
    @PostMapping
    public ResponseEntity<Beneficio> createBeneficio(@RequestBody Beneficio beneficio) {
        beneficio.setFechaCreacion(LocalDateTime.now());
        beneficio.setActivo(true);
        Beneficio nuevoBeneficio = beneficioRepositorio.save(beneficio);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoBeneficio);
    }

    // Actualizar un beneficio existente
    @PutMapping("/{id}")
    public ResponseEntity<Beneficio> updateBeneficio(@PathVariable Integer id, @RequestBody Beneficio beneficioDetails) {
        Optional<Beneficio> optionalBeneficio = beneficioRepositorio.findById(id);

        if (optionalBeneficio.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Beneficio beneficio = optionalBeneficio.get();
        beneficio.setDescripcion(beneficioDetails.getDescripcion());
        beneficio.setValor(beneficioDetails.getValor());
        beneficio.setVisibleSoloAdmin(beneficioDetails.getVisibleSoloAdmin());
        beneficio.setTipoBeneficio(beneficioDetails.getTipoBeneficio());
        beneficio.setCondiciones(beneficioDetails.getCondiciones());
        beneficio.setModificadoPor(beneficioDetails.getModificadoPor());
        beneficio.setFechaModificacion(LocalDateTime.now());

        return ResponseEntity.ok(beneficioRepositorio.save(beneficio));
    }

    // Eliminar lógicamente un beneficio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBeneficio(@PathVariable Integer id, @RequestParam String eliminadoPor) {
        Optional<Beneficio> optionalBeneficio = beneficioRepositorio.findById(id);

        if (optionalBeneficio.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Beneficio beneficio = optionalBeneficio.get();
        beneficio.setActivo(false);
        beneficio.setEliminadoPor(eliminadoPor);
        beneficio.setFechaEliminacion(LocalDateTime.now());

        beneficioRepositorio.save(beneficio);
        return ResponseEntity.noContent().build();
    }
}


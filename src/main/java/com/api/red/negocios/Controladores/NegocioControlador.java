package com.api.red.negocios.Controladores;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.api.red.negocios.Modelos.Negocio;
import com.api.red.negocios.Repositorios.NegocioRepositorio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/negocios")
public class NegocioControlador {
	private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger();
    @Autowired
    private NegocioRepositorio negocioRepositorio;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<Negocio> getAllNegocios() {
    	logger.info("al menos se ejecuta el metodo loledkorfifoiuerhbfgl;iserbg");
    	// Obtener el objeto de autenticación
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // Loggear los roles del usuario
            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            logger.info("Roles del usuario autenticado: {}", roles);
        
        }
        return negocioRepositorio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Negocio> getNegocioById(@PathVariable Integer id) {
        Optional<Negocio> negocio = negocioRepositorio.findById(id);
        return negocio.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Negocio> createNegocio(@RequestBody Negocio negocio) {
        negocio.setFechaCreacion(LocalDateTime.now());
        negocio.setActivo(true);
        return ResponseEntity.status(HttpStatus.CREATED).body(negocioRepositorio.save(negocio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Negocio> updateNegocio(@PathVariable Integer id, @RequestBody Negocio negocioDetails) {
        Optional<Negocio> optionalNegocio = negocioRepositorio.findById(id);

        if (optionalNegocio.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Negocio negocio = optionalNegocio.get();
        negocio.setNombre(negocioDetails.getNombre());
        negocio.setDescripcion(negocioDetails.getDescripcion());
        negocio.setModificadoPor(negocioDetails.getModificadoPor());
        negocio.setFechaModificacion(LocalDateTime.now());

        return ResponseEntity.ok(negocioRepositorio.save(negocio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNegocio(@PathVariable Integer id, @RequestParam String eliminadoPor) {
        Optional<Negocio> optionalNegocio = negocioRepositorio.findById(id);

        if (optionalNegocio.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Negocio negocio = optionalNegocio.get();
        negocio.setActivo(false);
        negocio.setEliminadoPor(eliminadoPor);
        negocio.setFechaEliminacion(LocalDateTime.now());

        negocioRepositorio.save(negocio);
        return ResponseEntity.noContent().build();
    }
}

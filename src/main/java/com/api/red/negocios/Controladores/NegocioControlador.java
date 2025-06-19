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

import com.api.red.negocios.DTO.NegocioUsuariosDTO;
import com.api.red.negocios.DTO.UsuarioDTO;
import com.api.red.negocios.Modelos.Negocio;
import com.api.red.negocios.Modelos.Usuario;
import com.api.red.negocios.Modelos.UsuarioNegocio;
import com.api.red.negocios.Repositorios.NegocioRepositorio;
import com.api.red.negocios.Repositorios.UsuarioNegocioRepositorio;
import com.api.red.negocios.Repositorios.UsuarioRepositorio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/negocios")
public class NegocioControlador {
	private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger();
    @Autowired
    private NegocioRepositorio negocioRepositorio;

    @Autowired
    private UsuarioNegocioRepositorio usuarioNegocioRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN_NEGOCIO')")
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
    
 // Nuevo endpoint para admin negocio agregar mas logica porque debe de traer de cada uno debe eligir cual manejar y debe aceptar o rechazar las solicitudes  more todo
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_NEGOCIO')")
    public List<NegocioUsuariosDTO> getNegociosConUsuariosPendientes() {
        logger.info("Método getNegociosConUsuariosPendientes ejecutado por ROLE_ADMIN_NEGOCIO");

        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.error("Usuario no autenticado");
            throw new IllegalArgumentException("Usuario no autenticado");
        }

        String username = authentication.getName();
        Usuario usuario = usuarioRepositorio.findByUsername(username);
        if (usuario == null) {
            logger.error("Usuario no encontrado: {}", username);
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        // Obtener los registros de UsuarioNegocio asociados al usuario
        List<UsuarioNegocio> usuarioNegocios = usuario.getUsuarioNegocios();
        logger.info("UsuarioNegocios para el usuario {}: {}", username, usuarioNegocios);

        // Extraer los negocios asociados
        List<Negocio> negociosAsociados = usuarioNegocios.stream()
                .map(UsuarioNegocio::getNegocio)
                .distinct()
                .collect(Collectors.toList());
        logger.info("Negocios asociados al usuario {}: {}", username, negociosAsociados);

        // Crear una lista para agrupar la respuesta
        List<NegocioUsuariosDTO> resultado = new ArrayList<>();

        for (Negocio negocio : negociosAsociados) {
        	logger.info("Negocios asociados {}", negociosAsociados);
            // Obtener los registros pendientes (estatusId = 1) para cada negocio
            List<UsuarioNegocio> pendientes = usuarioNegocioRepositorio.findByNegocioAndEstatusId(negocio, 1);

            // Transformar a DTO de usuarios
            List<UsuarioDTO> usuariosDTO = pendientes.stream()
                    .map(usuarioNegocio -> new UsuarioDTO(
                            usuarioNegocio.getUsuario().getUsername(),
                            usuarioNegocio.getUsuario().getEmail(),
                            usuarioNegocio.getUsuarioNegocioId()
                    ))
                    .distinct()
                    .collect(Collectors.toList());

            // Agregar solo si hay usuarios pendientes
            if (!usuariosDTO.isEmpty()) {
                resultado.add(new NegocioUsuariosDTO(negocio.getNombre(), usuariosDTO));
            }
        }

        logger.info("Negocios con usuarios pendientes: {}", resultado);
        return resultado;
    }

    @GetMapping("/admin-negocios")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_NEGOCIO')")
    public List<Negocio> getNegociosDeAdmin() {
        logger.info("Método getNegociosConUsuariosPendientes ejecutado por ROLE_ADMIN_NEGOCIO");

        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.error("Usuario no autenticado");
            throw new IllegalArgumentException("Usuario no autenticado");
        }

        String username = authentication.getName();
        Usuario usuario = usuarioRepositorio.findByUsername(username);
        if (usuario == null) {
            logger.error("Usuario no encontrado: {}", username);
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        // Obtener los registros de UsuarioNegocio asociados al usuario
        List<UsuarioNegocio> usuarioNegocios = usuario.getUsuarioNegocios();
        logger.info("UsuarioNegocios para el usuario {}: {}", username, usuarioNegocios);

        // Extraer los negocios asociados
        List<Negocio> negociosAsociados = usuarioNegocios.stream()
                .map(UsuarioNegocio::getNegocio)
                .distinct()
                .collect(Collectors.toList());
        logger.info("Negocios asociados al usuario {}: {}", username, negociosAsociados);

        
        return negociosAsociados;
    }

}

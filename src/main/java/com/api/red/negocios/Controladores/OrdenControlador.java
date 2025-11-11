package com.api.red.negocios.Controladores;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.api.red.negocios.DTO.LineaOrdenDTO;
import com.api.red.negocios.DTO.OrdenDTO;
import com.api.red.negocios.Modelos.LineaOrden;
import com.api.red.negocios.Modelos.Negocio;
import com.api.red.negocios.Modelos.Orden;
import com.api.red.negocios.Modelos.Usuario;
import com.api.red.negocios.Repositorios.LineaOrdenRepositorio;
import com.api.red.negocios.Repositorios.NegocioRepositorio;
import com.api.red.negocios.Repositorios.OrdenRepositorio;
import com.api.red.negocios.Repositorios.UsuarioRepositorio;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenControlador {

	@Autowired
    private OrdenRepositorio ordenRepositorio;

    @Autowired
    private LineaOrdenRepositorio lineaOrdenRepositorio;

    @Autowired
    private NegocioRepositorio negocioRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    // Obtener todas las órdenes
    @GetMapping
    public List<Orden> obtenerTodasLasOrdenes() {
        return ordenRepositorio.findAll();
    }

    @GetMapping("/mis-ordenes")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN_NEGOCIO')")
    public ResponseEntity<Map<String, Object>> obtenerOrdenesDelUsuarioAutenticado(
            @PageableDefault(size = 10, sort = "fechaOrden", direction = Sort.Direction.DESC)
            Pageable pageable) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Usuario usuario = usuarioRepositorio.findByUsername(auth.getName());
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Page<Orden> paged = ordenRepositorio.findByUsuario(usuario, pageable);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("content",       paged.getContent());
        response.put("page",          paged.getNumber());
        response.put("size",          paged.getSize());
        response.put("totalPages",    paged.getTotalPages());
        response.put("totalElements", paged.getTotalElements());
        response.put("last",          paged.isLast());

        return ResponseEntity.ok(response);
    }


    
    // Obtener una orden por ID
    @GetMapping("/{id}")
    public ResponseEntity<Orden> obtenerOrdenPorId(@PathVariable Integer id) {
        return ordenRepositorio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear una nueva orden
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN_NEGOCIO')")
    public ResponseEntity<Orden> crearOrden(@RequestBody OrdenDTO ordenDTO) {
        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("Usuario no autenticado");
        }

        String username = authentication.getName();
        Usuario usuario = usuarioRepositorio.findByUsername(username);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        // Buscar el negocio asociado
        Negocio negocio = negocioRepositorio.findById(ordenDTO.getNegocioId())
                .orElseThrow(() -> new IllegalArgumentException("Negocio no encontrado"));

        // Crear la nueva orden
        Orden nuevaOrden = new Orden();
        nuevaOrden.setNegocio(negocio);
        nuevaOrden.setUsuario(usuario);
        nuevaOrden.setNumeroOrden(UUID.randomUUID().toString());
        nuevaOrden.setFechaOrden(LocalDateTime.now());
        nuevaOrden.setMontoTotal(ordenDTO.getMontoTotal());
        nuevaOrden.setEstado("Pendiente");

        Orden ordenGuardada = ordenRepositorio.save(nuevaOrden);

        // Guardar las líneas de la orden con campos de gobernanza
        for (LineaOrdenDTO linea : ordenDTO.getLineasOrden()) {
            LineaOrden nuevaLinea = new LineaOrden();
            nuevaLinea.setOrden(ordenGuardada);
            nuevaLinea.setNegocioProducto(linea.getNegocioProducto());
            nuevaLinea.setCantidad(linea.getCantidad());
            nuevaLinea.setPrecioUnitario(linea.getPrecioUnitario());

            lineaOrdenRepositorio.save(nuevaLinea);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(ordenGuardada);
    }



    // Actualizar una orden
    @PutMapping("/{id}")
    public ResponseEntity<Orden> actualizarOrden(@PathVariable Integer id, @RequestBody Orden ordenActualizada) {
        return ordenRepositorio.findById(id).map(orden -> {
            orden.setNegocio(ordenActualizada.getNegocio());
            orden.setNumeroOrden(ordenActualizada.getNumeroOrden());
            orden.setFechaOrden(ordenActualizada.getFechaOrden());
            orden.setMontoTotal(ordenActualizada.getMontoTotal());
            orden.setEstado(ordenActualizada.getEstado());
            orden.setModificadoPor(ordenActualizada.getModificadoPor());
            orden.setFechaModificacion(ordenActualizada.getFechaModificacion());
            return ResponseEntity.ok(ordenRepositorio.save(orden));
        }).orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/actualizar-estado/{ordenId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_NEGOCIO')")
    public ResponseEntity<Orden> actualizarEstadoOrden(
            @PathVariable Integer ordenId,
            @RequestParam String nuevoEstado) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        Usuario usuario = usuarioRepositorio.findByUsername(username);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<Orden> ordenOptional = ordenRepositorio.findById(ordenId);
        if (ordenOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Orden orden = ordenOptional.get();

        // Validar que el negocio pertenece al admin
        if (orden.getNegocio().getUsuarioNegocios().stream()
                .noneMatch(un -> un.getUsuario().equals(usuario))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        orden.setEstado(nuevoEstado);
        orden.setFechaModificacion(LocalDateTime.now());

        return ResponseEntity.ok(ordenRepositorio.save(orden));
    }


    // Eliminar una orden (lógica de eliminación)
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarOrden(@PathVariable Integer id) {
        return ordenRepositorio.findById(id).map(orden -> {
            orden.setActivo(false);
            orden.setFechaEliminacion(LocalDateTime.now());
            ordenRepositorio.save(orden);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/por-negocio/{negocioId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_NEGOCIO')")
    public ResponseEntity<List<Orden>> obtenerOrdenesPorNegocio(@PathVariable Integer negocioId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        Usuario usuario = usuarioRepositorio.findByUsername(username);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<Negocio> negocioOptional = negocioRepositorio.findById(negocioId);
        if (negocioOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Negocio negocio = negocioOptional.get();

        // Validar que el negocio pertenece al admin
        if (negocio.getUsuarioNegocios().stream()
                .noneMatch(un -> un.getUsuario().equals(usuario))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Orden> ordenes = ordenRepositorio.findByNegocio(negocio);
        return ResponseEntity.ok(ordenes);
    }
    
 // In OrdenControlador.java
    @GetMapping("/historico/{negocioId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_NEGOCIO')")
    public ResponseEntity<Map<String, Object>> obtenerHistoricoPorNegocio(
            @PathVariable Integer negocioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Negocio negocio = negocioRepositorio.findById(negocioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Page<Orden> paged = ordenRepositorio.findByNegocio(
                negocio,
                PageRequest.of(page, size, Sort.by("fechaOrden").descending())
        );

        Map<String, Object> response = new LinkedHashMap();
        response.put("content",       paged.getContent());
        response.put("page",          paged.getNumber());
        response.put("size",          paged.getSize());
        response.put("totalPages",    paged.getTotalPages());
        response.put("totalElements", paged.getTotalElements());
        response.put("last",          paged.isLast());

        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/detalle/{ordenId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_NEGOCIO')")
    public ResponseEntity<List<LineaOrden>> obtenerDetalleOrden(@PathVariable Integer ordenId) {
        Optional<Orden> orden = ordenRepositorio.findById(ordenId);
        if (orden.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<LineaOrden> lineasOrden = lineaOrdenRepositorio.findByOrden(orden.get());
        return ResponseEntity.ok(lineasOrden);
    }



}

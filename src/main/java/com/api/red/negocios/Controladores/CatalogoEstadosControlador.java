package com.api.red.negocios.Controladores;

import com.api.red.negocios.Modelos.CatalogoEstados;
import com.api.red.negocios.Servicios.CatalogoEstadosServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/catalogo-estados")
@CrossOrigin(origins = "*")
public class CatalogoEstadosControlador {

    @Autowired
    private CatalogoEstadosServicio catalogoEstadosServicio;

    // ============= ENDPOINTS BÁSICOS CRUD =============

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ADMIN_NEGOCIO')")
    public ResponseEntity<List<CatalogoEstados>> obtenerTodos() {
        List<CatalogoEstados> estados = catalogoEstadosServicio.obtenerTodos();
        return ResponseEntity.ok(estados);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('ADMIN_NEGOCIO')")
    public ResponseEntity<CatalogoEstados> obtenerPorId(@PathVariable Integer id) {
        Optional<CatalogoEstados> estado = catalogoEstadosServicio.obtenerPorId(id);
        return estado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CatalogoEstados> crear(@RequestBody CatalogoEstados catalogoEstados) {
        try {
            CatalogoEstados nuevoEstado = catalogoEstadosServicio.crear(catalogoEstados, "sistema");
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEstado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CatalogoEstados> actualizar(@PathVariable Integer id, @RequestBody CatalogoEstados catalogoEstados) {
        try {
            CatalogoEstados estadoActualizado = catalogoEstadosServicio.actualizar(id, catalogoEstados, "sistema");
            return ResponseEntity.ok(estadoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        boolean eliminado = catalogoEstadosServicio.eliminar(id, "sistema");
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ============= ENDPOINTS POR ENTIDAD =============

    @GetMapping("/entidad/{entidad}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('ADMIN_NEGOCIO')")
    public ResponseEntity<List<CatalogoEstados>> obtenerPorEntidad(@PathVariable String entidad) {
        List<CatalogoEstados> estados = catalogoEstadosServicio.obtenerPorEntidad(entidad);
        return ResponseEntity.ok(estados);
    }

    @GetMapping("/entidad/{entidad}/paginado")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ADMIN_NEGOCIO')")
    public ResponseEntity<Page<CatalogoEstados>> obtenerPorEntidadPaginado(
            @PathVariable String entidad,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CatalogoEstados> estados = catalogoEstadosServicio.obtenerPorEntidadPaginado(entidad, pageable);
        return ResponseEntity.ok(estados);
    }

    @GetMapping("/entidad/{entidad}/estado/{estado}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('ADMIN_NEGOCIO')")
    public ResponseEntity<CatalogoEstados> obtenerEstadoEspecifico(@PathVariable String entidad, @PathVariable String estado) {
        Optional<CatalogoEstados> estadoEncontrado = catalogoEstadosServicio.obtenerEstadoEspecifico(entidad, estado);
        return estadoEncontrado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/entidades")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('ADMIN_NEGOCIO')")
    public ResponseEntity<List<String>> obtenerTodasLasEntidades() {
        List<String> entidades = catalogoEstadosServicio.obtenerTodasLasEntidades();
        return ResponseEntity.ok(entidades);
    }

    // ============= ENDPOINTS DE FLUJO DE ESTADOS =============

    @GetMapping("/entidad/{entidad}/inicial")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('ADMIN_NEGOCIO')")
    public ResponseEntity<CatalogoEstados> obtenerEstadoInicial(@PathVariable String entidad) {
        Optional<CatalogoEstados> estadoInicial = catalogoEstadosServicio.obtenerEstadoInicial(entidad);
        return estadoInicial.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/entidad/{entidad}/finales")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('ADMIN_NEGOCIO')")
    public ResponseEntity<List<CatalogoEstados>> obtenerEstadosFinales(@PathVariable String entidad) {
        List<CatalogoEstados> estadosFinales = catalogoEstadosServicio.obtenerEstadosFinales(entidad);
        return ResponseEntity.ok(estadosFinales);
    }

    @GetMapping("/entidad/{entidad}/transiciones-hacia/{estadoDestino}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('ADMIN_NEGOCIO')")
    public ResponseEntity<List<CatalogoEstados>> obtenerEstadosQuePermiteTransicionA(@PathVariable String entidad, @PathVariable String estadoDestino) {
        List<CatalogoEstados> estados = catalogoEstadosServicio.obtenerEstadosQuePermiteTransicionA(entidad, estadoDestino);
        return ResponseEntity.ok(estados);
    }

    @PostMapping("/validar-transicion")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('ADMIN_NEGOCIO')")
    public ResponseEntity<Map<String, Boolean>> validarTransicion(@RequestBody Map<String, String> request) {
        String entidad = request.get("entidad");
        String estadoOrigen = request.get("estadoOrigen");
        String estadoDestino = request.get("estadoDestino");
        
        boolean esValida = catalogoEstadosServicio.validarTransicion(entidad, estadoOrigen, estadoDestino);
        return ResponseEntity.ok(Map.of("transicionValida", esValida));
    }

    // ============= ENDPOINTS DE BÚSQUEDA =============

    @GetMapping("/entidad/{entidad}/buscar")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('ADMIN_NEGOCIO')")
    public ResponseEntity<List<CatalogoEstados>> buscarPorDescripcion(@PathVariable String entidad, @RequestParam String descripcion) {
        List<CatalogoEstados> estados = catalogoEstadosServicio.buscarPorDescripcion(entidad, descripcion);
        return ResponseEntity.ok(estados);
    }

    @GetMapping("/entidad/{entidad}/existe/{estado}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('ADMIN_NEGOCIO')")
    public ResponseEntity<Map<String, Boolean>> existeEstado(@PathVariable String entidad, @PathVariable String estado) {
        boolean existe = catalogoEstadosServicio.existeEstado(entidad, estado);
        return ResponseEntity.ok(Map.of("existe", existe));
    }

    // ============= ENDPOINTS DE CONFIGURACIÓN =============

    @PutMapping("/{id}/establecer-inicial")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CatalogoEstados> establecerComoInicial(@PathVariable Integer id) {
        try {
            CatalogoEstados estado = catalogoEstadosServicio.establecerComoInicial(id, "sistema");
            return ResponseEntity.ok(estado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/toggle-final")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CatalogoEstados> toggleEstadoFinal(@PathVariable Integer id) {
        try {
            CatalogoEstados estado = catalogoEstadosServicio.toggleEstadoFinal(id, "sistema");
            return ResponseEntity.ok(estado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ============= ENDPOINTS DE INICIALIZACIÓN =============

    @PostMapping("/crear-estados-base/{entidad}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, String>> crearEstadosBase(@PathVariable String entidad) {
        try {
            catalogoEstadosServicio.crearEstadosBase(entidad, "sistema");
            return ResponseEntity.ok(Map.of("mensaje", "Estados base creados exitosamente para: " + entidad));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/inicializar-sistema")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, String>> inicializarSistema() {
        try {
            String[] entidades = {"Usuario", "Negocio", "Orden", "UsuarioNegocio", "NegocioNegocio"};
            StringBuilder resultados = new StringBuilder();
            
            for (String entidad : entidades) {
                try {
                    catalogoEstadosServicio.crearEstadosBase(entidad, "sistema");
                    resultados.append(entidad).append(": ✓ ");
                } catch (RuntimeException e) {
                    resultados.append(entidad).append(": ✗ (").append(e.getMessage()).append(") ");
                }
            }
            
            return ResponseEntity.ok(Map.of("mensaje", "Inicialización completada", "detalles", resultados.toString()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error en la inicialización: " + e.getMessage()));
        }
    }
}
package com.api.red.negocios.Servicios;

import com.api.red.negocios.Modelos.CatalogoEstados;
import com.api.red.negocios.Repositorios.CatalogoEstadosRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CatalogoEstadosServicio {

    @Autowired
    private CatalogoEstadosRepositorio catalogoEstadosRepositorio;

    // ============= OPERACIONES BÁSICAS CRUD =============

    public List<CatalogoEstados> obtenerTodos() {
        return catalogoEstadosRepositorio.findAll();
    }

    public Optional<CatalogoEstados> obtenerPorId(Integer id) {
        return catalogoEstadosRepositorio.findById(id);
    }

    public CatalogoEstados crear(CatalogoEstados catalogoEstados, String usuario) {
        // Establecer el orden automáticamente si no se proporciona
        if (catalogoEstados.getOrden() == null || catalogoEstados.getOrden() == 0) {
            catalogoEstados.setOrden(catalogoEstadosRepositorio.getNextOrdenByEntidad(catalogoEstados.getEntidad()));
        }
        
        catalogoEstados.setCreadoPor(usuario);
        catalogoEstados.setFechaCreacion(LocalDateTime.now());
        return catalogoEstadosRepositorio.save(catalogoEstados);
    }

    public CatalogoEstados actualizar(Integer id, CatalogoEstados catalogoEstadosActualizado, String usuario) {
        return catalogoEstadosRepositorio.findById(id)
            .map(catalogoEstados -> {
                catalogoEstados.setEntidad(catalogoEstadosActualizado.getEntidad());
                catalogoEstados.setEstado(catalogoEstadosActualizado.getEstado());
                catalogoEstados.setDescripcion(catalogoEstadosActualizado.getDescripcion());
                catalogoEstados.setColor(catalogoEstadosActualizado.getColor());
                catalogoEstados.setIcono(catalogoEstadosActualizado.getIcono());
                catalogoEstados.setOrden(catalogoEstadosActualizado.getOrden());
                catalogoEstados.setEsEstadoInicial(catalogoEstadosActualizado.getEsEstadoInicial());
                catalogoEstados.setEsEstadoFinal(catalogoEstadosActualizado.getEsEstadoFinal());
                catalogoEstados.setPermiteTransicionA(catalogoEstadosActualizado.getPermiteTransicionA());
                catalogoEstados.setModificadoPor(usuario);
                catalogoEstados.setFechaModificacion(LocalDateTime.now());
                return catalogoEstadosRepositorio.save(catalogoEstados);
            })
            .orElseThrow(() -> new RuntimeException("Estado no encontrado con ID: " + id));
    }

    public boolean eliminar(Integer id, String usuario) {
        return catalogoEstadosRepositorio.findById(id)
            .map(catalogoEstados -> {
                catalogoEstados.setActivo(false);
                catalogoEstados.setEliminadoPor(usuario);
                catalogoEstados.setFechaEliminacion(LocalDateTime.now());
                catalogoEstadosRepositorio.save(catalogoEstados);
                return true;
            })
            .orElse(false);
    }

    // ============= OPERACIONES POR ENTIDAD =============

    public List<CatalogoEstados> obtenerPorEntidad(String entidad) {
        return catalogoEstadosRepositorio.findByEntidadAndActivoTrueOrderByOrdenAsc(entidad);
    }

    public Page<CatalogoEstados> obtenerPorEntidadPaginado(String entidad, Pageable pageable) {
        return catalogoEstadosRepositorio.findByEntidadAndActivoTrue(entidad, pageable);
    }

    public Optional<CatalogoEstados> obtenerEstadoEspecifico(String entidad, String estado) {
        return catalogoEstadosRepositorio.findByEntidadAndEstadoAndActivoTrue(entidad, estado);
    }

    public List<String> obtenerTodasLasEntidades() {
        return catalogoEstadosRepositorio.findDistinctEntidades();
    }

    // ============= OPERACIONES DE FLUJO DE ESTADOS =============

    public Optional<CatalogoEstados> obtenerEstadoInicial(String entidad) {
        return catalogoEstadosRepositorio.findEstadoInicialByEntidad(entidad);
    }

    public List<CatalogoEstados> obtenerEstadosFinales(String entidad) {
        return catalogoEstadosRepositorio.findEstadosFinalesByEntidad(entidad);
    }

    public List<CatalogoEstados> obtenerEstadosQuePermiteTransicionA(String entidad, String estadoDestino) {
        return catalogoEstadosRepositorio.findEstadosQuePermiteTransicionA(entidad, estadoDestino);
    }

    public boolean validarTransicion(String entidad, String estadoOrigen, String estadoDestino) {
        Optional<CatalogoEstados> estadoOrigenObj = obtenerEstadoEspecifico(entidad, estadoOrigen);
        Optional<CatalogoEstados> estadoDestinoObj = obtenerEstadoEspecifico(entidad, estadoDestino);
        
        if (estadoOrigenObj.isEmpty() || estadoDestinoObj.isEmpty()) {
            return false;
        }
        
        return estadoOrigenObj.get().puedeTransicionarA(estadoDestino);
    }

    // ============= OPERACIONES DE BÚSQUEDA =============

    public List<CatalogoEstados> buscarPorDescripcion(String entidad, String descripcion) {
        return catalogoEstadosRepositorio.findByEntidadAndDescripcionContainingIgnoreCaseAndActivoTrue(entidad, descripcion);
    }

    public boolean existeEstado(String entidad, String estado) {
        return catalogoEstadosRepositorio.existsByEntidadAndEstadoAndActivoTrue(entidad, estado);
    }

    // ============= OPERACIONES DE CONFIGURACIÓN =============

    public CatalogoEstados establecerComoInicial(Integer id, String usuario) {
        return catalogoEstadosRepositorio.findById(id)
            .map(catalogoEstados -> {
                // Primero, quitar el flag de inicial de otros estados de la misma entidad
                List<CatalogoEstados> estadosActuales = obtenerPorEntidad(catalogoEstados.getEntidad());
                estadosActuales.forEach(estado -> {
                    if (estado.getEsEstadoInicial()) {
                        estado.setEsEstadoInicial(false);
                        estado.setModificadoPor(usuario);
                        estado.setFechaModificacion(LocalDateTime.now());
                        catalogoEstadosRepositorio.save(estado);
                    }
                });
                
                // Establecer este como inicial
                catalogoEstados.setEsEstadoInicial(true);
                catalogoEstados.setModificadoPor(usuario);
                catalogoEstados.setFechaModificacion(LocalDateTime.now());
                return catalogoEstadosRepositorio.save(catalogoEstados);
            })
            .orElseThrow(() -> new RuntimeException("Estado no encontrado con ID: " + id));
    }

    public CatalogoEstados toggleEstadoFinal(Integer id, String usuario) {
        return catalogoEstadosRepositorio.findById(id)
            .map(catalogoEstados -> {
                catalogoEstados.setEsEstadoFinal(!catalogoEstados.getEsEstadoFinal());
                catalogoEstados.setModificadoPor(usuario);
                catalogoEstados.setFechaModificacion(LocalDateTime.now());
                return catalogoEstadosRepositorio.save(catalogoEstados);
            })
            .orElseThrow(() -> new RuntimeException("Estado no encontrado con ID: " + id));
    }

    // ============= OPERACIONES MASIVAS =============

    public void crearEstadosBase(String entidad, String usuario) {
        // Verificar si ya existen estados para esta entidad
        if (!obtenerPorEntidad(entidad).isEmpty()) {
            throw new RuntimeException("Ya existen estados para la entidad: " + entidad);
        }

        // Crear estados básicos según la entidad
        switch (entidad.toLowerCase()) {
            case "usuario":
                crearEstadosUsuario(usuario);
                break;
            case "negocio":
                crearEstadosNegocio(usuario);
                break;
            case "orden":
                crearEstadosOrden(usuario);
                break;
            case "usuarionegocio":
                crearEstadosUsuarioNegocio(usuario);
                break;
            case "negocionegocio":
                crearEstadosNegocioNegocio(usuario);
                break;
            default:
                throw new RuntimeException("Entidad no soportada: " + entidad);
        }
    }

    private void crearEstadosUsuario(String usuario) {
        // Estados para Usuario
        crear(new CatalogoEstados(null, "Usuario", "Registrado", "Usuario recién registrado, pendiente de verificación", "#FFA500", "fa-user-plus", 1, true, false, "Verificado,Inactivo", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "Usuario", "Verificado", "Usuario con email verificado y cuenta activa", "#28A745", "fa-user-check", 2, false, false, "Activo,Suspendido,Inactivo", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "Usuario", "Activo", "Usuario completamente funcional", "#007BFF", "fa-user", 3, false, false, "Suspendido,Inactivo", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "Usuario", "Suspendido", "Usuario temporalmente suspendido", "#DC3545", "fa-user-slash", 4, false, false, "Activo,Inactivo", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "Usuario", "Inactivo", "Usuario desactivado", "#6C757D", "fa-user-times", 5, false, true, "", null, null, null, null, true, null, null), usuario);
    }

    private void crearEstadosNegocio(String usuario) {
        // Estados para Negocio
        crear(new CatalogoEstados(null, "Negocio", "Pendiente", "Negocio en proceso de verificación", "#FFA500", "fa-clock", 1, true, false, "Aprobado,Rechazado", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "Negocio", "Aprobado", "Negocio verificado y aprobado", "#28A745", "fa-check-circle", 2, false, false, "Activo,Suspendido", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "Negocio", "Activo", "Negocio operativo y funcional", "#007BFF", "fa-store", 3, false, false, "Suspendido,Inactivo", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "Negocio", "Suspendido", "Negocio temporalmente suspendido", "#DC3545", "fa-pause-circle", 4, false, false, "Activo,Inactivo", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "Negocio", "Rechazado", "Negocio rechazado en verificación", "#DC3545", "fa-times-circle", 5, false, true, "", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "Negocio", "Inactivo", "Negocio cerrado permanentemente", "#6C757D", "fa-store-slash", 6, false, true, "", null, null, null, null, true, null, null), usuario);
    }

    private void crearEstadosOrden(String usuario) {
        // Estados para Orden
        crear(new CatalogoEstados(null, "Orden", "Pendiente", "Orden creada, esperando confirmación", "#FFA500", "fa-shopping-cart", 1, true, false, "Confirmada,Cancelada", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "Orden", "Confirmada", "Orden confirmada por el negocio", "#17A2B8", "fa-handshake", 2, false, false, "Procesando,Cancelada", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "Orden", "Procesando", "Orden en proceso de preparación", "#007BFF", "fa-cogs", 3, false, false, "Enviada,Lista,Cancelada", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "Orden", "Lista", "Orden lista para entrega/recogida", "#28A745", "fa-box", 4, false, false, "Enviada,Entregada", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "Orden", "Enviada", "Orden enviada al cliente", "#6610F2", "fa-shipping-fast", 5, false, false, "Entregada,Devuelta", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "Orden", "Entregada", "Orden entregada exitosamente", "#28A745", "fa-check", 6, false, true, "", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "Orden", "Cancelada", "Orden cancelada", "#DC3545", "fa-times", 7, false, true, "", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "Orden", "Devuelta", "Orden devuelta por el cliente", "#FD7E14", "fa-undo", 8, false, true, "", null, null, null, null, true, null, null), usuario);
    }

    private void crearEstadosUsuarioNegocio(String usuario) {
        // Estados para UsuarioNegocio (membresías/empleados)
        crear(new CatalogoEstados(null, "UsuarioNegocio", "Solicitado", "Solicitud de membresía/empleo enviada", "#FFA500", "fa-paper-plane", 1, true, false, "Aprobado,Rechazado", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "UsuarioNegocio", "Aprobado", "Solicitud aprobada, usuario vinculado", "#28A745", "fa-user-plus", 2, false, false, "Activo,Suspendido", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "UsuarioNegocio", "Activo", "Membresía/empleo activo", "#007BFF", "fa-user-check", 3, false, false, "Suspendido,Inactivo", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "UsuarioNegocio", "Suspendido", "Membresía/empleo temporalmente suspendido", "#DC3545", "fa-user-slash", 4, false, false, "Activo,Inactivo", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "UsuarioNegocio", "Rechazado", "Solicitud rechazada", "#DC3545", "fa-user-times", 5, false, true, "", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "UsuarioNegocio", "Inactivo", "Membresía/empleo terminado", "#6C757D", "fa-user-minus", 6, false, true, "", null, null, null, null, true, null, null), usuario);
    }

    private void crearEstadosNegocioNegocio(String usuario) {
        // Estados para NegocioNegocio (alianzas/partnerships)
        crear(new CatalogoEstados(null, "NegocioNegocio", "Propuesta", "Propuesta de alianza enviada", "#FFA500", "fa-handshake", 1, true, false, "Negociacion,Aprobada,Rechazada", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "NegocioNegocio", "Negociacion", "En proceso de negociación de términos", "#17A2B8", "fa-comments", 2, false, false, "Aprobada,Rechazada,Pausada", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "NegocioNegocio", "Aprobada", "Alianza aprobada por ambas partes", "#28A745", "fa-check-double", 3, false, false, "Activa,Pausada", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "NegocioNegocio", "Activa", "Alianza operativa y funcional", "#007BFF", "fa-link", 4, false, false, "Pausada,Terminada", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "NegocioNegocio", "Pausada", "Alianza temporalmente pausada", "#FD7E14", "fa-pause", 5, false, false, "Activa,Terminada", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "NegocioNegocio", "Rechazada", "Propuesta de alianza rechazada", "#DC3545", "fa-times", 6, false, true, "", null, null, null, null, true, null, null), usuario);
        crear(new CatalogoEstados(null, "NegocioNegocio", "Terminada", "Alianza terminada", "#6C757D", "fa-unlink", 7, false, true, "", null, null, null, null, true, null, null), usuario);
    }
}
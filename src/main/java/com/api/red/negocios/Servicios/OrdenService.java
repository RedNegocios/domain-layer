package com.api.red.negocios.Servicios;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

import lombok.extern.slf4j.Slf4j;

import com.api.red.negocios.Excepciones.EntityNotFoundException;

@Service
@Transactional
@Slf4j
public class OrdenService {

    @Autowired
    private OrdenRepositorio ordenRepositorio;

    @Autowired
    private LineaOrdenRepositorio lineaOrdenRepositorio;

    @Autowired
    private NegocioRepositorio negocioRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    /**
     * Crear una nueva orden
     */
    public Orden crearOrden(OrdenDTO ordenDTO) {
        log.info("Iniciando creación de orden para negocio: {}", ordenDTO.getNegocioId());
        
        // 1. Obtener usuario autenticado
        Usuario usuario = obtenerUsuarioAutenticado();
        
        // 2. Validar y obtener negocio
        Negocio negocio = validarYObtenerNegocio(ordenDTO.getNegocioId());
        
        // 3. Crear la orden principal
        Orden nuevaOrden = crearOrdenPrincipal(ordenDTO, usuario, negocio);
        
        // 4. Guardar la orden
        Orden ordenGuardada = ordenRepositorio.save(nuevaOrden);
        log.info("Orden principal creada con ID: {}", ordenGuardada.getOrdenId());
        
        // 5. Crear las líneas de la orden
        crearLineasOrden(ordenDTO.getLineasOrden(), ordenGuardada);
        
        log.info("Orden completa creada exitosamente con ID: {}", ordenGuardada.getOrdenId());
        return ordenGuardada;
    }

    /**
     * Obtener órdenes del usuario autenticado
     */
    public Page<Orden> obtenerOrdenesDelUsuario(Pageable pageable) {
        Usuario usuario = obtenerUsuarioAutenticado();
        return ordenRepositorio.findByUsuario(usuario, pageable);
    }

    /**
     * Obtener órdenes por negocio
     */
    public List<Orden> obtenerOrdenesPorNegocio(Integer negocioId) {
        Negocio negocio = validarYObtenerNegocio(negocioId);
        return ordenRepositorio.findByNegocio(negocio);
    }

    /**
     * Actualizar estado de una orden
     */
    public Orden actualizarEstadoOrden(Integer ordenId, String nuevoEstado) {
        log.info("Actualizando estado de orden {} a: {}", ordenId, nuevoEstado);
        
        Orden orden = ordenRepositorio.findById(ordenId)
                .orElseThrow(() -> new EntityNotFoundException("Orden", ordenId));
        
        validarCambioEstado(orden.getEstado(), nuevoEstado);
        
        orden.setEstado(nuevoEstado);
        orden.setFechaModificacion(LocalDateTime.now());
        
        Orden ordenActualizada = ordenRepositorio.save(orden);
        log.info("Estado de orden {} actualizado exitosamente", ordenId);
        
        return ordenActualizada;
    }

    /**
     * Obtener detalle de líneas de una orden
     */
    public List<LineaOrden> obtenerDetalleOrden(Integer ordenId) {
        Orden orden = ordenRepositorio.findById(ordenId)
                .orElseThrow(() -> new EntityNotFoundException("Orden", ordenId));
        
        return lineaOrdenRepositorio.findByOrden(orden);
    }

    // ==================== MÉTODOS PRIVADOS ====================

    private Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        return usuarioRepositorio.findByUsername(username);
        //        .orElseThrow(() -> new EntityNotFoundException("Usuario", username));
    }

    private Negocio validarYObtenerNegocio(Integer negocioId) {
        return negocioRepositorio.findById(negocioId)
                .orElseThrow(() -> new EntityNotFoundException("Negocio", negocioId));
    }

    private Orden crearOrdenPrincipal(OrdenDTO ordenDTO, Usuario usuario, Negocio negocio) {
        Orden nuevaOrden = new Orden();
        nuevaOrden.setNegocio(negocio);
        nuevaOrden.setUsuario(usuario);
        nuevaOrden.setNumeroOrden(UUID.randomUUID().toString());
        nuevaOrden.setFechaOrden(LocalDateTime.now());
        nuevaOrden.setMontoTotal(ordenDTO.getMontoTotal());
        nuevaOrden.setEstado("Pendiente");
        
        return nuevaOrden;
    }

    private void crearLineasOrden(List<LineaOrdenDTO> lineasDTO, Orden orden) {
        for (LineaOrdenDTO linea : lineasDTO) {
            LineaOrden nuevaLinea = new LineaOrden();
            nuevaLinea.setOrden(orden);
            nuevaLinea.setNegocioProducto(linea.getNegocioProducto());
            nuevaLinea.setCantidad(linea.getCantidad());
            nuevaLinea.setPrecioUnitario(linea.getPrecioUnitario());

            lineaOrdenRepositorio.save(nuevaLinea);
        }
    }

    private void validarCambioEstado(String estadoActual, String nuevoEstado) {
        // Lógica de validación de estados
        if ("Entregada".equals(estadoActual) || "Cancelada".equals(estadoActual)) {
            throw new RuntimeException("No se puede cambiar el estado de una orden ya finalizada");
        }
        
        // Agregar más validaciones según las reglas de negocio
    }
}
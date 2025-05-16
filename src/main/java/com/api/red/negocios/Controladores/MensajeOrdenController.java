package com.api.red.negocios.Controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.api.red.negocios.Componentes.ChatWebSocketPublisher;
import com.api.red.negocios.DTO.MensajeOrdenDTO;
import com.api.red.negocios.DTO.MensajeOrdenRespuestaDTO;
import com.api.red.negocios.Modelos.MensajeOrden;
import com.api.red.negocios.Modelos.Negocio;
import com.api.red.negocios.Modelos.Orden;
import com.api.red.negocios.Modelos.Usuario;
import com.api.red.negocios.Repositorios.MensajeOrdenRepositorio;
import com.api.red.negocios.Repositorios.NegocioRepositorio;
import com.api.red.negocios.Repositorios.OrdenRepositorio;
import com.api.red.negocios.Repositorios.UsuarioRepositorio;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mensajes/orden")
@RequiredArgsConstructor
public class MensajeOrdenController {

    @Autowired
    private final ChatWebSocketPublisher chatWebSocketPublisher;

    @Autowired
    private final MensajeOrdenRepositorio mensajeRepo;

    @Autowired
    private final OrdenRepositorio ordenRepo;

    @Autowired
    private final UsuarioRepositorio usuarioRepo;

    @Autowired
    private final NegocioRepositorio negocioRepo;

    @GetMapping("/{ordenId}")
    public ResponseEntity<List<MensajeOrdenRespuestaDTO>> getMensajes(@PathVariable Integer ordenId) {
        List<MensajeOrden> mensajes = mensajeRepo.findByOrdenOrdenIdOrderByFechaEnvioAsc(ordenId);

        List<MensajeOrdenRespuestaDTO> resultado = mensajes.stream().map(m -> {
            MensajeOrdenRespuestaDTO dto = new MensajeOrdenRespuestaDTO();
            dto.setMensajeOrdenId(m.getMensajeOrdenId());
            dto.setContenido(m.getContenido());

            if (m.getEmisorUsuario() != null) {
                dto.setEmisorNombre(m.getEmisorUsuario().getUsername());
            } else if (m.getEmisorNegocio() != null) {
                dto.setEmisorNombre(m.getEmisorNegocio().getNombre());
            } else {
                dto.setEmisorNombre("Sistema");
            }

            return dto;
        }).toList();

        return ResponseEntity.ok(resultado);
    }


    @PostMapping("/{ordenId}/enviar")
    public ResponseEntity<MensajeOrden> enviarMensaje(
            @PathVariable Integer ordenId,
            @RequestBody MensajeOrdenDTO dto) {

        Orden orden = ordenRepo.findById(ordenId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Orden no encontrada"));

        MensajeOrden mensaje = new MensajeOrden();
        mensaje.setOrden(orden);
        mensaje.setContenido(dto.getContenido());

        if (dto.getEmisorUsuarioId() != null) {
            Usuario usuario = usuarioRepo.findById(dto.getEmisorUsuarioId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
            mensaje.setEmisorUsuario(usuario);
        } else if (dto.getEmisorNegocioId() != null) {
            Negocio negocio = negocioRepo.findById(dto.getEmisorNegocioId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Negocio no encontrado"));
            mensaje.setEmisorNegocio(negocio);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Emisor inválido");
        }

        MensajeOrden guardado = mensajeRepo.save(mensaje);

        // Enviar el mensaje por WebSocket
        chatWebSocketPublisher.notificarNuevoMensaje(guardado);

        return ResponseEntity.ok(guardado);
    }
}



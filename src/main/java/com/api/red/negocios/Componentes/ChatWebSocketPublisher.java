package com.api.red.negocios.Componentes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.api.red.negocios.Modelos.MensajeOrden;

import java.util.HashMap;
import java.util.Map;

@Component
public class ChatWebSocketPublisher {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notificarNuevoMensaje(MensajeOrden mensaje) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("mensajeOrdenId", mensaje.getMensajeOrdenId());
        msg.put("contenido", mensaje.getContenido());
        msg.put("fechaEnvio", mensaje.getFechaEnvio());
        msg.put("ordenId", mensaje.getOrden().getOrdenId());
        msg.put("emisorNombre", mensaje.getEmisorUsuario() != null
            ? mensaje.getEmisorUsuario().getUsername()
            : mensaje.getEmisorNegocio() != null
                ? mensaje.getEmisorNegocio().getNombre()
                : "Desconocido");

        messagingTemplate.convertAndSend("/topic/orden/" + mensaje.getOrden().getOrdenId(), msg);
    }
}

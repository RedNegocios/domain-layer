package com.api.red.negocios.Excepciones;

public class EntityNotFoundException extends RuntimeException {
    
    public EntityNotFoundException(String message) {
        super(message);
    }
    
    public EntityNotFoundException(String entity, Object id) {
        super(String.format("%s no encontrado con ID: %s", entity, id));
    }
}
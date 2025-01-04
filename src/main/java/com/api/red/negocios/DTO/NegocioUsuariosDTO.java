package com.api.red.negocios.DTO;

import java.util.List;

import lombok.Data;

@Data
public class NegocioUsuariosDTO {
    private String negocioNombre;
    private List<UsuarioDTO> usuarios;

    public NegocioUsuariosDTO(String negocioNombre, List<UsuarioDTO> usuarios) {
        this.negocioNombre = negocioNombre;
        this.usuarios = usuarios;
    }
}


package com.api.red.negocios.DTO;

import lombok.Data;

@Data
public class UsuarioDTO {
    private String username;
    private String email;
    private Integer usuarioId;

    public UsuarioDTO(String username, String email, Integer usuarioId) {
        this.username = username;
        this.email = email;
        this.usuarioId = usuarioId;
    }
}

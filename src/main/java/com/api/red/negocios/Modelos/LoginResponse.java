package com.api.red.negocios.Modelos;

import java.util.List;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String username;
    private List<Autoridad> autoridades; 
    private Integer usuarioId;
    
    public LoginResponse(String token, String username, List<Autoridad> autoridades, Integer usuarioId) {
        this.token = token;
        this.username = username;
        this.autoridades = autoridades;
        this.usuarioId = usuarioId;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }
    
    public List<Autoridad> getAutoridades() {
        return autoridades;
    }

}
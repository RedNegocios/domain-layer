package com.api.red.negocios.Controladores;


import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.api.red.negocios.Modelos.LoginRequest;
import com.api.red.negocios.Modelos.LoginResponse;
import com.api.red.negocios.Modelos.Usuario;
import com.api.red.negocios.Modelos.Autoridad;
import com.api.red.negocios.Modelos.UsuarioToken;
import com.api.red.negocios.Repositorios.UsuarioRepositorio;
import com.api.red.negocios.Repositorios.UsuarioTokenRepositorio;



import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/login")
public class LoginControlador {
	
    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger();

    private final UsuarioRepositorio usuarioRepositorio;
    private final UsuarioTokenRepositorio usuarioTokenRepositorio;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginControlador(UsuarioRepositorio usuarioRepositorio, UsuarioTokenRepositorio usuarioTokenRepositorio, PasswordEncoder passwordEncoder) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.usuarioTokenRepositorio = usuarioTokenRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Usuario usuario = usuarioRepositorio.findByUsername(loginRequest.getUsername());
        
        List<Autoridad> autoridades = usuario.getAutoridades();
        
    	logger.info(passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword()));
        
    	
    	
    	if (!passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        // Crear un nuevo token para el usuario
        String token = java.util.UUID.randomUUID().toString();
        UsuarioToken usuarioToken = new UsuarioToken();
        usuarioToken.setUsuario(usuario);
        usuarioToken.setToken(token);
        usuarioToken.setFechaExpiracion(LocalDateTime.now().plusHours(2)); // Token válido por 2 horas
        usuarioToken.setHabilitado(true);
        usuarioTokenRepositorio.save(usuarioToken);

        return ResponseEntity.ok(new LoginResponse(token, usuario.getUsername(), autoridades));
    }
}

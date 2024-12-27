package com.api.red.negocios.Controladores;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.red.negocios.Modelos.Autoridad;
import com.api.red.negocios.Modelos.Usuario;
import com.api.red.negocios.Repositorios.AutoridadRepositorio;
import com.api.red.negocios.Repositorios.UsuarioRepositorio;

@RestController
@RequestMapping("/api/registro")
public class RegistroControlador {

    @Autowired
    private UsuarioRepositorio usuarioService;

    @Autowired
    private AutoridadRepositorio autoridadRepositorio;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody Usuario usuario) {
        // Establecer valores predeterminados para el usuario
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setActivo(true);
        usuario.setHabilitado(true);
        usuario.setCuentaNoExpirada(true);
        usuario.setCredencialesNoExpiradas(true);
        usuario.setCuentaNoBloqueada(true);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Guardar el usuario en la base de datos
        Usuario nuevoUsuario = usuarioService.save(usuario);

        // Crear la autoridad "USER" para el usuario
        Autoridad autoridad = new Autoridad();
        autoridad.setUsuario(nuevoUsuario);
        autoridad.setAutoridad("ROLE_USER");
        autoridad.setFechaCreacion(LocalDateTime.now());
        autoridad.setActivo(true);

        // Guardar la autoridad en la base de datos
        autoridadRepositorio.save(autoridad);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }
}


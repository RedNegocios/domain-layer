package com.api.red.negocios.Filtros;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.api.red.negocios.Modelos.Autoridad;
import com.api.red.negocios.Modelos.Usuario;
import com.api.red.negocios.Modelos.UsuarioToken;
import com.api.red.negocios.Repositorios.AutoridadRepositorio;
import com.api.red.negocios.Repositorios.UsuarioTokenRepositorio;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;

@Component
public class AutorizacionFiltro extends GenericFilterBean {
	
	private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger();

    private final UsuarioTokenRepositorio usuarioTokenRepositorio;
    private final AutoridadRepositorio autoridadRepositorio;

    public AutorizacionFiltro(UsuarioTokenRepositorio usuarioTokenRepositorio, AutoridadRepositorio autoridadRepositorio) {
        this.usuarioTokenRepositorio = usuarioTokenRepositorio;
        this.autoridadRepositorio = autoridadRepositorio;
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        // Excluir el endpoint /api/login
        if (requestURI.contains("/api/login") || requestURI.contains("/error")) {
            chain.doFilter(request, response);
            return;
        }

        String header = httpRequest.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = header.substring(7);

        // Validar el token y obtener el usuario
        Optional<UsuarioToken> usuarioTokenOpt = usuarioTokenRepositorio.findByToken(token);

        if (usuarioTokenOpt.isEmpty()) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        UsuarioToken usuarioToken = (UsuarioToken)usuarioTokenOpt.get();

        // Obtener el usuario asociado al token
        Usuario usuario = usuarioToken.getUsuario();

        // Buscar roles asociados al usuario
        List<Autoridad> autoridades = autoridadRepositorio.findByUsuarioUsuarioId(usuario.getUsuarioId());

        if (autoridades.isEmpty()) {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // Establecer el contexto de seguridad
        UserDetails userDetails = User.withUsername(usuario.getUsername())
                                      .authorities(autoridades.stream().map(Autoridad::getAutoridad).toArray(String[]::new))
                                      .password("") // Password no relevante aquí
                                      .build();

        var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }
}

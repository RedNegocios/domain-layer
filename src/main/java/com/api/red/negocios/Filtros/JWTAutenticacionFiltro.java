package com.api.red.negocios.Filtros;

import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.api.red.negocios.Modelos.Autoridad;
import com.api.red.negocios.Modelos.Usuario;
import com.api.red.negocios.Modelos.UsuarioToken;
import com.api.red.negocios.Repositorios.UsuarioTokenRepositorio;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@Component
public class JWTAutenticacionFiltro extends GenericFilterBean {
	
	private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger();
	
    private UsuarioTokenRepositorio usuarioTokenRepositorio;
    
	public JWTAutenticacionFiltro(UsuarioTokenRepositorio usuarioTokenRepositorio) {
        this.usuarioTokenRepositorio = usuarioTokenRepositorio;
    }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
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

        // Verificar que el token exista 
        UsuarioToken usuarioToken = usuarioTokenRepositorio.findByToken(token).orElse(null);
        
        // revisamos si el token es valido o sea que no este expirado o que sea habilitado
        if (usuarioToken == null || !usuarioToken.getHabilitado() || 
                usuarioToken.getFechaExpiracion().isBefore(java.time.LocalDateTime.now())) {
        		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        
        Usuario usuario = usuarioToken.getUsuario();
        
        List<Autoridad> autoridades = usuario.getAutoridades(); 
        
        UsernamePasswordAuthenticationToken autenticacion  = new UsernamePasswordAuthenticationToken(usuario, null, autoridades);
        SecurityContextHolder.getContext().setAuthentication(autenticacion);

        
        
        
        // eventualmente agregar logica para usar jwt info por ahora solo tokencito y yapopapo

        chain.doFilter(request, response);
    }

	
}

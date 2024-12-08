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
        
        logger.info("Inicia el filtroteeeeeee");
        
        // Excluir el endpoint /api/login
        if (requestURI.contains("/api/login") || requestURI.contains("/error")) {
            chain.doFilter(request, response);
            return;
        }
		
		String header = httpRequest.getHeader("Authorization");
		
		logger.info(header);

        if (header == null || !header.startsWith("Bearer ")) {
        	
        	httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
            
        }

        String token = header.substring(7);
        
        logger.info(token);

        // Verificar que el token exista 
        UsuarioToken usuarioToken = usuarioTokenRepositorio.findByToken(token).orElse(null);
        
        logger.info(usuarioToken.getToken());
        
        // revisamos si el token es valido o sea que no este expirado o que sea habilitado
        if (usuarioToken == null || !usuarioToken.getHabilitado() || 
                !usuarioToken.getFechaExpiracion().isBefore(java.time.LocalDateTime.now())) {
        		logger.info("Entra donde no deberia F");
        		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        
        logger.info("previo a usuario token");
        
        Usuario usuario = usuarioToken.getUsuario();
        
        List<Autoridad> autoridades = usuario.getAutoridades(); 
        
        UsernamePasswordAuthenticationToken autenticacion  = new UsernamePasswordAuthenticationToken(usuario, null, autoridades);
        logger.info("previo a set autentication");
        SecurityContextHolder.getContext().setAuthentication(autenticacion);
        
        logger.info("Usuario autenticado: " + autenticacion.getName());
        logger.info("Class of Principal: " + autenticacion.getPrincipal().getClass().getName());
		logger.info("Principal: " + autenticacion.getPrincipal());
		logger.info("Autenticado: " + autenticacion.isAuthenticated());
		logger.info("Authorities: " + autenticacion.getAuthorities());
		logger.info("SecurityContext Authentication: " + SecurityContextHolder.getContext().getAuthentication());

        
        
        
        // eventualmente agregar logica para usar jwt info por ahora solo tokencito y yapopapo

        chain.doFilter(request, response);
    }

	
}

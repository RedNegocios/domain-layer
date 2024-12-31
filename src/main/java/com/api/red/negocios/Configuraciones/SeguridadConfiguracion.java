package com.api.red.negocios.Configuraciones;

import java.util.List;

import javax.crypto.SecretKey;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.cors.CorsConfiguration;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import com.api.red.negocios.Filtros.JWTAutenticacionFiltro;
//import com.api.red.negocios.Filtros.JWTAutorizacionFiltro;

@Configuration
@EnableMethodSecurity
public class SeguridadConfiguracion {
	
	private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger();
    
    @Autowired
    private JWTAutenticacionFiltro jwtAutenticacionFiltro;
    
    @Getter
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("mysecretkeymysecretkeymysecretkey".getBytes());

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configurando SecurityFilterChain con las reglas de autorización.");

        http
            .csrf(csrf -> csrf.disable()) // Desactiva CSRF (para APIs REST, es común desactivarlo)
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("http://localhost:3000")); // Permitir solicitudes desde React
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Métodos permitidos
                config.setAllowedHeaders(List.of("*")); // Permitir todos los encabezados
                config.setAllowCredentials(true); // Permitir credenciales (opcional)
                return config;
            }))
            .authorizeHttpRequests(auth -> auth
            		//.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            		//.requestMatchers(HttpMethod.OPTIONS, "/negocios/api/**").permitAll()
                .requestMatchers("/api/registro/**").permitAll() // Permitir acceso público
                .requestMatchers("/api/login/**").permitAll()    // Permitir acceso público
                //.requestMatchers("/api/negocios/**").permitAll()
                .anyRequest().permitAll()                   // Requiere rol "USER" para todo lo demás
            )
            .addFilterBefore(jwtAutenticacionFiltro, SecurityContextHolderFilter.class) // Filtro JWT antes del contexto de seguridad
            .sessionManagement(session -> session.disable())
            .logout(logout -> logout.disable())          // Desactiva el manejo de logout
            .anonymous(anonymous -> anonymous.disable()) // Desactiva usuarios anónimos
            .requestCache(requestCache -> requestCache.disable()); // Desactiva caché de solicitudes

        return http.build();
    }

    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

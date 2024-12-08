package com.api.red.negocios.Configuraciones;

import javax.crypto.SecretKey;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;

import com.api.red.negocios.Filtros.AutorizacionFiltro;
import com.api.red.negocios.Filtros.JWTAutenticacionFiltro;
//import com.api.red.negocios.Filtros.JWTAutorizacionFiltro;

@Configuration
@EnableMethodSecurity
public class SeguridadConfiguracion {
	
	private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger();
    
    @Autowired
    private JWTAutenticacionFiltro jwtAutenticacionFiltro;
    @Autowired
    private AutorizacionFiltro autorizacionFiltro;
    
    @Getter
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("mysecretkeymysecretkeymysecretkey".getBytes());

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/api/*").permitAll()
//                .requestMatchers("/api/negocios").hasRole("ADMIN")
                .anyRequest().permitAll()
            )
            .addFilterBefore(jwtAutenticacionFiltro, SecurityContextHolderFilter.class)
//            .addFilterAfter(autorizacionFiltro, JWTAutenticacionFiltro.class)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .anonymous(anonymous -> anonymous.disable());

        return http.build();
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

package com.api.red.negocios.Configuraciones;

import java.util.List;
import javax.crypto.SecretKey;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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

@Configuration
@EnableMethodSecurity
public class SeguridadConfiguracion {

    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger();

    @Autowired
    private JWTAutenticacionFiltro jwtAutenticacionFiltro;

    @Getter
    private static final SecretKey SECRET_KEY =
        Keys.hmacShaKeyFor("mysecretkeymysecretkeymysecretkey".getBytes());

    /* ------------------------------------------------------------------
     * 1) CADENA  WS / SockJS  ——  sin filtro JWT
     * ------------------------------------------------------------------ */
 // ---------- Cadena 1:  WS / SockJS ----------
    @Bean
    @Order(1)
    public SecurityFilterChain wsChain(HttpSecurity http) throws Exception {

        http
            .securityMatcher("/ws/**",        // cuando NO hay context-path
                             "/negocios/ws/**",    // cuando el context-path es /negocios
                             "/sockjs-node/**",
                             "/webjars/**",
                             "/topic/**")
            .csrf(c -> c.disable())
            .cors(c -> c.configurationSource(r -> corsConfig()))
            .authorizeHttpRequests(a -> a.anyRequest().permitAll())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


    /* ------------------------------------------------------------------
     * 2) CADENA  API  ——  tu configuración original (con JWT)
     * ------------------------------------------------------------------ */
    @Bean
    @Order(2)                       // ← se evalúa después de la de WS
    public SecurityFilterChain apiChain(HttpSecurity http) throws Exception {

        logger.info("Cadena API + JWT");

        http
            /* ---------- CSRF & CORS ---------- */
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(r -> corsConfig()))

            /* ---------- Autorización ---------- */
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/registro/**", "/api/login/**").permitAll()
                .anyRequest().permitAll()          // (igual que tenías)
            )

            /* ---------- Filtros ---------- */
            .addFilterBefore(jwtAutenticacionFiltro, SecurityContextHolderFilter.class)

            /* ---------- Session / State ---------- */
            .sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            /* ---------- Otros ---------- */
            .logout(l -> l.disable())
            .anonymous(a -> a.disable())
            .requestCache(rc -> rc.disable());

        return http.build();
    }

    /* ---------- Configuración CORS reutilizada por ambos chains ---------- */
    private CorsConfiguration corsConfig() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        return config;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

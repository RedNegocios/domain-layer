package com.api.red.negocios.Componentes;

import java.util.List;

import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class LoggerFiltros {

    private final FilterChainProxy filterChainProxy;

    public LoggerFiltros(FilterChainProxy filterChainProxy) {
        this.filterChainProxy = filterChainProxy;
    }

    @PostConstruct
    public void logFilters() {
        List<SecurityFilterChain> filterChains = filterChainProxy.getFilterChains();
        System.out.println("===== Orden de los Filtros =====");
        for (SecurityFilterChain chain : filterChains) {
            chain.getFilters().forEach(filter -> {
                System.out.println(filter.getClass().getName());
            });
        }
        System.out.println("================================");
    }
}

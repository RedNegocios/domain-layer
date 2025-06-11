package com.api.red.negocios.Repositorios;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class KPIITRepositorio {

    @PersistenceContext
    private EntityManager entityManager;
    
    public BigDecimal obtenerIngresosTotales(String token, Long negocioId) {
        String sql = """
            WITH usuario AS (
                SELECT TOP 10 usuarioId
                FROM UsuarioToken
                WHERE token = :token
            ),
            usuarioNegociosId AS (
                SELECT TOP 10 negocioId 
                FROM UsuarioNegocio a
                INNER JOIN usuario b ON a.usuarioId = b.usuarioId
                WHERE negocioId = :negocioId
            ),
            ordenes AS (
                SELECT TOP 10 montoTotal
                FROM Orden a
                INNER JOIN usuarioNegociosId b ON a.negocioId = b.negocioId
                WHERE estado = 'Aceptada'
            )
            SELECT SUM(montoTotal)
            FROM ordenes
        """;

        Query nativeQuery = entityManager.createNativeQuery(sql);
        nativeQuery.setParameter("token", token);
        nativeQuery.setParameter("negocioId", negocioId);
        Object result = nativeQuery.getSingleResult();
        return result != null ? (BigDecimal) result : BigDecimal.ZERO;
    }
}

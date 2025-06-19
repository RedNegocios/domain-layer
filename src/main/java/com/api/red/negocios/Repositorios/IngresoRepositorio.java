package com.api.red.negocios.Repositorios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.api.red.negocios.DTO.IngresoAgregadoDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class IngresoRepositorio {

    @PersistenceContext
    private EntityManager entityManager;

    /** Devuelve ingresos agregados por día, mes o año y filtrados por rango de fechas */
    public List<IngresoAgregadoDTO> obtenerIngresosPorPeriodo(
            String token,
            Long   negocioId,
            String tipo,
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        /* ─────────── Expresiones variables según el tipo ─────────── */

        String selectExp;   // columnas del SELECT  (con alias)
        String groupExp;    // lista para GROUP BY
        String orderExp;    // lista para ORDER BY

        switch (tipo.toLowerCase()) {

            /* ░░░ Agregado diario ░░░ */
            case "dia" -> {
                selectExp = """
                    CAST(o.fechaCreacion AS DATE) AS fecha,
                    DAY(o.fechaCreacion)          AS dia,
                    MONTH(o.fechaCreacion)        AS mes,
                    YEAR(o.fechaCreacion)         AS anio
                """;
                groupExp = """
                    CAST(o.fechaCreacion AS DATE),
                    DAY(o.fechaCreacion), MONTH(o.fechaCreacion), YEAR(o.fechaCreacion)
                """;
                orderExp = "anio, mes, dia";
            }

            /* ░░░ Agregado mensual ░░░ */
            case "mes" -> {
                selectExp = """
                    FORMAT(o.fechaCreacion,'yyyy-MM') AS fecha,
                    NULL                              AS dia,
                    MONTH(o.fechaCreacion)            AS mes,
                    YEAR(o.fechaCreacion)             AS anio
                """;
                groupExp = """
                    FORMAT(o.fechaCreacion,'yyyy-MM'),
                    MONTH(o.fechaCreacion), YEAR(o.fechaCreacion)
                """;
                orderExp = "anio, mes";
            }

            /* ░░░ Agregado anual ░░░ */
            case "anio" -> {
                selectExp = """
                    YEAR(o.fechaCreacion) AS fecha,
                    NULL                  AS dia,
                    NULL                  AS mes,
                    YEAR(o.fechaCreacion) AS anio
                """;
                groupExp = "YEAR(o.fechaCreacion)";
                orderExp = "anio";
            }

            default -> throw new IllegalArgumentException("Tipo inválido: " + tipo);
        }

        /* ───────────── Query nativa con filtro por fechas ───────────── */

        String sql = """
            WITH usuario_cte AS (
                SELECT usuarioId
                FROM   UsuarioToken
                WHERE  token = :token
            ),
            usuario_negocio_cte AS (
                SELECT negocioId
                FROM   UsuarioNegocio un
                JOIN   usuario_cte u ON u.usuarioId = un.usuarioId
                WHERE  un.negocioId = :negocioId
            ),
            ordenes_filtradas AS (
                SELECT o.ordenId,
                       o.montoTotal,
                       o.fechaCreacion
                FROM   Orden o
                JOIN   usuario_negocio_cte un ON o.negocioId = un.negocioId
                WHERE  o.estado = 'Aceptada'
                  AND  CAST(o.fechaCreacion AS DATE) BETWEEN :fechaInicio AND :fechaFin
            )
            SELECT
                %s,
                SUM(o.montoTotal) AS total
            FROM   ordenes_filtradas o
            GROUP  BY %s
            ORDER  BY %s
            """.formatted(selectExp, groupExp, orderExp);

        Query q = entityManager.createNativeQuery(sql);
        q.setParameter("token",       token);
        q.setParameter("negocioId",   negocioId);
        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin",    fechaFin);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();

        /* ───────────── Mapear a DTO ───────────── */

        return rows.stream()
                   .map(r -> new IngresoAgregadoDTO(
                               r[1] != null ? ((Number) r[1]).intValue() : null, // día
                               r[2] != null ? ((Number) r[2]).intValue() : null, // mes
                               r[3] != null ? ((Number) r[3]).intValue() : null, // año
                               (BigDecimal) r[4]                                    // total
                           ))
                   .toList();
    }
}


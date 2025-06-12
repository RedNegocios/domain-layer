package com.api.red.negocios.Repositorios;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.api.red.negocios.Modelos.KPITP;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class KPITPRepositorio {

    @PersistenceContext
    private EntityManager entityManager;

    public List<KPITP> obtenerTopProductosVendidos(String token, Long negocioId) {
        String sql = """
            WITH usuario_cte AS (
		    SELECT usuarioId FROM UsuarioToken WHERE token = :token
			),
			usuario_negocio_cte AS (
			    SELECT negocioId FROM UsuarioNegocio un
			    INNER JOIN usuario_cte u ON u.usuarioId = un.usuarioId
			    WHERE un.negocioId = :negocioId
			),
			ordenesAceptadas AS (
			    SELECT ordenId FROM Orden o
			    INNER JOIN usuario_negocio_cte un ON o.negocioId = un.negocioId
			    WHERE o.estado = 'Aceptada'
			),
			lineas AS (
			    SELECT lo.negocioProductoId, lo.cantidad
			    FROM LineaOrden lo
			    INNER JOIN ordenesAceptadas oa ON lo.ordenId = oa.ordenId
			),
			ventas AS (
			    SELECT
			        p.productoId,
			        p.nombre,
			        SUM(l.cantidad) AS totalVendida
			    FROM lineas l
			    INNER JOIN NegocioProducto np ON l.negocioProductoId = np.negocioProductoId
			    INNER JOIN Producto p ON np.productoId = p.productoId
			    WHERE np.negocioId = :negocioId
			    GROUP BY p.productoId, p.nombre
			)
			SELECT TOP 10 p.productoId, p.nombre, v.totalVendida
			FROM ventas v
			JOIN Producto p ON p.productoId = v.productoId
			ORDER BY v.totalVendida DESC
        """;

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("token", token);
        query.setParameter("negocioId", negocioId);

        List<Object[]> resultList = query.getResultList();

        return resultList.stream().map(r -> new KPITP(
                ((Number) r[0]).longValue(),
                (String) r[1],
                ((Number) r[2]).longValue()
        )).collect(Collectors.toList());
    }
}
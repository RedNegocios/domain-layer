package com.api.red.negocios.Repositorios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.red.negocios.Modelos.Negocio;
import com.api.red.negocios.Modelos.Orden;
import com.api.red.negocios.Modelos.Usuario;

@Repository
public interface OrdenRepositorio extends JpaRepository<Orden, Integer> {
    List<Orden> findByUsuario(Usuario usuario);
    List<Orden> findByNegocio(Negocio negocio);
    Page<Orden> findByNegocio(Negocio negocio, Pageable pageable);
    Page<Orden> findByUsuario(Usuario usuario, Pageable pageable);
    
    // Métodos para analytics
    List<Orden> findByNegocioAndFechaOrdenBetween(Negocio negocio, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<Orden> findByUsuarioAndFechaOrdenBetween(Usuario usuario, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    @Query("SELECT o FROM Orden o WHERE o.negocio.id IN :negocioIds AND o.fechaCreacion BETWEEN :fechaInicio AND :fechaFin AND o.estado IN ('Entregada', 'Completada')")
    List<Orden> findOrdenesCompletadasByNegociosAndFechas(@Param("negocioIds") List<Integer> negocioIds, 
                                                          @Param("fechaInicio") LocalDateTime fechaInicio, 
                                                          @Param("fechaFin") LocalDateTime fechaFin);
}

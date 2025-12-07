package com.api.red.negocios.Repositorios;

import com.api.red.negocios.Modelos.CatalogoEstados;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatalogoEstadosRepositorio extends JpaRepository<CatalogoEstados, Integer> {

    // Buscar todos los estados activos de una entidad específica, ordenados
    @Query("SELECT c FROM CatalogoEstados c WHERE c.entidad = :entidad AND c.activo = true ORDER BY c.orden ASC")
    List<CatalogoEstados> findByEntidadAndActivoTrueOrderByOrdenAsc(@Param("entidad") String entidad);

    // Buscar un estado específico de una entidad
    @Query("SELECT c FROM CatalogoEstados c WHERE c.entidad = :entidad AND c.estado = :estado AND c.activo = true")
    Optional<CatalogoEstados> findByEntidadAndEstadoAndActivoTrue(@Param("entidad") String entidad, @Param("estado") String estado);

    // Buscar el estado inicial de una entidad
    @Query("SELECT c FROM CatalogoEstados c WHERE c.entidad = :entidad AND c.esEstadoInicial = true AND c.activo = true")
    Optional<CatalogoEstados> findEstadoInicialByEntidad(@Param("entidad") String entidad);

    // Buscar estados finales de una entidad
    @Query("SELECT c FROM CatalogoEstados c WHERE c.entidad = :entidad AND c.esEstadoFinal = true AND c.activo = true ORDER BY c.orden ASC")
    List<CatalogoEstados> findEstadosFinalesByEntidad(@Param("entidad") String entidad);

    // Buscar todas las entidades únicas
    @Query("SELECT DISTINCT c.entidad FROM CatalogoEstados c WHERE c.activo = true ORDER BY c.entidad")
    List<String> findDistinctEntidades();

    // Buscar estados con paginación por entidad
    @Query("SELECT c FROM CatalogoEstados c WHERE c.entidad = :entidad AND c.activo = true")
    Page<CatalogoEstados> findByEntidadAndActivoTrue(@Param("entidad") String entidad, Pageable pageable);

    // Buscar estados que permiten transición a un estado específico
    @Query("SELECT c FROM CatalogoEstados c WHERE c.entidad = :entidad AND c.permiteTransicionA LIKE %:estadoDestino% AND c.activo = true")
    List<CatalogoEstados> findEstadosQuePermiteTransicionA(@Param("entidad") String entidad, @Param("estadoDestino") String estadoDestino);

    // Buscar por descripción (búsqueda parcial)
    @Query("SELECT c FROM CatalogoEstados c WHERE c.entidad = :entidad AND LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%')) AND c.activo = true ORDER BY c.orden ASC")
    List<CatalogoEstados> findByEntidadAndDescripcionContainingIgnoreCaseAndActivoTrue(@Param("entidad") String entidad, @Param("descripcion") String descripcion);

    // Verificar si existe un estado específico activo
    @Query("SELECT COUNT(c) > 0 FROM CatalogoEstados c WHERE c.entidad = :entidad AND c.estado = :estado AND c.activo = true")
    boolean existsByEntidadAndEstadoAndActivoTrue(@Param("entidad") String entidad, @Param("estado") String estado);

    // Obtener el siguiente orden disponible para una entidad
    @Query("SELECT COALESCE(MAX(c.orden), 0) + 1 FROM CatalogoEstados c WHERE c.entidad = :entidad")
    Integer getNextOrdenByEntidad(@Param("entidad") String entidad);
}
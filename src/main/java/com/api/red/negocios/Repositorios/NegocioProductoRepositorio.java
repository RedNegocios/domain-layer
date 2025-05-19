package com.api.red.negocios.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.red.negocios.Modelos.Negocio;
import com.api.red.negocios.Modelos.NegocioProducto;

@Repository
public interface NegocioProductoRepositorio extends JpaRepository<NegocioProducto, Integer> {
    List<NegocioProducto> findByNegocio(Negocio negocio);
    
    @Query("SELECT np FROM NegocioProducto np WHERE np.negocio = :negocio AND np.visualizacionProducto = true")
    List<NegocioProducto> findVisibleByNegocio(@Param("negocio") Negocio negocio);

}

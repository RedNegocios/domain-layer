package com.api.red.negocios.Modelos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CatalogoEstados", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"entidad", "estado"}))
public class CatalogoEstados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catalogoEstadoId")
    private Integer catalogoEstadoId;

    @Column(name = "entidad", nullable = false, length = 50)
    private String entidad; // Usuario, Negocio, Orden, UsuarioNegocio, NegocioNegocio, etc.

    @Column(name = "estado", nullable = false, length = 50)
    private String estado; // Activo, Inactivo, Pendiente, Aprobado, etc.

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "color", length = 7) // Para códigos hexadecimales como #FF5733
    private String color;

    @Column(name = "icono", length = 50) // Para clases CSS o nombres de iconos
    private String icono;

    @Column(name = "orden", nullable = false)
    private Integer orden = 0; // Para ordenar los estados en UI

    @Column(name = "esEstadoInicial", nullable = false)
    private Boolean esEstadoInicial = false;

    @Column(name = "esEstadoFinal", nullable = false)
    private Boolean esEstadoFinal = false;

    @Column(name = "permiteTransicionA", length = 500) // Estados a los que puede transicionar, separados por comas
    private String permiteTransicionA;

    // Campos de gobernanza
    @Column(name = "creadoPor", length = 100)
    private String creadoPor;

    @Column(name = "fechaCreacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "modificadoPor", length = 100)
    private String modificadoPor;

    @Column(name = "fechaModificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "eliminadoPor", length = 100)
    private String eliminadoPor;

    @Column(name = "fechaEliminacion")
    private LocalDateTime fechaEliminacion;

    // Método de conveniencia para verificar si puede transicionar a otro estado
    public boolean puedeTransicionarA(String estadoDestino) {
        if (permiteTransicionA == null || permiteTransicionA.isEmpty()) {
            return false;
        }
        String[] estadosPermitidos = permiteTransicionA.split(",");
        for (String estadoPermitido : estadosPermitidos) {
            if (estadoPermitido.trim().equals(estadoDestino)) {
                return true;
            }
        }
        return false;
    }
}
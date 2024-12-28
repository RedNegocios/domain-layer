package com.api.red.negocios.Modelos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Orden")
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ordenId;

    @ManyToOne
    @JoinColumn(name = "negocioId", nullable = false) // Configuración de la clave foránea
    private Negocio negocio;

    @Column(nullable = false, length = 50)
    private String numeroOrden;

    @Column(nullable = false)
    private LocalDateTime fechaOrden;

    @Column(nullable = false)
    private BigDecimal montoTotal;

    @Column(nullable = false, length = 50)
    private String estado; // Ejemplo: 'Pendiente', 'Completada'

    @Column(updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private String modificadoPor;

    private LocalDateTime fechaModificacion = LocalDateTime.now();

    private Boolean activo = true;

    private String eliminadoPor;

    private LocalDateTime fechaEliminacion;
}

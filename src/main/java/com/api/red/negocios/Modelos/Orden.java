package com.api.red.negocios.Modelos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Orden")
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ordenId;
    
    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<LineaOrden> lineasOrden;

    @ManyToOne
    @JoinColumn(name = "negocioId", nullable = false) // Configuración de la clave foránea con Negocio
    private Negocio negocio;

    @ManyToOne
    @JoinColumn(name = "usuarioId", nullable = false) // Configuración de la clave foránea con Usuario
    private Usuario usuario;

    @Column(nullable = false, length = 50)
    private String numeroOrden;

    @Column(nullable = false)
    private LocalDateTime fechaOrden;

    @Column(nullable = false)
    private BigDecimal montoTotal;

    @Column(nullable = false, length = 50)
    private String estado = "Pendiente"; // Ejemplo: 'Pendiente', 'Completada'

    @Column(updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private String modificadoPor;

    private LocalDateTime fechaModificacion = LocalDateTime.now();

    private Boolean activo = true;

    private String eliminadoPor;

    private LocalDateTime fechaEliminacion;
}

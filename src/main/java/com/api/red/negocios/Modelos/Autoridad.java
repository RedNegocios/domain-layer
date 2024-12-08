package com.api.red.negocios.Modelos;

import java.time.LocalDateTime;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Autoridad")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Autoridad implements GrantedAuthority{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "autoridadId")
    private Integer autoridadId;

    @ManyToOne
    @JoinColumn(name = "usuarioId", nullable = false)
    @JsonBackReference
    private Usuario usuario;

    @Column(name = "autoridad", nullable = false, length = 50)
    private String autoridad;

    @Column(name = "creadoPor")
    private String creadoPor;

    @Column(name = "fechaCreacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "modificadoPor")
    private String modificadoPor;

    @Column(name = "fechaModificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "eliminadoPor")
    private String eliminadoPor;

    @Column(name = "fechaEliminacion")
    private LocalDateTime fechaEliminacion;

	@Override
	public String getAuthority() {
		return this.autoridad;
	}
}

package com.api.red.negocios.Modelos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@ToString(exclude = "autoridades")
@Table(name = "Usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuarioId")
    private Integer usuarioId;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "habilitado", nullable = false)
    private Boolean habilitado = true;

    @Column(name = "cuentaNoExpirada", nullable = false)
    private Boolean cuentaNoExpirada = true;

    @Column(name = "credencialesNoExpiradas", nullable = false)
    private Boolean credencialesNoExpiradas = true;

    @Column(name = "cuentaNoBloqueada", nullable = false)
    private Boolean cuentaNoBloqueada = true;

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

    // Relación uno a muchos con autoridades
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference("autoridad-usuario")
    private List<Autoridad> autoridades;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference("token-usuario")
    private List<UsuarioToken> tokens;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference("usuario-negocio")
    private List<UsuarioNegocio> usuarioNegocios;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Devuelve la lista de autoridades asociadas al usuario
        return this.autoridades;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.cuentaNoExpirada;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.cuentaNoBloqueada;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credencialesNoExpiradas;
    }

    @Override
    public boolean isEnabled() {
        return this.habilitado;
    }
}



package com.api.red.negocios.Servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.red.negocios.Modelos.Usuario;
import com.api.red.negocios.Repositorios.UsuarioRepositorio;
// Nota que puedes implementar la interfaz de user details service y que spring cree cosas como findUsuarioById fin, solo pa eso sirve o mas pero por lo pronto
// se queda con la version mas dinamica posible

// Respuesta, para poder usar authenticator config hay que usarlo :/
@Service
public class UsuarioServicio implements UserDetailsService{

    private final UsuarioRepositorio usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioServicio(UsuarioRepositorio usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario saveUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> findUsuarioById(Integer id) {
        return usuarioRepository.findById(id);
    }

    public List<Usuario> findAllUsuarios() {
        return usuarioRepository.findAll();
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return usuarioRepository.findByUsername(username);
	}
}
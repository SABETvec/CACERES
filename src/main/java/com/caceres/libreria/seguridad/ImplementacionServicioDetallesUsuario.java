package com.caceres.libreria.seguridad;

import com.caceres.libreria.entity.Usuario;
import com.caceres.libreria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImplementacionServicioDetallesUsuario implements UserDetailsService {

    private final UsuarioRepository usuarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByUsername(nombreUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + nombreUsuario));

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles("USUARIO")
                .build();
    }
}
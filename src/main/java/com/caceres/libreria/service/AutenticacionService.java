package com.caceres.libreria.service;


import com.caceres.libreria.entity.dto.RespuestaTokenDto;
import com.caceres.libreria.entity.dto.SolicitudLoginDto;
import com.caceres.libreria.entity.Usuario;
import com.caceres.libreria.repository.UsuarioRepository;
import com.caceres.libreria.seguridad.JwtUtilidad;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AutenticacionService {

    private final AuthenticationManager gestorAutenticacion;
    private final JwtUtilidad jwtUtilidad;
    private final UsuarioRepository usuarioRepositorio;

    public RespuestaTokenDto iniciarSesion(SolicitudLoginDto solicitudLogin) {
        gestorAutenticacion.authenticate(
                new UsernamePasswordAuthenticationToken(
                        solicitudLogin.getNombreUsuario(),
                        solicitudLogin.getContrasena()
                )
        );

        Usuario usuario = usuarioRepositorio.findByUsername(solicitudLogin.getNombreUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String tokenJwt = jwtUtilidad.generarToken(usuario.getUsername(), new HashMap<>());

        RespuestaTokenDto respuestaToken = new RespuestaTokenDto();
        respuestaToken.setTokenJwt(tokenJwt);
        respuestaToken.setNombreUsuario(usuario.getUsername());

        if (usuario.getIdpersona() != null) {
            respuestaToken.setNombre(usuario.getIdpersona().getNombre());
            respuestaToken.setApellidoPaterno(usuario.getIdpersona().getApellidopaterno());
            respuestaToken.setFotoPerfil(usuario.getIdpersona().getFotoperfil());
        }

        return respuestaToken;
    }
}
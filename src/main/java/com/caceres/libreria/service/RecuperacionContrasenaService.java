package com.caceres.libreria.service;


import com.caceres.libreria.entity.dto.SolicitudRecuperarContrasenaDto;
import com.caceres.libreria.entity.dto.SolicitudRestablecerContrasenaDto;
import com.caceres.libreria.entity.Contacto;
import com.caceres.libreria.entity.TokenRecuperacion;
import com.caceres.libreria.entity.Usuario;
import com.caceres.libreria.repository.ContactoRepository;
import com.caceres.libreria.repository.TokenRecuperacionRepository;
import com.caceres.libreria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecuperacionContrasenaService {

    private final ContactoRepository contactoRepositorio;
    private final UsuarioRepository usuarioRepositorio;
    private final TokenRecuperacionRepository tokenRecuperacionRepositorio;
    private final CorreoElectronicoService correoElectronicoServicio;
    private final PasswordEncoder codificadorContrasena;

    @Value("${aplicacion.url.frontend}")
    private String urlFrontend;

    @Transactional
    public void solicitarRecuperacion(SolicitudRecuperarContrasenaDto solicitudRecuperacion) {
        Contacto contacto = contactoRepositorio.findByEmail(solicitudRecuperacion.getCorreoElectronico())
                .orElseThrow(() -> new RuntimeException("No existe una cuenta registrada con ese correo electronico"));

        Usuario usuario = usuarioRepositorio.buscarPorPersona(contacto.getIdpersona())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        tokenRecuperacionRepositorio.deleteByUsuario(usuario);

        String tokenGenerado = UUID.randomUUID().toString();

        TokenRecuperacion nuevoToken = new TokenRecuperacion();
        nuevoToken.setToken(tokenGenerado);
        nuevoToken.setFechaExpiracion(LocalDateTime.now().plusHours(1));
        nuevoToken.setUsado(false);
        nuevoToken.setUsuario(usuario);
        tokenRecuperacionRepositorio.save(nuevoToken);

        String enlaceRecuperacion = urlFrontend + "/restablecer-contrasena?token=" + tokenGenerado;

        correoElectronicoServicio.enviarCorreoRecuperacion(
                solicitudRecuperacion.getCorreoElectronico(),
                usuario.getIdpersona().getNombre(),
                enlaceRecuperacion
        );
    }

    public boolean validarToken(String tokenRecibido) {
        return tokenRecuperacionRepositorio.findByToken(tokenRecibido)
                .map(tokenRecuperacion ->
                        !tokenRecuperacion.getUsado() &&
                        tokenRecuperacion.getFechaExpiracion().isAfter(LocalDateTime.now())
                )
                .orElse(false);
    }

    @Transactional
    public void restablecerContrasena(SolicitudRestablecerContrasenaDto solicitudRestablecimiento) {
        TokenRecuperacion tokenRecuperacion = tokenRecuperacionRepositorio
                .findByToken(solicitudRestablecimiento.getToken())
                .orElseThrow(() -> new RuntimeException("Token invalido o no encontrado"));

        if (tokenRecuperacion.getUsado()) {
            throw new RuntimeException("Este enlace ya fue utilizado");
        }

        if (tokenRecuperacion.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Este enlace ha expirado, solicita uno nuevo");
        }

        Usuario usuario = tokenRecuperacion.getUsuario();
        usuario.setPassword(codificadorContrasena.encode(solicitudRestablecimiento.getNuevaContrasena()));
        usuarioRepositorio.save(usuario);

        tokenRecuperacion.setUsado(true);
        tokenRecuperacionRepositorio.save(tokenRecuperacion);
    }
}
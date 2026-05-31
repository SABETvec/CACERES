package com.caceres.libreria.service;

import com.caceres.libreria.entity.*;
import com.caceres.libreria.entity.dto.UsuarioDto;
import com.caceres.libreria.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final PasswordEncoder codificadorContrasena;
    private final PersonaRepository personaRepositorio;
    private final ContactoRepository contactoRepositorio;
    private final DireccionRepository direccionRepositorio;
    private final UsuarioRepository usuarioRepositorio;
    private final UsuarioRolRepository usuarioRolRepositorio;
    private final RolRepository rolRepositorio;

    @Value("${aplicacion.directorio.subidas:uploads}")
    private String directorioSubidas;

    public List<UsuarioDto> listarTodos() {
        return usuarioRepositorio.findAll()
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public UsuarioDto guardar(UsuarioDto usuarioDto, MultipartFile fotoPerfil) throws IOException {
        String nombreUsuarioGenerado = generarNombreUsuario(
                usuarioDto.getNombre(),
                usuarioDto.getApellidoPaterno()
        );

        Persona nuevaPersona = new Persona();
        nuevaPersona.setNombre(usuarioDto.getNombre());
        nuevaPersona.setApellidopaterno(usuarioDto.getApellidoPaterno());
        nuevaPersona.setApellidomaterno(usuarioDto.getApellidoMaterno());
        nuevaPersona.setSexo(usuarioDto.getSexo());
        nuevaPersona.setFechadenacimiento(usuarioDto.getFechaDeNacimiento());
        nuevaPersona.setFechacreacion(Instant.now());
        nuevaPersona.setFechaactualizacion(Instant.now());

        if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
            String rutaFoto = guardarFotoEnDisco(fotoPerfil, nombreUsuarioGenerado);
            nuevaPersona.setFotoperfil(rutaFoto);
        }

        nuevaPersona = personaRepositorio.save(nuevaPersona);

        Contacto nuevoContacto = new Contacto();
        nuevoContacto.setEmail(usuarioDto.getCorreoElectronico());
        nuevoContacto.setCelular(usuarioDto.getCelular());
        nuevoContacto.setIdpersona(nuevaPersona);
        contactoRepositorio.save(nuevoContacto);

        Direccion nuevaDireccion = new Direccion();
        nuevaDireccion.setPais(usuarioDto.getPais());
        nuevaDireccion.setCiudad(usuarioDto.getCiudad());
        nuevaDireccion.setZona(usuarioDto.getZona());
        nuevaDireccion.setDireccion(usuarioDto.getDireccion());
        nuevaDireccion.setReferencia(usuarioDto.getReferencia());
        nuevaDireccion.setIdpersona(nuevaPersona);
        direccionRepositorio.save(nuevaDireccion);

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(nombreUsuarioGenerado);
        nuevoUsuario.setPassword(codificadorContrasena.encode(usuarioDto.getContrasena()));
        nuevoUsuario.setActivo(true);
        nuevoUsuario.setIdpersona(nuevaPersona);
        nuevoUsuario = usuarioRepositorio.save(nuevoUsuario);

        if (usuarioDto.getIdRol() != null) {
            Rol rol = rolRepositorio.findById(usuarioDto.getIdRol())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            Usuariorol nuevoUsuarioRol = new Usuariorol();
            nuevoUsuarioRol.setIdusuario(nuevoUsuario);
            nuevoUsuarioRol.setIdrol(rol);
            nuevoUsuarioRol.setFecharegistro(Instant.now());
            usuarioRolRepositorio.save(nuevoUsuarioRol);
        }

        return convertirADto(nuevoUsuario);
    }

    public UsuarioDto actualizar(Integer identificador, UsuarioDto usuarioDto, MultipartFile fotoPerfil) throws IOException {
        Usuario usuarioExistente = usuarioRepositorio.findById(identificador)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuarioDto.getContrasena() != null && !usuarioDto.getContrasena().isBlank()) {
            usuarioExistente.setPassword(codificadorContrasena.encode(usuarioDto.getContrasena()));
        }

        Persona persona = usuarioExistente.getIdpersona();
        persona.setNombre(usuarioDto.getNombre());
        persona.setApellidopaterno(usuarioDto.getApellidoPaterno());
        persona.setApellidomaterno(usuarioDto.getApellidoMaterno());
        persona.setSexo(usuarioDto.getSexo());
        persona.setFechadenacimiento(usuarioDto.getFechaDeNacimiento());
        persona.setFechaactualizacion(Instant.now());

        if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
            String rutaFoto = guardarFotoEnDisco(fotoPerfil, usuarioExistente.getUsername());
            persona.setFotoperfil(rutaFoto);
        }

        personaRepositorio.save(persona);

        contactoRepositorio.findByIdpersona(persona).ifPresent(contacto -> {
            contacto.setEmail(usuarioDto.getCorreoElectronico());
            contacto.setCelular(usuarioDto.getCelular());
            contactoRepositorio.save(contacto);
        });

        direccionRepositorio.findByIdpersona(persona).ifPresent(direccion -> {
            direccion.setPais(usuarioDto.getPais());
            direccion.setCiudad(usuarioDto.getCiudad());
            direccion.setZona(usuarioDto.getZona());
            direccion.setDireccion(usuarioDto.getDireccion());
            direccion.setReferencia(usuarioDto.getReferencia());
            direccionRepositorio.save(direccion);
        });

        if (usuarioDto.getIdRol() != null) {
            usuarioRolRepositorio.findByIdusuario(usuarioExistente).ifPresent(usuarioRol -> {
                Rol rol = rolRepositorio.findById(usuarioDto.getIdRol())
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
                usuarioRol.setIdrol(rol);
                usuarioRolRepositorio.save(usuarioRol);
            });
        }

        usuarioRepositorio.save(usuarioExistente);
        return convertirADto(usuarioExistente);
    }

    public void eliminar(Integer identificador) {
        Usuario usuarioExistente = usuarioRepositorio.findById(identificador)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Persona persona = usuarioExistente.getIdpersona();

        usuarioRolRepositorio.findByIdusuario(usuarioExistente)
                .ifPresent(usuarioRolRepositorio::delete);
        usuarioRepositorio.delete(usuarioExistente);
        contactoRepositorio.findByIdpersona(persona)
                .ifPresent(contactoRepositorio::delete);
        direccionRepositorio.findByIdpersona(persona)
                .ifPresent(direccionRepositorio::delete);
        personaRepositorio.delete(persona);
    }

    public void cambiarEstado(Integer identificador) {
        Usuario usuarioExistente = usuarioRepositorio.findById(identificador)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuarioExistente.setActivo(!usuarioExistente.getActivo());
        usuarioRepositorio.save(usuarioExistente);
    }

    private String generarNombreUsuario(String nombre, String apellidoPaterno) {
        String primeraLetra = nombre.substring(0, 1).toLowerCase();
        String apellidoSinEspacios = apellidoPaterno.toLowerCase().replaceAll("\\s+", "");
        String baseNombreUsuario = primeraLetra + apellidoSinEspacios;
        String nombreUsuarioFinal = baseNombreUsuario;
        int contador = 1;
        while (usuarioRepositorio.existsByUsername(nombreUsuarioFinal)) {
            nombreUsuarioFinal = baseNombreUsuario + contador;
            contador++;
        }
        return nombreUsuarioFinal;
    }

    private String guardarFotoEnDisco(MultipartFile foto, String nombreUsuario) throws IOException {
        String extension = foto.getOriginalFilename()
                .substring(foto.getOriginalFilename().lastIndexOf("."));
        String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        String nombreArchivo = nombreUsuario + "_" + fechaActual + extension;
        Path rutaCompleta = Paths.get(directorioSubidas, "FotoPerfil", "Usuarios", nombreArchivo);
        Files.createDirectories(rutaCompleta.getParent());
        Files.write(rutaCompleta, foto.getBytes());
        return "/uploads/FotoPerfil/Usuarios/" + nombreArchivo;
    }

    private UsuarioDto convertirADto(Usuario usuario) {
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setIdUsuario(usuario.getId());
        usuarioDto.setNombreUsuario(usuario.getUsername());
        usuarioDto.setActivo(usuario.getActivo());

        Persona persona = usuario.getIdpersona();
        if (persona != null) {
            usuarioDto.setIdPersona(persona.getId());
            usuarioDto.setNombre(persona.getNombre());
            usuarioDto.setApellidoPaterno(persona.getApellidopaterno());
            usuarioDto.setApellidoMaterno(persona.getApellidomaterno());
            usuarioDto.setSexo(persona.getSexo());
            usuarioDto.setFechaDeNacimiento(persona.getFechadenacimiento());
            usuarioDto.setFotoPerfil(persona.getFotoperfil());
            usuarioDto.setFechaCreacion(persona.getFechacreacion());
            usuarioDto.setFechaActualizacion(persona.getFechaactualizacion());
        }

        contactoRepositorio.findByIdpersona(persona).ifPresent(contacto -> {
            usuarioDto.setIdContacto(contacto.getId());
            usuarioDto.setCorreoElectronico(contacto.getEmail());
            usuarioDto.setCelular(contacto.getCelular());
        });

        direccionRepositorio.findByIdpersona(persona).ifPresent(direccion -> {
            usuarioDto.setIdDireccion(direccion.getId());
            usuarioDto.setPais(direccion.getPais());
            usuarioDto.setCiudad(direccion.getCiudad());
            usuarioDto.setZona(direccion.getZona());
            usuarioDto.setDireccion(direccion.getDireccion());
            usuarioDto.setReferencia(direccion.getReferencia());
        });

        usuarioRolRepositorio.findByIdusuario(usuario).ifPresent(usuarioRol -> {
            usuarioDto.setIdUsuarioRol(usuarioRol.getId());
            usuarioDto.setIdRol(usuarioRol.getIdrol().getId());
            usuarioDto.setNombreRol(usuarioRol.getIdrol().getNombre());
        });

        return usuarioDto;
    }
}
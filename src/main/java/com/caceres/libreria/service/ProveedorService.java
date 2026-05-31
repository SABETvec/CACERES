package com.caceres.libreria.service;

import com.caceres.libreria.entity.Contacto;
import com.caceres.libreria.entity.Persona;
import com.caceres.libreria.entity.Proveedor;
import com.caceres.libreria.entity.dto.ProveedorDto;
import com.caceres.libreria.repository.ContactoRepository;
import com.caceres.libreria.repository.PersonaRepository;
import com.caceres.libreria.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
public class ProveedorService {

    private final ProveedorRepository proveedorRepositorio;
    private final PersonaRepository personaRepositorio;
    private final ContactoRepository contactoRepositorio;

    @Value("${aplicacion.directorio.subidas:uploads}")
    private String directorioSubidas;

    public List<ProveedorDto> listarTodos() {
        return proveedorRepositorio.findAll()
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public ProveedorDto guardar(ProveedorDto proveedorDto, MultipartFile fotoPerfil) throws IOException {
        Persona nuevaPersona = new Persona();
        nuevaPersona.setNombre(proveedorDto.getNombre());
        nuevaPersona.setApellidopaterno(proveedorDto.getApellidoPaterno());
        nuevaPersona.setApellidomaterno(proveedorDto.getApellidoMaterno());
        nuevaPersona.setSexo(proveedorDto.getSexo());
        nuevaPersona.setFechadenacimiento(proveedorDto.getFechaDeNacimiento());
        nuevaPersona.setFechacreacion(Instant.now());
        nuevaPersona.setFechaactualizacion(Instant.now());

        if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
            String NombreArchivo = generarNombreArchivo(proveedorDto.getNombre(), proveedorDto.getApellidoPaterno(), fotoPerfil);
            nuevaPersona.setFotoperfil(guardarFotoEnDisco(fotoPerfil, NombreArchivo));
        }

        nuevaPersona = personaRepositorio.save(nuevaPersona);

        Contacto nuevoContacto = new Contacto();
        nuevoContacto.setEmail(proveedorDto.getCorreoElectronico());
        nuevoContacto.setCelular(proveedorDto.getCelular());
        nuevoContacto.setIdpersona(nuevaPersona);
        contactoRepositorio.save(nuevoContacto);

        Proveedor nuevoProveedor = new Proveedor();
        nuevoProveedor.setDescripcion(proveedorDto.getDescripcion());
        nuevoProveedor.setIdpersona(nuevaPersona);
        nuevoProveedor = proveedorRepositorio.save(nuevoProveedor);

        return convertirADto(nuevoProveedor);
    }

    public ProveedorDto actualizar(Integer identificador, ProveedorDto proveedorDto, MultipartFile fotoPerfil) throws IOException {
        Proveedor proveedorExistente = proveedorRepositorio.findById(identificador)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        Persona persona = proveedorExistente.getIdpersona();
        persona.setNombre(proveedorDto.getNombre());
        persona.setApellidopaterno(proveedorDto.getApellidoPaterno());
        persona.setApellidomaterno(proveedorDto.getApellidoMaterno());
        persona.setSexo(proveedorDto.getSexo());
        persona.setFechadenacimiento(proveedorDto.getFechaDeNacimiento());
        persona.setFechaactualizacion(Instant.now());

        if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
            String NombreArchivo = generarNombreArchivo(proveedorDto.getNombre(), proveedorDto.getApellidoPaterno(), fotoPerfil);
            persona.setFotoperfil(guardarFotoEnDisco(fotoPerfil, NombreArchivo));
        }

        personaRepositorio.save(persona);

        contactoRepositorio.findByIdpersona(persona).ifPresent(contacto -> {
            contacto.setEmail(proveedorDto.getCorreoElectronico());
            contacto.setCelular(proveedorDto.getCelular());
            contactoRepositorio.save(contacto);
        });

        proveedorExistente.setDescripcion(proveedorDto.getDescripcion());
        proveedorRepositorio.save(proveedorExistente);
        return convertirADto(proveedorExistente);
    }

    public void eliminar(Integer identificador) {
        Proveedor proveedorExistente = proveedorRepositorio.findById(identificador)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        Persona persona = proveedorExistente.getIdpersona();
        proveedorRepositorio.delete(proveedorExistente);
        contactoRepositorio.findByIdpersona(persona).ifPresent(contactoRepositorio::delete);
        personaRepositorio.delete(persona);
    }

    private String generarNombreArchivo(String Nombre, String Apellido, MultipartFile Foto) {
        String Base = (Nombre.substring(0, 1) + Apellido).toLowerCase().replaceAll("\\s+", "");
        String Extension = Foto.getOriginalFilename().substring(Foto.getOriginalFilename().lastIndexOf("."));
        String Fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        return Base + "_" + Fecha + Extension;
    }

    private String guardarFotoEnDisco(MultipartFile Foto, String NombreArchivo) throws IOException {
        Path RutaCompleta = Paths.get(directorioSubidas, "FotoPerfil", "Proveedores", NombreArchivo);
        Files.createDirectories(RutaCompleta.getParent());
        Files.write(RutaCompleta, Foto.getBytes());
        return "/uploads/FotoPerfil/Proveedores/" + NombreArchivo;
    }

    private ProveedorDto convertirADto(Proveedor proveedor) {
        ProveedorDto proveedorDto = new ProveedorDto();
        proveedorDto.setIdProveedor(proveedor.getId());
        proveedorDto.setDescripcion(proveedor.getDescripcion());

        Persona persona = proveedor.getIdpersona();
        if (persona != null) {
            proveedorDto.setIdPersona(persona.getId());
            proveedorDto.setNombre(persona.getNombre());
            proveedorDto.setApellidoPaterno(persona.getApellidopaterno());
            proveedorDto.setApellidoMaterno(persona.getApellidomaterno());
            proveedorDto.setSexo(persona.getSexo());
            proveedorDto.setFechaDeNacimiento(persona.getFechadenacimiento());
            proveedorDto.setFotoPerfil(persona.getFotoperfil());
            proveedorDto.setFechaCreacion(persona.getFechacreacion());
            proveedorDto.setFechaActualizacion(persona.getFechaactualizacion());

            contactoRepositorio.findByIdpersona(persona).ifPresent(contacto -> {
                proveedorDto.setIdContacto(contacto.getId());
                proveedorDto.setCorreoElectronico(contacto.getEmail());
                proveedorDto.setCelular(contacto.getCelular());
            });
        }

        return proveedorDto;
    }
}
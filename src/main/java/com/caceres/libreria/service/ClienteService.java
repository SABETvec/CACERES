package com.caceres.libreria.service;

import com.caceres.libreria.entity.Cliente;
import com.caceres.libreria.entity.Contacto;
import com.caceres.libreria.entity.Direccion;
import com.caceres.libreria.entity.Persona;
import com.caceres.libreria.entity.dto.ClienteDto;
import com.caceres.libreria.repository.ClienteRepository;
import com.caceres.libreria.repository.ContactoRepository;
import com.caceres.libreria.repository.DireccionRepository;
import com.caceres.libreria.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigDecimal;
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
public class ClienteService {

    private final ClienteRepository clienteRepositorio;
    private final PersonaRepository personaRepositorio;
    private final ContactoRepository contactoRepositorio;
    private final DireccionRepository direccionRepositorio;

    @Value("${aplicacion.directorio.subidas:uploads}")
    private String directorioSubidas;

    public List<ClienteDto> listarTodos() {
        return clienteRepositorio.findAll()
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public ClienteDto guardar(ClienteDto clienteDto, MultipartFile fotoPerfil) throws IOException {
        Persona nuevaPersona = new Persona();
        nuevaPersona.setNombre(clienteDto.getNombre());
        nuevaPersona.setApellidopaterno(clienteDto.getApellidoPaterno());
        nuevaPersona.setApellidomaterno(clienteDto.getApellidoMaterno());
        nuevaPersona.setSexo(clienteDto.getSexo());
        nuevaPersona.setFechadenacimiento(clienteDto.getFechaDeNacimiento());
        nuevaPersona.setFechacreacion(Instant.now());
        nuevaPersona.setFechaactualizacion(Instant.now());

        if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
            String NombreArchivo = generarNombreArchivo(clienteDto.getNombre(), clienteDto.getApellidoPaterno(), fotoPerfil);
            nuevaPersona.setFotoperfil(guardarFotoEnDisco(fotoPerfil, NombreArchivo, "Clientes"));
        }

        nuevaPersona = personaRepositorio.save(nuevaPersona);

        Contacto nuevoContacto = new Contacto();
        nuevoContacto.setEmail(clienteDto.getCorreoElectronico());
        nuevoContacto.setCelular(clienteDto.getCelular());
        nuevoContacto.setIdpersona(nuevaPersona);
        contactoRepositorio.save(nuevoContacto);

        Direccion nuevaDireccion = new Direccion();
        nuevaDireccion.setPais(clienteDto.getPais());
        nuevaDireccion.setCiudad(clienteDto.getCiudad());
        nuevaDireccion.setZona(clienteDto.getZona());
        nuevaDireccion.setDireccion(clienteDto.getDireccion());
        nuevaDireccion.setReferencia(clienteDto.getReferencia());
        nuevaDireccion.setIdpersona(nuevaPersona);
        direccionRepositorio.save(nuevaDireccion);

        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setSaldo(clienteDto.getSaldo() != null ? clienteDto.getSaldo() : BigDecimal.ZERO);
        nuevoCliente.setIdpersona(nuevaPersona);
        nuevoCliente = clienteRepositorio.save(nuevoCliente);

        return convertirADto(nuevoCliente);
    }

    public ClienteDto actualizar(Integer identificador, ClienteDto clienteDto, MultipartFile fotoPerfil) throws IOException {
        Cliente clienteExistente = clienteRepositorio.findById(identificador)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Persona persona = clienteExistente.getIdpersona();
        persona.setNombre(clienteDto.getNombre());
        persona.setApellidopaterno(clienteDto.getApellidoPaterno());
        persona.setApellidomaterno(clienteDto.getApellidoMaterno());
        persona.setSexo(clienteDto.getSexo());
        persona.setFechadenacimiento(clienteDto.getFechaDeNacimiento());
        persona.setFechaactualizacion(Instant.now());

        if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
            String NombreArchivo = generarNombreArchivo(clienteDto.getNombre(), clienteDto.getApellidoPaterno(), fotoPerfil);
            persona.setFotoperfil(guardarFotoEnDisco(fotoPerfil, NombreArchivo, "Clientes"));
        }

        personaRepositorio.save(persona);

        contactoRepositorio.findByIdpersona(persona).ifPresent(contacto -> {
            contacto.setEmail(clienteDto.getCorreoElectronico());
            contacto.setCelular(clienteDto.getCelular());
            contactoRepositorio.save(contacto);
        });

        direccionRepositorio.findByIdpersona(persona).ifPresent(direccion -> {
            direccion.setPais(clienteDto.getPais());
            direccion.setCiudad(clienteDto.getCiudad());
            direccion.setZona(clienteDto.getZona());
            direccion.setDireccion(clienteDto.getDireccion());
            direccion.setReferencia(clienteDto.getReferencia());
            direccionRepositorio.save(direccion);
        });

        if (clienteDto.getSaldo() != null) {
            clienteExistente.setSaldo(clienteDto.getSaldo());
        }

        clienteRepositorio.save(clienteExistente);
        return convertirADto(clienteExistente);
    }

    public void eliminar(Integer identificador) {
        Cliente clienteExistente = clienteRepositorio.findById(identificador)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Persona persona = clienteExistente.getIdpersona();
        clienteRepositorio.delete(clienteExistente);
        contactoRepositorio.findByIdpersona(persona).ifPresent(contactoRepositorio::delete);
        direccionRepositorio.findByIdpersona(persona).ifPresent(direccionRepositorio::delete);
        personaRepositorio.delete(persona);
    }

    private String generarNombreArchivo(String Nombre, String Apellido, MultipartFile Foto) {
        String Base = (Nombre.substring(0, 1) + Apellido).toLowerCase().replaceAll("\\s+", "");
        String Extension = Foto.getOriginalFilename().substring(Foto.getOriginalFilename().lastIndexOf("."));
        String Fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        return Base + "_" + Fecha + Extension;
    }

    private String guardarFotoEnDisco(MultipartFile Foto, String NombreArchivo, String Subcarpeta) throws IOException {
        Path RutaCompleta = Paths.get(directorioSubidas, "FotoPerfil", Subcarpeta, NombreArchivo);
        Files.createDirectories(RutaCompleta.getParent());
        Files.write(RutaCompleta, Foto.getBytes());
        return "/uploads/FotoPerfil/" + Subcarpeta + "/" + NombreArchivo;
    }

    private ClienteDto convertirADto(Cliente cliente) {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setIdCliente(cliente.getId());
        clienteDto.setSaldo(cliente.getSaldo());

        Persona persona = cliente.getIdpersona();
        if (persona != null) {
            clienteDto.setIdPersona(persona.getId());
            clienteDto.setNombre(persona.getNombre());
            clienteDto.setApellidoPaterno(persona.getApellidopaterno());
            clienteDto.setApellidoMaterno(persona.getApellidomaterno());
            clienteDto.setSexo(persona.getSexo());
            clienteDto.setFechaDeNacimiento(persona.getFechadenacimiento());
            clienteDto.setFotoPerfil(persona.getFotoperfil());
            clienteDto.setFechaCreacion(persona.getFechacreacion());
            clienteDto.setFechaActualizacion(persona.getFechaactualizacion());

            contactoRepositorio.findByIdpersona(persona).ifPresent(contacto -> {
                clienteDto.setIdContacto(contacto.getId());
                clienteDto.setCorreoElectronico(contacto.getEmail());
                clienteDto.setCelular(contacto.getCelular());
            });

            direccionRepositorio.findByIdpersona(persona).ifPresent(direccion -> {
                clienteDto.setIdDireccion(direccion.getId());
                clienteDto.setPais(direccion.getPais());
                clienteDto.setCiudad(direccion.getCiudad());
                clienteDto.setZona(direccion.getZona());
                clienteDto.setDireccion(direccion.getDireccion());
                clienteDto.setReferencia(direccion.getReferencia());
            });
        }

        return clienteDto;
    }
}
package com.caceres.libreria.service;

import com.caceres.libreria.entity.Rol;
import com.caceres.libreria.entity.dto.RolDto;
import com.caceres.libreria.repository.RolAccesoRepository;
import com.caceres.libreria.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepositorio;
    private final RolAccesoRepository rolAccesoRepositorio;

    public List<RolDto> listarTodos() {
        return rolRepositorio.findAll()
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public RolDto guardar(RolDto rolDto) {
        if (rolRepositorio.existsByNombre(rolDto.getNombre())) {
            throw new RuntimeException("Ya existe un rol con ese nombre");
        }
        Rol nuevoRol = new Rol();
        nuevoRol.setNombre(rolDto.getNombre());
        return convertirADto(rolRepositorio.save(nuevoRol));
    }

    public RolDto actualizar(Integer identificador, RolDto rolDto) {
        Rol rolExistente = rolRepositorio.findById(identificador)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        rolExistente.setNombre(rolDto.getNombre());
        return convertirADto(rolRepositorio.save(rolExistente));
    }

    public void eliminar(Integer identificador) {
        rolRepositorio.findById(identificador)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        rolAccesoRepositorio.deleteByIdrol_Id(identificador);
        rolRepositorio.deleteById(identificador);
    }

    private RolDto convertirADto(Rol rol) {
        RolDto rolDto = new RolDto();
        rolDto.setIdRol(rol.getId());
        rolDto.setNombre(rol.getNombre());
        return rolDto;
    }
}
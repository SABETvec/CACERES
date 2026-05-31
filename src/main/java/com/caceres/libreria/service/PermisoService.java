package com.caceres.libreria.service;

import com.caceres.libreria.entity.Permiso;
import com.caceres.libreria.entity.dto.PermisoDto;
import com.caceres.libreria.repository.PermisoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermisoService {

    private final PermisoRepository permisoRepositorio;

    public List<PermisoDto> listarTodos() {
        return permisoRepositorio.findAll()
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    private PermisoDto convertirADto(Permiso permiso) {
        PermisoDto permisoDto = new PermisoDto();
        permisoDto.setIdPermiso(permiso.getId());
        permisoDto.setNombre(permiso.getNombre());
        return permisoDto;
    }
}
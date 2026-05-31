package com.caceres.libreria.service;

import com.caceres.libreria.entity.Modulo;
import com.caceres.libreria.entity.dto.ModuloDto;
import com.caceres.libreria.repository.ModuloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuloService {

    private final ModuloRepository moduloRepositorio;

    public List<ModuloDto> listarTodos() {
        return moduloRepositorio.findAll()
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    private ModuloDto convertirADto(Modulo modulo) {
        ModuloDto moduloDto = new ModuloDto();
        moduloDto.setIdModulo(modulo.getId());
        moduloDto.setNombre(modulo.getNombre());
        moduloDto.setIcono(modulo.getIcono());
        moduloDto.setRuta(modulo.getRuta());
        return moduloDto;
    }
}
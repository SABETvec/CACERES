package com.caceres.libreria.controller;

import com.caceres.libreria.entity.dto.PermisoDto;
import com.caceres.libreria.service.PermisoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/permisos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PermisoController {

    private final PermisoService permisoServicio;

    @GetMapping
    public List<PermisoDto> listarTodos() {
        return permisoServicio.listarTodos();
    }
}
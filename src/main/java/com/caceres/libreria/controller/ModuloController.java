package com.caceres.libreria.controller;

import com.caceres.libreria.entity.dto.ModuloDto;
import com.caceres.libreria.service.ModuloService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/modulos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ModuloController {

    private final ModuloService moduloServicio;

    @GetMapping
    public List<ModuloDto> listarTodos() {
        return moduloServicio.listarTodos();
    }
}
package com.caceres.libreria.controller;

import com.caceres.libreria.entity.dto.RolDto;
import com.caceres.libreria.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RolController {

    private final RolService rolServicio;

    @GetMapping
    public List<RolDto> listarTodos() {
        return rolServicio.listarTodos();
    }

    @PostMapping
    public RolDto guardar(@RequestBody RolDto rolDto) {
        return rolServicio.guardar(rolDto);
    }

    @PutMapping("/{identificador}")
    public RolDto actualizar(@PathVariable Integer identificador, @RequestBody RolDto rolDto) {
        return rolServicio.actualizar(identificador, rolDto);
    }

    @DeleteMapping("/{identificador}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer identificador) {
        rolServicio.eliminar(identificador);
        return ResponseEntity.noContent().build();
    }
}
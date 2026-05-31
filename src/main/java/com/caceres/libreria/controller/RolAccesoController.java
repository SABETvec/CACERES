package com.caceres.libreria.controller;

import com.caceres.libreria.entity.dto.GuardarAccesosDto;
import com.caceres.libreria.entity.dto.RolAccesoDto;
import com.caceres.libreria.service.RolAccesoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rol-accesos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RolAccesoController {

    private final RolAccesoService rolAccesoServicio;

    @GetMapping("/rol/{idRol}")
    public List<RolAccesoDto> listarPorRol(@PathVariable Integer idRol) {
        return rolAccesoServicio.listarPorRol(idRol);
    }

    @PostMapping
    public ResponseEntity<Void> guardarAccesos(@RequestBody GuardarAccesosDto guardarAccesosDto) {
        rolAccesoServicio.guardarAccesos(guardarAccesosDto);
        return ResponseEntity.ok().build();
    }
}
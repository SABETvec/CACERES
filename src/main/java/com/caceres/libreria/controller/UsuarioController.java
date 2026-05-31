package com.caceres.libreria.controller;

import com.caceres.libreria.entity.dto.UsuarioDto;
import com.caceres.libreria.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioServicio;

    @GetMapping
    public List<UsuarioDto> listarTodos() {
        return usuarioServicio.listarTodos();
    }

    @PostMapping(consumes = "multipart/form-data")
    public UsuarioDto guardar(
            @RequestPart("usuario") UsuarioDto usuarioDto,
            @RequestPart(value = "foto", required = false) MultipartFile fotoPerfil
    ) throws IOException {
        return usuarioServicio.guardar(usuarioDto, fotoPerfil);
    }

    @PutMapping(value = "/{identificador}", consumes = "multipart/form-data")
    public UsuarioDto actualizar(
            @PathVariable Integer identificador,
            @RequestPart("usuario") UsuarioDto usuarioDto,
            @RequestPart(value = "foto", required = false) MultipartFile fotoPerfil
    ) throws IOException {
        return usuarioServicio.actualizar(identificador, usuarioDto, fotoPerfil);
    }

    @DeleteMapping("/{identificador}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer identificador) {
        usuarioServicio.eliminar(identificador);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{identificador}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Integer identificador) {
        usuarioServicio.cambiarEstado(identificador);
        return ResponseEntity.ok().build();
    }
}
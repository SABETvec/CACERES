package com.caceres.libreria.controller;

import com.caceres.libreria.entity.dto.ProveedorDto;
import com.caceres.libreria.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProveedorController {

    private final ProveedorService proveedorServicio;

    @GetMapping
    public List<ProveedorDto> listarTodos() {
        return proveedorServicio.listarTodos();
    }

    @PostMapping(consumes = "multipart/form-data")
    public ProveedorDto guardar(
            @RequestPart("proveedor") ProveedorDto proveedorDto,
            @RequestPart(value = "foto", required = false) MultipartFile fotoPerfil
    ) throws IOException {
        return proveedorServicio.guardar(proveedorDto, fotoPerfil);
    }

    @PutMapping(value = "/{identificador}", consumes = "multipart/form-data")
    public ProveedorDto actualizar(
            @PathVariable Integer identificador,
            @RequestPart("proveedor") ProveedorDto proveedorDto,
            @RequestPart(value = "foto", required = false) MultipartFile fotoPerfil
    ) throws IOException {
        return proveedorServicio.actualizar(identificador, proveedorDto, fotoPerfil);
    }

    @DeleteMapping("/{identificador}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer identificador) {
        proveedorServicio.eliminar(identificador);
        return ResponseEntity.noContent().build();
    }
}
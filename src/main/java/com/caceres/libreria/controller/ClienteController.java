package com.caceres.libreria.controller;

import com.caceres.libreria.entity.dto.ClienteDto;
import com.caceres.libreria.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService clienteServicio;

    @GetMapping
    public List<ClienteDto> listarTodos() {
        return clienteServicio.listarTodos();
    }

    @PostMapping(consumes = "multipart/form-data")
    public ClienteDto guardar(
            @RequestPart("cliente") ClienteDto clienteDto,
            @RequestPart(value = "foto", required = false) MultipartFile fotoPerfil
    ) throws IOException {
        return clienteServicio.guardar(clienteDto, fotoPerfil);
    }

    @PutMapping(value = "/{identificador}", consumes = "multipart/form-data")
    public ClienteDto actualizar(
            @PathVariable Integer identificador,
            @RequestPart("cliente") ClienteDto clienteDto,
            @RequestPart(value = "foto", required = false) MultipartFile fotoPerfil
    ) throws IOException {
        return clienteServicio.actualizar(identificador, clienteDto, fotoPerfil);
    }

    @DeleteMapping("/{identificador}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer identificador) {
        clienteServicio.eliminar(identificador);
        return ResponseEntity.noContent().build();
    }
}
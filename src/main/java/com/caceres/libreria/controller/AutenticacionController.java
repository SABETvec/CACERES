package com.caceres.libreria.controller;


import com.caceres.libreria.entity.dto.RespuestaTokenDto;
import com.caceres.libreria.entity.dto.SolicitudLoginDto;
import com.caceres.libreria.entity.dto.SolicitudRecuperarContrasenaDto;
import com.caceres.libreria.entity.dto.SolicitudRestablecerContrasenaDto;
import com.caceres.libreria.service.AutenticacionService;
import com.caceres.libreria.service.RecuperacionContrasenaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/autenticacion")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AutenticacionController {

    private final AutenticacionService autenticacionServicio;
    private final RecuperacionContrasenaService recuperacionContrasenaServicio;

    @PostMapping("/IniciarSesion")
    public ResponseEntity<RespuestaTokenDto> iniciarSesion(@RequestBody SolicitudLoginDto solicitudLogin) {
        return ResponseEntity.ok(autenticacionServicio.iniciarSesion(solicitudLogin));
    }

    @PostMapping("/RecuperarContrasena")
public ResponseEntity<String> recuperarContrasena(
        @RequestBody SolicitudRecuperarContrasenaDto solicitudRecuperacion) {
    recuperacionContrasenaServicio.solicitarRecuperacion(solicitudRecuperacion);
    return ResponseEntity.ok("Se ha enviado un enlace de recuperacion a tu correo electronico");
}

    @GetMapping("/ValidarToken")
    public ResponseEntity<Boolean> validarToken(@RequestParam String token) {
        return ResponseEntity.ok(recuperacionContrasenaServicio.validarToken(token));
    }

    @PostMapping("/RestablecerContrasena")
    public ResponseEntity<String> restablecerContrasena(@RequestBody SolicitudRestablecerContrasenaDto solicitudRestablecimiento) {
        recuperacionContrasenaServicio.restablecerContrasena(solicitudRestablecimiento);
        return ResponseEntity.ok("Contrasena restablecida correctamente");
    }
}
package com.caceres.libreria.entity.dto;

import lombok.Data;

@Data
public class SolicitudLoginDto {
    private String nombreUsuario;
    private String contrasena;
}
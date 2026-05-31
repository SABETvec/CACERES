package com.caceres.libreria.entity.dto;

import lombok.Data;

@Data
public class SolicitudRestablecerContrasenaDto {
    private String token;
    private String nuevaContrasena;
}
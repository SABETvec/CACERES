package com.caceres.libreria.entity.dto;

import lombok.Data;

@Data
public class RespuestaTokenDto {
    private String tokenJwt;
    private String nombreUsuario;
    private String nombre;
    private String apellidoPaterno;
    private String fotoPerfil;
}
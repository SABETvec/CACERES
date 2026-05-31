package com.caceres.libreria.entity.dto;

import lombok.Data;

@Data
public class RolAccesoDto {
    private Integer idRolAcceso;
    private Integer idRol;
    private Integer idModulo;
    private String nombreModulo;
    private Integer idPermiso;
    private String nombrePermiso;
}
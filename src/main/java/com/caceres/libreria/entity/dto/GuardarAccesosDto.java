package com.caceres.libreria.entity.dto;

import lombok.Data;
import java.util.List;

@Data
public class GuardarAccesosDto {
    private Integer idRol;
    private List<AccesoItemDto> accesos;

    @Data
    public static class AccesoItemDto {
        private Integer idModulo;
        private Integer idPermiso;
    }
}
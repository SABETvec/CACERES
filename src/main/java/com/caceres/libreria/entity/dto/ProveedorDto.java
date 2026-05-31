package com.caceres.libreria.entity.dto;

import lombok.Data;
import java.time.Instant;
import java.time.LocalDate;

@Data
public class ProveedorDto {
    private Integer idProveedor;
    private String descripcion;
    private Integer idPersona;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String sexo;
    private LocalDate fechaDeNacimiento;
    private String fotoPerfil;
    private Instant fechaCreacion;
    private Instant fechaActualizacion;
    private Integer idContacto;
    private String correoElectronico;
    private String celular;
}
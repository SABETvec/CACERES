package com.caceres.libreria.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.Instant;
import java.time.LocalDate;

@Data
public class UsuarioDto {
    private Integer idUsuario;
    private String nombreUsuario;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String contrasena;

    private Boolean activo;
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
    private Integer idDireccion;
    private String pais;
    private String ciudad;
    private String zona;
    private String direccion;
    private String referencia;
    private Integer idRol;
    private String nombreRol;
    private Integer idUsuarioRol;
}
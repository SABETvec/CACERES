package com.caceres.libreria.entity.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
public class ClienteDto {
    private Integer idCliente;
    private BigDecimal saldo;
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
}
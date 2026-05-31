package com.caceres.libreria.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "persona")
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpersona", nullable = false)
    private Integer id;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "apellidopaterno", length = 100)
    private String apellidopaterno;

    @Column(name = "apellidomaterno", length = 100)
    private String apellidomaterno;

    @Column(name = "sexo", length = Integer.MAX_VALUE)
    private String sexo;

    @Column(name = "fechadenacimiento")
    private LocalDate fechadenacimiento;

    @Column(name = "fechacreacion")
    private Instant fechacreacion;

    @Column(name = "fechaactualizacion")
    private Instant fechaactualizacion;

    @Column(name = "fotoperfil", length = Integer.MAX_VALUE)
    private String fotoperfil;


}
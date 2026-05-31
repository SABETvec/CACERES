package com.caceres.libreria.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "reporte")
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idreporte", nullable = false)
    private Integer id;

    @Column(name = "tipo", length = 50)
    private String tipo;

    @Column(name = "fechainicio")
    private Instant fechainicio;

    @Column(name = "fechafin")
    private Instant fechafin;

    @Column(name = "fechagenerado")
    private Instant fechagenerado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idusuario")
    private Usuario idusuario;


}
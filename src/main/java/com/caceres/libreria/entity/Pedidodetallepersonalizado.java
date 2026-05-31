package com.caceres.libreria.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pedidodetallepersonalizado")
public class Pedidodetallepersonalizado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpersonalizado", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iddetalle")
    private Pedidodetalle iddetalle;

    @Column(name = "tipotrabajo", length = 50)
    private String tipotrabajo;

    @Column(name = "colortapafrontal", length = 50)
    private String colortapafrontal;

    @Column(name = "colortapaposterior", length = 50)
    private String colortapaposterior;

    @Column(name = "cantidadhojas")
    private Integer cantidadhojas;

    @Column(name = "tamano", length = 50)
    private String tamano;

    @Column(name = "observacion", length = Integer.MAX_VALUE)
    private String observacion;


}
package com.caceres.libreria.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "movimientoinventario")
public class Movimientoinventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmovimiento", nullable = false)
    private Integer id;

    @Column(name = "tipo", length = 50)
    private String tipo;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "fecha")
    private Instant fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idproductounidad")
    private Productounidad idproductounidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idusuario")
    private Usuario idusuario;


}
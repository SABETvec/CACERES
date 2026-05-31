package com.caceres.libreria.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "productounidad")
public class Productounidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idproductounidad", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idproducto")
    private Producto idproducto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idunidad")
    private Unidadmedida idunidad;

    @Column(name = "equivalencia")
    private Integer equivalencia;

    @Column(name = "precio", precision = 12, scale = 2)
    private BigDecimal precio;


}
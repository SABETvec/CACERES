package com.caceres.libreria.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "pedidodetalle")
public class Pedidodetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddetalle", nullable = false)
    private Integer id;

    @Column(name = "tipo", length = 20)
    private String tipo;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "precio", precision = 12, scale = 2)
    private BigDecimal precio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idpedido")
    private Pedido idpedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idproductounidad")
    private Productounidad idproductounidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idservicio")
    private Servicio idservicio;


}
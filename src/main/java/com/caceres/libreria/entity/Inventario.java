package com.caceres.libreria.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "inventario")
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idinventario", nullable = false)
    private Integer id;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "stockminimo")
    private Integer stockminimo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idproductounidad")
    private Productounidad idproductounidad;


}
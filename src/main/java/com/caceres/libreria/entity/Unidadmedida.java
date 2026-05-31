package com.caceres.libreria.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "unidadmedida")
public class Unidadmedida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idunidad", nullable = false)
    private Integer id;

    @Column(name = "nombre", length = 50)
    private String nombre;


}
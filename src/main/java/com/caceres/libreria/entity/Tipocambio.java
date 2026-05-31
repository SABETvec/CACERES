package com.caceres.libreria.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tipocambio")
public class Tipocambio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipocambio", nullable = false)
    private Integer id;

    @Column(name = "valor", precision = 12, scale = 4)
    private BigDecimal valor;

    @ColumnDefault("now()")
    @Column(name = "fecha")
    private Instant fecha;


}
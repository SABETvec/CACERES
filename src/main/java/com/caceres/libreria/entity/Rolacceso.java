package com.caceres.libreria.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rolacceso")
public class Rolacceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idrolacceso")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idrol")
    private Rol idrol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idmodulo")
    private Modulo idmodulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idpermiso")
    private Permiso idpermiso;
}
package com.caceres.libreria.repository;

import com.caceres.libreria.entity.Direccion;
import com.caceres.libreria.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Integer> {
    Optional<Direccion> findByIdpersona(Persona persona);
}
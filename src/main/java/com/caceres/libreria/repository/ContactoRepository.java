package com.caceres.libreria.repository;

import com.caceres.libreria.entity.Contacto;
import com.caceres.libreria.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Integer> {
    Optional<Contacto> findByIdpersona(Persona persona);
    Optional<Contacto> findByEmail(String correoElectronico);
}
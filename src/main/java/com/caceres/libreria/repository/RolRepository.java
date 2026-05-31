package com.caceres.libreria.repository;

import com.caceres.libreria.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    boolean existsByNombre(String nombre);
}
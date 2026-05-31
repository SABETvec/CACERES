package com.caceres.libreria.repository;

import com.caceres.libreria.entity.Usuariorol;
import com.caceres.libreria.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRolRepository extends JpaRepository<Usuariorol, Integer> {
    Optional<Usuariorol> findByIdusuario(Usuario usuario);
}
package com.caceres.libreria.repository;

import com.caceres.libreria.entity.Persona;
import com.caceres.libreria.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String nombreUsuario);
    boolean existsByUsername(String nombreUsuario);

    @Query("SELECT u FROM Usuario u WHERE u.idpersona = :persona")
    Optional<Usuario> buscarPorPersona(@Param("persona") Persona persona);
}
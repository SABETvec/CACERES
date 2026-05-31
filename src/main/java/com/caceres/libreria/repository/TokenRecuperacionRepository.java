package com.caceres.libreria.repository;

import com.caceres.libreria.entity.TokenRecuperacion;
import com.caceres.libreria.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
public interface TokenRecuperacionRepository extends JpaRepository<TokenRecuperacion, Integer> {
    Optional<TokenRecuperacion> findByToken(String token);

    @Modifying
    @Transactional
    void deleteByUsuario(Usuario usuario);
}
package com.caceres.libreria.repository;

import com.caceres.libreria.entity.Rolacceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface RolAccesoRepository extends JpaRepository<Rolacceso, Integer> {
    List<Rolacceso> findByIdrol_Id(Integer idRol);

    @Modifying
    @Transactional
    void deleteByIdrol_Id(Integer idRol);
}
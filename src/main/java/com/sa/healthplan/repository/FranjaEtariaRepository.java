package com.sa.healthplan.repository;

import com.sa.healthplan.model.FranjaEtaria;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranjaEtariaRepository extends JpaRepository<FranjaEtaria, Long> {

    Optional<FranjaEtaria> findByNombre(String nombre);
}

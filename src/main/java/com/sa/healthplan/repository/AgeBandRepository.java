package com.sa.healthplan.repository;

import com.sa.healthplan.model.AgeBand;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgeBandRepository extends JpaRepository<AgeBand, Long> {

    Optional<AgeBand> findByName(String name);
}

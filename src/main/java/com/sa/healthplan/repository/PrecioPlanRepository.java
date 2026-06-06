package com.sa.healthplan.repository;

import com.sa.healthplan.model.PrecioPlan;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrecioPlanRepository extends JpaRepository<PrecioPlan, Long> {

    List<PrecioPlan> findByPlanId(Long planId);
}

package com.sa.healthplan.repository;

import com.sa.healthplan.model.PlanPrice;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanPriceRepository extends JpaRepository<PlanPrice, Long> {

    List<PlanPrice> findByPlanId(Long planId);
}

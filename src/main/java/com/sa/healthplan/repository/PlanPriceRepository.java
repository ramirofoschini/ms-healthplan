package com.sa.healthplan.repository;

import com.sa.healthplan.model.PlanPrice;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanPriceRepository extends JpaRepository<PlanPrice, Long> {

    List<PlanPrice> findByPlanId(Long planId);

    /**
     * Precios vigentes de un plan para una franja a una fecha dada, del más
     * reciente al más antiguo. El motor de tasación toma el primero.
     */
    @Query("""
            SELECT p FROM PlanPrice p
            WHERE p.plan.id = :planId
              AND p.ageBand.id = :ageBandId
              AND p.validFrom <= :date
              AND (p.validTo IS NULL OR p.validTo >= :date)
            ORDER BY p.validFrom DESC
            """)
    List<PlanPrice> findValid(@Param("planId") Long planId,
                              @Param("ageBandId") Long ageBandId,
                              @Param("date") LocalDate date);
}

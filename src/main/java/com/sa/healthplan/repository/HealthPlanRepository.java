package com.sa.healthplan.repository;

import com.sa.healthplan.model.HealthPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthPlanRepository extends JpaRepository<HealthPlan, Long> {

    @Query(
            value = "SELECT * FROM health_plan WHERE clinics ILIKE CONCAT('%', :filter, '%') "
                    + "OR comments ILIKE CONCAT('%', :filter, '%')",
            countQuery = "SELECT count(*) FROM health_plan WHERE clinics ILIKE CONCAT('%', :filter, '%') "
                    + "OR comments ILIKE CONCAT('%', :filter, '%')",
            nativeQuery = true
    )
    Page<HealthPlan> search(@Param("filter") String filter, Pageable pageable);
}

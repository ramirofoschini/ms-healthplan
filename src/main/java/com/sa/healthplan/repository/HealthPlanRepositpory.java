package com.sa.healthplan.repository;

import com.sa.healthplan.model.HealthPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

@Repository
public interface HealthPlanRepositpory extends BaseRepository<HealthPlan, Long> {

//Paginas
    Page<HealthPlan> findByClinicsContainingOrCommentsContaining(@Param("clinics") String clinics, @Param("comments") String comments, Pageable pageable);

    @Query(value = "SELECT a FROM HealthPlan a WHERE a.clinics LIKE %:filter% OR a.comments LIKE %:filter%")
    Page<HealthPlan> search(@Param("filter") String filter, Pageable pageable);

    @Query(
            value = "SELECT * FROM health_plan.health_plan WHERE clinics LIKE %:filter% OR comments LIKE %:filter%",
            countQuery = "SELECT count(*) FROM health_plan",
            nativeQuery = true
    )
    Page<HealthPlan> searchNative(@Param("filter") String filter, Pageable pageable);


    /* @Modifying
    @Transactional
    @Query(value = "UPDATE HealthPlan SET nameplan = :nameplan WHERE id = :id")

    void updateNamePlan(@Param ("id") Long id, @Param ("nameplan") String nameplan);
    
     */
}

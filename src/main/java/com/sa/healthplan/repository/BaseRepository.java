package com.sa.healthplan.repository;

import com.sa.healthplan.model.Base;
import com.sa.healthplan.model.HealthPlan;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;




@NoRepositoryBean
public interface BaseRepository<E extends HealthPlan, ID extends Serializable> extends JpaRepository<E, ID> {
    
    @Modifying
    @Query("UPDATE HealthPlan SET nameplan = :nameplan WHERE id = :id")
    
    public E updateNamePlan(@Param("id") ID id, @Param("nameplan") String nameplan);
} 

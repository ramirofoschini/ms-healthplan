package com.sa.healthplan.service;

import com.sa.healthplan.model.HealthPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HealthPlanService extends BaseService<HealthPlan, Long> {

    Page<HealthPlan> search(String filter, Pageable pageable) throws Exception;

    //public HealthPlan updateNamePlan (Long id, String nameplan)throws Exception;
}

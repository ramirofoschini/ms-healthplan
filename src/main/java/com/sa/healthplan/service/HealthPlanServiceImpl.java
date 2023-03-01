package com.sa.healthplan.service;

import com.sa.healthplan.model.HealthPlan;

import com.sa.healthplan.repository.HealthPlanRepositpory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HealthPlanServiceImpl extends BaseServiceImpl<HealthPlan, Long> implements HealthPlanService {

    @Autowired
    private HealthPlanRepositpory healthPlanRepositpory;

    @Override
    public Page<HealthPlan> search(String filter, Pageable pageable) throws Exception {
        try {

            Page<HealthPlan> entities = healthPlanRepositpory.searchNative(filter, pageable);
            return entities;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}

package com.sa.healthplan.service;

import com.sa.healthplan.model.HealthPlan;

import com.sa.healthplan.repository.HealthPlanRepositpory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class HealthPlanServiceImpl extends BaseServiceImpl<HealthPlan, Long> implements HealthPlanService {

    @Autowired
    private HealthPlanRepositpory healthPlanRepositpory;

    
}

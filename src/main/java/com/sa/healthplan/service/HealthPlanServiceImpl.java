package com.sa.healthplan.service;

import com.sa.healthplan.model.HealthPlan;
import com.sa.healthplan.repository.HealthPlanRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HealthPlanServiceImpl extends BaseServiceImpl<HealthPlan, Long> implements HealthPlanService {

    @Autowired
    private HealthPlanRepository healthPlanRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<HealthPlan> search(String filter, Pageable pageable) {
        return healthPlanRepository.searchNative(filter, pageable);
    }

}

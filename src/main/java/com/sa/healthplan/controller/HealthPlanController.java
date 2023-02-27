
package com.sa.healthplan.controller;

import com.sa.healthplan.model.HealthPlan;

import com.sa.healthplan.service.HealthPlanServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin("*")
@RequestMapping("api/v1")
public class HealthPlanController extends BaseControllerImpl <HealthPlan, HealthPlanServiceImpl> {
    
    @Autowired
    private HealthPlanServiceImpl healthPlanService;
}

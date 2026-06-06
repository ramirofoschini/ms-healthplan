package com.sa.healthplan.controller;

import com.sa.healthplan.model.HealthPlan;

import com.sa.healthplan.service.HealthPlanServiceImpl;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("api/v1")

public class HealthPlanController extends BaseControllerImpl<HealthPlan, HealthPlanServiceImpl> {

    @Autowired
    private HealthPlanServiceImpl healthPlanServiceImpl;

   
    @Operation(summary = "Devuelve las entidades paginadas y filtradas")
    @GetMapping("/searchPaged")
    public ResponseEntity<?> search(@RequestParam String filter, Pageable pageable) {
        return ResponseEntity.ok(healthPlanServiceImpl.search(filter, pageable));
    }

}

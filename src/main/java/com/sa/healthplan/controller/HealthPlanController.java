package com.sa.healthplan.controller;

import com.sa.healthplan.model.HealthPlan;

import com.sa.healthplan.service.HealthPlanServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("api/v1/healthPlans")
public class HealthPlanController extends BaseControllerImpl<HealthPlan, HealthPlanServiceImpl> {

    @Autowired
    private HealthPlanServiceImpl healthPlanServiceImpl;

    @GetMapping("/searchPaged")
    public ResponseEntity<?> search(@RequestParam String filter, Pageable pageable) {

        try {
            return ResponseEntity.status(HttpStatus.OK).body(healthPlanServiceImpl.search(filter, pageable));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"error\": \"" + e.getMessage() + "\"}"));
        }
    }
    /*
    @PatchMapping("/{id}")
    
    public ResponseEntity<?> updateNamePlan(@PathVariable Long id, @RequestBody HealthPlan entity) {
        try {

            HealthPlan hp = healthPlanServiceImpl.updateNamePlan(id, entity.getNameplan());
            
            return ResponseEntity.status(HttpStatus.OK).body(hp);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"404\"}");
        }
    }
     */

}

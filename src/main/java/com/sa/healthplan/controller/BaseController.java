package com.sa.healthplan.controller;

import com.sa.healthplan.model.Base;
import com.sa.healthplan.model.HealthPlan;

import java.io.Serializable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


public interface BaseController <E extends HealthPlan, ID extends Serializable> {
    
    public ResponseEntity<?> getAll();
    public ResponseEntity<?> getOne(@PathVariable ID id);
    public ResponseEntity<?> save(@RequestBody E entity);
    public ResponseEntity<?> update(@PathVariable ID id, @RequestBody E entity);
    public ResponseEntity<?> delete(@PathVariable ID id);
    public ResponseEntity<?> updateNamePlan (@PathVariable ID id, @RequestBody E entity);
}

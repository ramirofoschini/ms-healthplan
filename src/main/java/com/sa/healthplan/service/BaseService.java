
package com.sa.healthplan.service;

import com.sa.healthplan.model.Base;
import com.sa.healthplan.model.HealthPlan;

import java.io.Serializable;
import java.util.List;



    public interface BaseService<E extends HealthPlan, ID extends Serializable> {
        
        
    public List<E> findAll() throws Exception;
    public E findById(ID id)throws Exception;
    public E save(E entity)throws Exception;
    public E update(ID id, E entity)throws Exception;
    public boolean delete(ID id)throws Exception;
    public E updateNamePlan (ID id, String nameplan)throws Exception;
}



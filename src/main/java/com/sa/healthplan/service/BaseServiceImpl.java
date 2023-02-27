package com.sa.healthplan.service;

import com.sa.healthplan.model.Base;
import com.sa.healthplan.model.HealthPlan;

import com.sa.healthplan.repository.BaseRepository;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseServiceImpl<E extends HealthPlan, ID extends Serializable> implements BaseService<E, ID> {

    @Autowired
    protected BaseRepository<E, ID> baseRepository;

    @Override
    public List<E> findAll() throws Exception {
        try {
            List<E> entities = baseRepository.findAll();
            return entities;
        } catch (Exception e) {
            throw new Exception(e.getMessage());

        }
    }

    @Override
    public E findById(ID id) throws Exception {
        try {
            Optional<E> entiti = baseRepository.findById(id);

            return entiti.get();

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public E save(E entity) throws Exception {
        try {

            return baseRepository.save(entity);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public E update(ID id, E entity) throws Exception {
        try {
            Optional<E> op = baseRepository.findById(id);
            E healthPlan = op.get();

            healthPlan = baseRepository.save(entity);
            return healthPlan;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public boolean delete(ID id) throws Exception {
        try {
            if (baseRepository.existsById(id)) {

                baseRepository.deleteById(id);

                return true;
            } else {
                throw new Exception();
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public E updateNamePlan(ID id, String nameplan) throws Exception {
        try {

            baseRepository.updateNamePlan(id, nameplan);
            E e = baseRepository.findById(id).get();

            return e;
            
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}

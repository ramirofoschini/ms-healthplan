package com.sa.healthplan.service;

import com.sa.healthplan.model.Base;

import com.sa.healthplan.repository.BaseRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public abstract class BaseServiceImpl<E extends Base, ID extends Serializable> implements BaseService<E, ID> {

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

            E healthPlan = baseRepository.findById(id).get();

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
    public Page<E> findAll(Pageable pageable) throws Exception {
        try {
            Page<E> entities = baseRepository.findAll(pageable);
            return entities;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}

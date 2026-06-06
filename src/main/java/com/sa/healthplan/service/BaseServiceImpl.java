package com.sa.healthplan.service;

import com.sa.healthplan.exception.ResourceNotFoundException;
import com.sa.healthplan.model.Base;
import com.sa.healthplan.repository.BaseRepository;

import java.io.Serializable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public abstract class BaseServiceImpl<E extends Base, ID extends Serializable> implements BaseService<E, ID> {

    @Autowired
    protected BaseRepository<E, ID> baseRepository;

    @Override
    @Transactional(readOnly = true)
    public List<E> findAll() {
        return baseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<E> findAll(Pageable pageable) {
        return baseRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public E findById(ID id) {
        return baseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("el recurso", id));
    }

    @Override
    @Transactional
    public E save(E entity) {
        return baseRepository.save(entity);
    }

    @Override
    @Transactional
    public E update(ID id, E entity) {
        if (!baseRepository.existsById(id)) {
            throw new ResourceNotFoundException("el recurso", id);
        }
        return baseRepository.save(entity);
    }

    @Override
    @Transactional
    public boolean delete(ID id) {
        if (!baseRepository.existsById(id)) {
            throw new ResourceNotFoundException("el recurso", id);
        }
        baseRepository.deleteById(id);
        return true;
    }

}

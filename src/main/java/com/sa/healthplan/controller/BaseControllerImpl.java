package com.sa.healthplan.controller;

import com.sa.healthplan.model.Base;
import com.sa.healthplan.service.BaseServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public abstract class BaseControllerImpl<E extends Base, S extends BaseServiceImpl<E, Long>> implements BaseController<E, Long> {

    @Autowired
    protected S service;

    private static final Logger log = LoggerFactory.getLogger(BaseControllerImpl.class);

    @Operation(summary = "Devuelve todas las entidades")
    @GetMapping("/healthPlans")
    @Override
    public ResponseEntity<?> getAll() {
        log.info("getAll");
        List<E> listHp = service.findAll();

        if (listHp.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // HATEOAS: enlaces self/colección por cada recurso
        for (E hp : listHp) {
            hp.add(linkTo(methodOn(HealthPlanController.class).getOne(hp.getId())).withSelfRel());
            hp.add(linkTo(methodOn(HealthPlanController.class).getAll()).withRel(IanaLinkRelations.COLLECTION));
        }

        CollectionModel<E> model = CollectionModel.of(listHp);
        model.add(linkTo(methodOn(HealthPlanController.class).getAll()).withSelfRel());

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Operation(summary = "Devuelve una entidad por ID")
    @GetMapping("/healthPlan/{id}")
    @Override
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        E hp = service.findById(id);

        hp.add(linkTo(methodOn(HealthPlanController.class).getOne(hp.getId())).withSelfRel());
        hp.add(linkTo(methodOn(HealthPlanController.class).getAll()).withRel(IanaLinkRelations.COLLECTION));

        return new ResponseEntity<>(hp, HttpStatus.OK);
    }

    @Operation(summary = "Crea una nueva entidad")
    @PostMapping("/healthPlan")
    @Override
    public ResponseEntity<?> save(@RequestBody E entity) {
        E saved = service.save(entity);

        saved.add(linkTo(methodOn(HealthPlanController.class).getOne(saved.getId())).withSelfRel());
        saved.add(linkTo(methodOn(HealthPlanController.class).getAll()).withRel(IanaLinkRelations.COLLECTION));

        return ResponseEntity.created(
                        linkTo(methodOn(HealthPlanController.class).getOne(saved.getId())).toUri())
                .body(saved);
    }

    @Operation(summary = "Modifica una entidad por ID")
    @PutMapping("/healthPlan/{id}")
    @Override
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody E entity) {
        // Forzamos el ID de la URI para no crear una entidad nueva por error
        entity.setId(id);
        service.update(id, entity);

        entity.add(linkTo(methodOn(HealthPlanController.class).getOne(entity.getId())).withSelfRel());
        entity.add(linkTo(methodOn(HealthPlanController.class).getAll()).withRel(IanaLinkRelations.COLLECTION));

        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @Operation(summary = "Elimina una entidad por ID")
    @DeleteMapping("/healthPlan/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Devuelve las entidades paginadas")
    @GetMapping("/paged")
    @Override
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

}

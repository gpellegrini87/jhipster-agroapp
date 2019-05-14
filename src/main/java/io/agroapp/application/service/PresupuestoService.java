package io.agroapp.application.service;

import io.agroapp.application.domain.Presupuesto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Presupuesto}.
 */
public interface PresupuestoService {

    /**
     * Save a presupuesto.
     *
     * @param presupuesto the entity to save.
     * @return the persisted entity.
     */
    Presupuesto save(Presupuesto presupuesto);

    /**
     * Get all the presupuestos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Presupuesto> findAll(Pageable pageable);


    /**
     * Get the "id" presupuesto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Presupuesto> findOne(Long id);

    /**
     * Delete the "id" presupuesto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

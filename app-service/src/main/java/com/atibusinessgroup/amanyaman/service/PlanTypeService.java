package com.atibusinessgroup.amanyaman.service;

import com.atibusinessgroup.amanyaman.domain.PlanType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link PlanType}.
 */
public interface PlanTypeService {

    /**
     * Save a PlanType.
     *
     * @param PlanType the entity to save.
     * @return the persisted entity.
     */
    PlanType save(PlanType PlanType);

    /**
     * Get all the cities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlanType> findAll(Pageable pageable);


    /**
     * Get the "id" PlanType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlanType> findOne(Long id);

    /**
     * Delete the "id" PlanType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

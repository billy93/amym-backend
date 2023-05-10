package com.atibusinessgroup.amanyaman.service;

import com.atibusinessgroup.amanyaman.domain.SystemParameter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link SystemParameter}.
 */
public interface SystemParameterService {

    /**
     * Save a systemParameter.
     *
     * @param systemParameter the entity to save.
     * @return the persisted entity.
     */
    SystemParameter save(SystemParameter systemParameter);

    /**
     * Get all the systemParameters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SystemParameter> findAll(Pageable pageable);


    /**
     * Get the "id" systemParameter.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SystemParameter> findOne(Long id);

    /**
     * Get the "name" systemParameter.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SystemParameter> findOneByName(String name);

    /**
     * Delete the "id" systemParameter.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

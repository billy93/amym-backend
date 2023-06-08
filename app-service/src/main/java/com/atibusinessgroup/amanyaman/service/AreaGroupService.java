package com.atibusinessgroup.amanyaman.service;

import com.atibusinessgroup.amanyaman.domain.AreaGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link AreaGroup}.
 */
public interface AreaGroupService {

    /**
     * Save a areaGroup.
     *
     * @param areaGroup the entity to save.
     * @return the persisted entity.
     */
    AreaGroup save(AreaGroup areaGroup);

    /**
     * Get all the areaGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AreaGroup> findAll(Pageable pageable);


    /**
     * Get the "id" areaGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AreaGroup> findOne(Long id);

    /**
     * Delete the "id" areaGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

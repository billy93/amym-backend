package com.atibusinessgroup.amanyaman.service;

import com.atibusinessgroup.amanyaman.domain.Area;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Area}.
 */
public interface AreaService {

    /**
     * Save a area.
     *
     * @param area the entity to save.
     * @return the persisted entity.
     */
    Area save(Area area);

    /**
     * Get all the areas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Area> findAll(Pageable pageable);


    /**
     * Get the "id" area.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Area> findOne(Long id);

    /**
     * Delete the "id" area.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

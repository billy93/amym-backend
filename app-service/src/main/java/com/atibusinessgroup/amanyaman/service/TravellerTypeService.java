package com.atibusinessgroup.amanyaman.service;

import com.atibusinessgroup.amanyaman.domain.TravellerType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link TravellerType}.
 */
public interface TravellerTypeService {

    /**
     * Save a TravellerType.
     *
     * @param TravellerType the entity to save.
     * @return the persisted entity.
     */
    TravellerType save(TravellerType TravellerType);

    /**
     * Get all the cities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TravellerType> findAll(Pageable pageable);


    /**
     * Get the "id" TravellerType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TravellerType> findOne(Long id);

    /**
     * Delete the "id" TravellerType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

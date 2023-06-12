package com.atibusinessgroup.amanyaman.service;

import com.atibusinessgroup.amanyaman.domain.BandType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link BandType}.
 */
public interface BandTypeService {

    /**
     * Save a BandType.
     *
     * @param BandType the entity to save.
     * @return the persisted entity.
     */
    BandType save(BandType BandType);

    /**
     * Get all the cities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BandType> findAll(Pageable pageable);


    /**
     * Get the "id" BandType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BandType> findOne(Long id);

    /**
     * Delete the "id" BandType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

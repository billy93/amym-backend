package com.atibusinessgroup.amanyaman.service;

import com.atibusinessgroup.amanyaman.domain.Authority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Country}.
 */
public interface AuthorityService {

    /**
     * Save a authority.
     *
     * @param authority the entity to save.
     * @return the persisted entity.
     */
    Authority save(Authority authority);

    /**
     * Get all the countries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Authority> findAll(Pageable pageable);


    /**
     * Get the "id" authority.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Authority> findOne(String id);

    /**
     * Delete the "id" authority.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}

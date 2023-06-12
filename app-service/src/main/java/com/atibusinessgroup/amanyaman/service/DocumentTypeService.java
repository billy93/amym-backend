package com.atibusinessgroup.amanyaman.service;

import com.atibusinessgroup.amanyaman.domain.DocumentType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link DocumentType}.
 */
public interface DocumentTypeService {

    /**
     * Save a DocumentType.
     *
     * @param DocumentType the entity to save.
     * @return the persisted entity.
     */
    DocumentType save(DocumentType DocumentType);

    /**
     * Get all the cities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentType> findAll(Pageable pageable);


    /**
     * Get the "id" DocumentType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentType> findOne(Long id);

    /**
     * Delete the "id" DocumentType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

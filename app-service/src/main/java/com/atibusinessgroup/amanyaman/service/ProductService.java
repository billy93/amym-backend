package com.atibusinessgroup.amanyaman.service;

import com.atibusinessgroup.amanyaman.domain.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Product}.
 */
public interface ProductService {

    /**
     * Save a Product.
     *
     * @param Product the entity to save.
     * @return the persisted entity.
     */
    Product save(Product Product);

    /**
     * Get all the cities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Product> findAll(Pageable pageable);


    /**
     * Get the "id" Product.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Product> findOne(Long id);

    /**
     * Delete the "id" Product.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

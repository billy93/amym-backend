package com.atibusinessgroup.amanyaman.service;

import com.atibusinessgroup.amanyaman.domain.ProductTravelAgent;
import com.atibusinessgroup.amanyaman.web.rest.dto.ProductTravelAgentSearchRequestDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link ProductTravelAgent}.
 */
public interface ProductTravelAgentService {

    /**
     * Save a ProductTravelAgent.
     *
     * @param ProductTravelAgent the entity to save.
     * @return the persisted entity.
     */
    ProductTravelAgent save(ProductTravelAgent ProductTravelAgent);

    /**
     * Get all the cities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductTravelAgent> findAll(Pageable pageable);


    /**
     * Get the "id" ProductTravelAgent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductTravelAgent> findOne(Long id);

    /**
     * Delete the "id" ProductTravelAgent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<ProductTravelAgent> findAllBy(ProductTravelAgentSearchRequestDTO productTravelAgentSearchRequestDTO,
            Pageable pageable);
}

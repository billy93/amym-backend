package com.atibusinessgroup.amanyaman.web.rest;

import com.atibusinessgroup.amanyaman.domain.TravellerType;
import com.atibusinessgroup.amanyaman.service.TravellerTypeService;
import com.atibusinessgroup.amanyaman.util.HeaderUtil;
import com.atibusinessgroup.amanyaman.util.PaginationUtil;
import com.atibusinessgroup.amanyaman.util.ResponseUtil;
import com.atibusinessgroup.amanyaman.web.rest.errors.BadRequestAlertException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.atibusinessgroup.amanyaman.domain.TravellerType}.
 */
@RestController
@RequestMapping("/api/app")
public class TravellerTypeResource {

    private final Logger log = LoggerFactory.getLogger(TravellerTypeResource.class);

    private static final String ENTITY_NAME = "TravellerType";

    private String applicationName ="AMANYAMAN";

    private final TravellerTypeService TravellerTypeService;

    public TravellerTypeResource(TravellerTypeService TravellerTypeService) {
        this.TravellerTypeService = TravellerTypeService;
    }

    /**
     * {@code POST  /traveller-types} : Create a new TravellerType.
     *
     * @param TravellerType the TravellerType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new TravellerType, or with status {@code 400 (Bad Request)} if the TravellerType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/traveller-types")
    public ResponseEntity<TravellerType> createTravellerType(@Valid @RequestBody TravellerType TravellerType) throws URISyntaxException {
        log.debug("REST request to save TravellerType : {}", TravellerType);
        if (TravellerType.getId() != null) {
            throw new BadRequestAlertException("A new TravellerType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TravellerType result = TravellerTypeService.save(TravellerType);
        return ResponseEntity.created(new URI("/api/traveller-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /traveller-types} : Updates an existing TravellerType.
     *
     * @param TravellerType the TravellerType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated TravellerType,
     * or with status {@code 400 (Bad Request)} if the TravellerType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the TravellerType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/traveller-types")
    public ResponseEntity<TravellerType> updateTravellerType(@Valid @RequestBody TravellerType TravellerType) throws URISyntaxException {
        log.debug("REST request to update TravellerType : {}", TravellerType);
        if (TravellerType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TravellerType result = TravellerTypeService.save(TravellerType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, TravellerType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /traveller-types} : get all the traveller-types.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of traveller-types in body.
     */
    @GetMapping("/traveller-types")
    public ResponseEntity<List<TravellerType>> getAll(Pageable pageable) {
        log.debug("REST request to get Plan Types");
        Page<TravellerType> page = TravellerTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /traveller-types/count} : count all the traveller-types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    // @GetMapping("/traveller-types/count")
    // public ResponseEntity<Long> countCities(TravellerTypeCriteria criteria) {
    //     log.debug("REST request to count Cities by criteria: {}", criteria);
    //     return ResponseEntity.ok().body(TravellerTypeQueryService.countByCriteria(criteria));
    // }

    /**
     * {@code GET  /traveller-types/:id} : get the "id" TravellerType.
     *
     * @param id the id of the TravellerType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the TravellerType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/traveller-types/{id}")
    public ResponseEntity<TravellerType> getOne(@PathVariable Long id) {
        log.debug("REST request to get TravellerType : {}", id);
        Optional<TravellerType> TravellerType = TravellerTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(TravellerType);
    }

    /**
     * {@code DELETE  /traveller-types/:id} : delete the "id" TravellerType.
     *
     * @param id the id of the TravellerType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/traveller-types/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable Long id) {
        log.debug("REST request to delete TravellerType : {}", id);
        TravellerTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

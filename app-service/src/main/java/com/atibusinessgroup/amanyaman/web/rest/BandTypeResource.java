package com.atibusinessgroup.amanyaman.web.rest;

import com.atibusinessgroup.amanyaman.domain.BandType;
import com.atibusinessgroup.amanyaman.service.BandTypeService;
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
 * REST controller for managing {@link com.atibusinessgroup.amanyaman.domain.BandType}.
 */
@RestController
@RequestMapping("/api/app")
public class BandTypeResource {

    private final Logger log = LoggerFactory.getLogger(BandTypeResource.class);

    private static final String ENTITY_NAME = "BandType";

    private String applicationName ="AMANYAMAN";

    private final BandTypeService BandTypeService;

    public BandTypeResource(BandTypeService BandTypeService) {
        this.BandTypeService = BandTypeService;
    }

    /**
     * {@code POST  /band-types} : Create a new BandType.
     *
     * @param BandType the BandType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new BandType, or with status {@code 400 (Bad Request)} if the BandType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/band-types")
    public ResponseEntity<BandType> createBandType(@Valid @RequestBody BandType BandType) throws URISyntaxException {
        log.debug("REST request to save BandType : {}", BandType);
        if (BandType.getId() != null) {
            throw new BadRequestAlertException("A new BandType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BandType result = BandTypeService.save(BandType);
        return ResponseEntity.created(new URI("/api/band-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /band-types} : Updates an existing BandType.
     *
     * @param BandType the BandType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated BandType,
     * or with status {@code 400 (Bad Request)} if the BandType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the BandType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/band-types")
    public ResponseEntity<BandType> updateBandType(@Valid @RequestBody BandType BandType) throws URISyntaxException {
        log.debug("REST request to update BandType : {}", BandType);
        if (BandType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BandType result = BandTypeService.save(BandType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, BandType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /band-types} : get all the band-types.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of band-types in body.
     */
    @GetMapping("/band-types")
    public ResponseEntity<List<BandType>> getAll(Pageable pageable) {
        log.debug("REST request to get Plan Types");
        Page<BandType> page = BandTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /band-types/count} : count all the band-types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    // @GetMapping("/band-types/count")
    // public ResponseEntity<Long> countCities(BandTypeCriteria criteria) {
    //     log.debug("REST request to count Cities by criteria: {}", criteria);
    //     return ResponseEntity.ok().body(BandTypeQueryService.countByCriteria(criteria));
    // }

    /**
     * {@code GET  /band-types/:id} : get the "id" BandType.
     *
     * @param id the id of the BandType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the BandType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/band-types/{id}")
    public ResponseEntity<BandType> getOne(@PathVariable Long id) {
        log.debug("REST request to get BandType : {}", id);
        Optional<BandType> BandType = BandTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(BandType);
    }

    /**
     * {@code DELETE  /band-types/:id} : delete the "id" BandType.
     *
     * @param id the id of the BandType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/band-types/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable Long id) {
        log.debug("REST request to delete BandType : {}", id);
        BandTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

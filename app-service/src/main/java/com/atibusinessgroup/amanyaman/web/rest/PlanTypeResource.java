package com.atibusinessgroup.amanyaman.web.rest;

import com.atibusinessgroup.amanyaman.domain.PlanType;
import com.atibusinessgroup.amanyaman.service.PlanTypeService;
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
 * REST controller for managing {@link com.atibusinessgroup.amanyaman.domain.PlanType}.
 */
@RestController
@RequestMapping("/api/app")
public class PlanTypeResource {

    private final Logger log = LoggerFactory.getLogger(PlanTypeResource.class);

    private static final String ENTITY_NAME = "PlanType";

    private String applicationName ="AMANYAMAN";

    private final PlanTypeService PlanTypeService;

    public PlanTypeResource(PlanTypeService PlanTypeService) {
        this.PlanTypeService = PlanTypeService;
    }

    /**
     * {@code POST  /plan-types} : Create a new PlanType.
     *
     * @param PlanType the PlanType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new PlanType, or with status {@code 400 (Bad Request)} if the PlanType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plan-types")
    public ResponseEntity<PlanType> createPlanType(@Valid @RequestBody PlanType PlanType) throws URISyntaxException {
        log.debug("REST request to save PlanType : {}", PlanType);
        if (PlanType.getId() != null) {
            throw new BadRequestAlertException("A new PlanType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlanType result = PlanTypeService.save(PlanType);
        return ResponseEntity.created(new URI("/api/plan-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plan-types} : Updates an existing PlanType.
     *
     * @param PlanType the PlanType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated PlanType,
     * or with status {@code 400 (Bad Request)} if the PlanType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the PlanType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plan-types")
    public ResponseEntity<PlanType> updatePlanType(@Valid @RequestBody PlanType PlanType) throws URISyntaxException {
        log.debug("REST request to update PlanType : {}", PlanType);
        if (PlanType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlanType result = PlanTypeService.save(PlanType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, PlanType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /plan-types} : get all the plan-types.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plan-types in body.
     */
    @GetMapping("/plan-types")
    public ResponseEntity<List<PlanType>> getAll(Pageable pageable) {
        log.debug("REST request to get Plan Types");
        Page<PlanType> page = PlanTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plan-types/count} : count all the plan-types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    // @GetMapping("/plan-types/count")
    // public ResponseEntity<Long> countCities(PlanTypeCriteria criteria) {
    //     log.debug("REST request to count Cities by criteria: {}", criteria);
    //     return ResponseEntity.ok().body(PlanTypeQueryService.countByCriteria(criteria));
    // }

    /**
     * {@code GET  /plan-types/:id} : get the "id" PlanType.
     *
     * @param id the id of the PlanType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the PlanType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plan-types/{id}")
    public ResponseEntity<PlanType> getOne(@PathVariable Long id) {
        log.debug("REST request to get PlanType : {}", id);
        Optional<PlanType> PlanType = PlanTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(PlanType);
    }

    /**
     * {@code DELETE  /plan-types/:id} : delete the "id" PlanType.
     *
     * @param id the id of the PlanType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plan-types/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable Long id) {
        log.debug("REST request to delete PlanType : {}", id);
        PlanTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

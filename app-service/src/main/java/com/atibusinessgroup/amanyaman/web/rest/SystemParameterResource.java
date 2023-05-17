package com.atibusinessgroup.amanyaman.web.rest;

import com.atibusinessgroup.amanyaman.domain.SystemParameter;
import com.atibusinessgroup.amanyaman.service.SystemParameterService;
import com.atibusinessgroup.amanyaman.web.rest.errors.BadRequestAlertException;

import lombok.RequiredArgsConstructor;
import com.atibusinessgroup.amanyaman.util.HeaderUtil;
import com.atibusinessgroup.amanyaman.util.PaginationUtil;
import com.atibusinessgroup.amanyaman.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.atibusinessgroup.amanyaman.domain.SystemParameter}.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/app")
public class SystemParameterResource {

    private final Logger log = LoggerFactory.getLogger(SystemParameterResource.class);

    private static final String ENTITY_NAME = "systemParameter";

    
    private String applicationName ="AMANYAMAN";

    @Autowired
    private SystemParameterService systemParameterService;

    /**
     * {@code POST  /system-parameters} : Create a new systemParameter.
     *
     * @param systemParameter the systemParameter to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemParameter, or with status {@code 400 (Bad Request)} if the systemParameter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.     
    */
    @PostMapping("/system-parameters")
    public ResponseEntity<SystemParameter> createSystemParameter(@RequestBody SystemParameter systemParameter) throws URISyntaxException {
        log.debug("REST request to save SystemParameter : {}", systemParameter);
        if (systemParameter.getId() != null) {
            throw new BadRequestAlertException("A new systemParameter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SystemParameter result = systemParameterService.save(systemParameter);
        return ResponseEntity.created(new URI("/api/system-parameters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    /**
     * {@code PUT  /system-parameters} : Updates an existing systemParameter.
     *
     * @param systemParameter the systemParameter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemParameter,
     * or with status {@code 400 (Bad Request)} if the systemParameter is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemParameter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.     
    */
    @PutMapping("/system-parameters")
    public ResponseEntity<SystemParameter> updateSystemParameter(@RequestBody SystemParameter systemParameter) throws URISyntaxException {
        log.debug("REST request to update SystemParameter : {}", systemParameter);
        if (systemParameter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SystemParameter result = systemParameterService.save(systemParameter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, systemParameter.getId().toString()))
            .body(result);
    }
    /**
     * {@code GET  /system-parameters} : get all the systemParameters.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemParameters in body.
     */
    @GetMapping("/system-parameters")
    public ResponseEntity<List<SystemParameter>> getAllSystemParameters(Pageable pageable) {
        Page<SystemParameter> page = systemParameterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);        
        return ResponseEntity.ok()
            .headers(headers)
            .body(page.getContent());
    }

    /**
     * {@code GET  /system-parameters/count} : count all the systemParameters.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    // @GetMapping("/system-parameters/count")
    // public ResponseEntity<Long> countSystemParameters(SystemParameterCriteria criteria) {
    //     log.debug("REST request to count SystemParameters by criteria: {}", criteria);
    //     return ResponseEntity.ok().body(systemParameterQueryService.countByCriteria(criteria));
    // }

    /**
     * {@code GET  /system-parameters/:id} : get the "id" systemParameter.
     *
     * @param id the id of the systemParameter to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemParameter, or with status {@code 404 (Not Found)}.
    */
    @GetMapping("/system-parameters/{id}")
    public ResponseEntity<SystemParameter> getSystemParameter(@PathVariable Long id) {
        log.debug("REST request to get SystemParameter : {}", id);
        Optional<SystemParameter> systemParameter = systemParameterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemParameter);
    }

    /**
     * {@code DELETE  /system-parameters/:id} : delete the "id" systemParameter.
     *
     * @param id the id of the systemParameter to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.    
     */
    @DeleteMapping("/system-parameters/{id}")
    public ResponseEntity<Void> deleteSystemParameter(@PathVariable Long id) {
        log.debug("REST request to delete SystemParameter : {}", id);
        systemParameterService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

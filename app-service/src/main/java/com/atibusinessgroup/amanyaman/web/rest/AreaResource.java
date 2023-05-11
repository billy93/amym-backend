package com.atibusinessgroup.amanyaman.web.rest;

import com.atibusinessgroup.amanyaman.domain.Area;
import com.atibusinessgroup.amanyaman.service.AreaService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.atibusinessgroup.amanyaman.domain.Area}.
 */
@RestController
@RequestMapping("/api/app")
public class AreaResource {

    private final Logger log = LoggerFactory.getLogger(AreaResource.class);

    private static final String ENTITY_NAME = "area";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AreaService areaService;


    public AreaResource(AreaService areaService) {
        this.areaService = areaService;
    }

    /**
     * {@code POST  /areas} : Create a new area.
     *
     * @param area the area to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new area, or with status {@code 400 (Bad Request)} if the area has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/areas")
    public ResponseEntity<Area> createArea(@Valid @RequestBody Area area) throws URISyntaxException {
        log.debug("REST request to save Area : {}", area);
        if (area.getId() != null) {
            throw new BadRequestAlertException("A new area cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Area result = areaService.save(area);
        return ResponseEntity.created(new URI("/api/areas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /areas} : Updates an existing area.
     *
     * @param area the area to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated area,
     * or with status {@code 400 (Bad Request)} if the area is not valid,
     * or with status {@code 500 (Internal Server Error)} if the area couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/areas")
    public ResponseEntity<Area> updateArea(@Valid @RequestBody Area area) throws URISyntaxException {
        log.debug("REST request to update Area : {}", area);
        if (area.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Area result = areaService.save(area);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, area.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /areas} : get all the areas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of areas in body.
     */
    @GetMapping("/areas")
    public ResponseEntity<List<Area>> getAllAreas(Pageable pageable, ServerHttpRequest serverRequest) {
        log.debug("REST request to get Area");
        Page<Area> page = areaService.findAll(pageable);        
        URI uri = serverRequest.getURI();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(uri);        
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(builder, page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /areas/count} : count all the areas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    // @GetMapping("/areas/count")
    // public ResponseEntity<Long> countAreas(AreaCriteria criteria) {
    //     log.debug("REST request to count Areas by criteria: {}", criteria);
    //     return ResponseEntity.ok().body(areaQueryService.countByCriteria(criteria));
    // }

    /**
     * {@code GET  /areas/:id} : get the "id" area.
     *
     * @param id the id of the area to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the area, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/areas/{id}")
    public ResponseEntity<Area> getArea(@PathVariable Long id) {
        log.debug("REST request to get Area : {}", id);
        Optional<Area> area = areaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(area);
    }

    /**
     * {@code DELETE  /areas/:id} : delete the "id" area.
     *
     * @param id the id of the area to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/areas/{id}")
    public ResponseEntity<Void> deleteArea(@PathVariable Long id) {
        log.debug("REST request to delete Area : {}", id);

        areaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

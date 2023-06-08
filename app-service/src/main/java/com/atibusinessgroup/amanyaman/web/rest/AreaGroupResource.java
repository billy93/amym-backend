package com.atibusinessgroup.amanyaman.web.rest;

import com.atibusinessgroup.amanyaman.domain.AreaGroup;
import com.atibusinessgroup.amanyaman.service.AreaGroupService;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.atibusinessgroup.amanyaman.domain.AreaGroup}.
 */
@RestController
@RequestMapping("/api")
public class AreaGroupResource {

    private final Logger log = LoggerFactory.getLogger(AreaGroupResource.class);

    private static final String ENTITY_NAME = "areaGroup";

    private String applicationName = "AMANYAMAN";

    private final AreaGroupService areaGroupService;

    public AreaGroupResource(AreaGroupService areaGroupService) {
        this.areaGroupService = areaGroupService;
    }

    /**
     * {@code POST  /area-groups} : Create a new areaGroup.
     *
     * @param areaGroup the areaGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new areaGroup, or with status {@code 400 (Bad Request)} if the areaGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/area-groups")
    public ResponseEntity<AreaGroup> createAreaGroup(@RequestBody AreaGroup areaGroup) throws URISyntaxException {
        log.debug("REST request to save AreaGroup : {}", areaGroup);
        if (areaGroup.getId() != null) {
            throw new BadRequestAlertException("A new areaGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AreaGroup result = areaGroupService.save(areaGroup);
        return ResponseEntity.created(new URI("/api/area-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /area-groups} : Updates an existing areaGroup.
     *
     * @param areaGroup the areaGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated areaGroup,
     * or with status {@code 400 (Bad Request)} if the areaGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the areaGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/area-groups")
    public ResponseEntity<AreaGroup> updateAreaGroup(@RequestBody AreaGroup areaGroup) throws URISyntaxException {
        log.debug("REST request to update AreaGroup : {}", areaGroup);
        if (areaGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AreaGroup result = areaGroupService.save(areaGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, areaGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /area-groups} : get all the areaGroups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of areaGroups in body.
     */
    @GetMapping("/area-groups")
    public ResponseEntity<List<AreaGroup>> getAllAreaGroups(Pageable pageable) {
        log.debug("REST request to get a page of AreaGroups");
        Page<AreaGroup> page = areaGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /area-groups/:id} : get the "id" areaGroup.
     *
     * @param id the id of the areaGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the areaGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/area-groups/{id}")
    public ResponseEntity<AreaGroup> getAreaGroup(@PathVariable Long id) {
        log.debug("REST request to get AreaGroup : {}", id);
        Optional<AreaGroup> areaGroup = areaGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(areaGroup);
    }

    /**
     * {@code DELETE  /area-groups/:id} : delete the "id" areaGroup.
     *
     * @param id the id of the areaGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/area-groups/{id}")
    public ResponseEntity<Void> deleteAreaGroup(@PathVariable Long id) {
        log.debug("REST request to delete AreaGroup : {}", id);
        areaGroupService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

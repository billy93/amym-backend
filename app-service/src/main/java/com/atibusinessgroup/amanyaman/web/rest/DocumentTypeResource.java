package com.atibusinessgroup.amanyaman.web.rest;

import com.atibusinessgroup.amanyaman.domain.DocumentType;
import com.atibusinessgroup.amanyaman.service.DocumentTypeService;
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
 * REST controller for managing {@link com.atibusinessgroup.amanyaman.domain.DocumentType}.
 */
@RestController
@RequestMapping("/api/app")
public class DocumentTypeResource {

    private final Logger log = LoggerFactory.getLogger(DocumentTypeResource.class);

    private static final String ENTITY_NAME = "DocumentType";

    private String applicationName ="AMANYAMAN";

    private final DocumentTypeService DocumentTypeService;

    public DocumentTypeResource(DocumentTypeService DocumentTypeService) {
        this.DocumentTypeService = DocumentTypeService;
    }

    /**
     * {@code POST  /document-types} : Create a new DocumentType.
     *
     * @param DocumentType the DocumentType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new DocumentType, or with status {@code 400 (Bad Request)} if the DocumentType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/document-types")
    public ResponseEntity<DocumentType> createDocumentType(@Valid @RequestBody DocumentType DocumentType) throws URISyntaxException {
        log.debug("REST request to save DocumentType : {}", DocumentType);
        if (DocumentType.getId() != null) {
            throw new BadRequestAlertException("A new DocumentType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentType result = DocumentTypeService.save(DocumentType);
        return ResponseEntity.created(new URI("/api/document-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /document-types} : Updates an existing DocumentType.
     *
     * @param DocumentType the DocumentType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated DocumentType,
     * or with status {@code 400 (Bad Request)} if the DocumentType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the DocumentType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/document-types")
    public ResponseEntity<DocumentType> updateDocumentType(@Valid @RequestBody DocumentType DocumentType) throws URISyntaxException {
        log.debug("REST request to update DocumentType : {}", DocumentType);
        if (DocumentType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DocumentType result = DocumentTypeService.save(DocumentType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, DocumentType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /document-types} : get all the document-types.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of document-types in body.
     */
    @GetMapping("/document-types")
    public ResponseEntity<List<DocumentType>> getAll(Pageable pageable) {
        log.debug("REST request to get Plan Types");
        Page<DocumentType> page = DocumentTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-types/count} : count all the document-types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    // @GetMapping("/document-types/count")
    // public ResponseEntity<Long> countCities(DocumentTypeCriteria criteria) {
    //     log.debug("REST request to count Cities by criteria: {}", criteria);
    //     return ResponseEntity.ok().body(DocumentTypeQueryService.countByCriteria(criteria));
    // }

    /**
     * {@code GET  /document-types/:id} : get the "id" DocumentType.
     *
     * @param id the id of the DocumentType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the DocumentType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/document-types/{id}")
    public ResponseEntity<DocumentType> getOne(@PathVariable Long id) {
        log.debug("REST request to get DocumentType : {}", id);
        Optional<DocumentType> DocumentType = DocumentTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(DocumentType);
    }

    /**
     * {@code DELETE  /document-types/:id} : delete the "id" DocumentType.
     *
     * @param id the id of the DocumentType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/document-types/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable Long id) {
        log.debug("REST request to delete DocumentType : {}", id);
        DocumentTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

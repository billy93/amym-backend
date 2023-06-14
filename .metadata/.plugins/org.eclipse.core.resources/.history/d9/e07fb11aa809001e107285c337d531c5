package com.atibusinessgroup.amanyaman.web.rest;

import com.atibusinessgroup.amanyaman.domain.ProductTravelAgent;
import com.atibusinessgroup.amanyaman.service.ProductTravelAgentService;
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
 * REST controller for managing {@link com.atibusinessgroup.amanyaman.domain.ProductTravelAgent}.
 */
@RestController
@RequestMapping("/api/app")
public class ProductTravelAgentResource {

    private final Logger log = LoggerFactory.getLogger(ProductTravelAgentResource.class);

    private static final String ENTITY_NAME = "ProductTravelAgent";

    private String applicationName ="AMANYAMAN";

    private final ProductTravelAgentService ProductTravelAgentService;

    public ProductTravelAgentResource(ProductTravelAgentService ProductTravelAgentService) {
        this.ProductTravelAgentService = ProductTravelAgentService;
    }

    /**
     * {@code POST  /product-travel-agents} : Create a new ProductTravelAgent.
     *
     * @param ProductTravelAgent the ProductTravelAgent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ProductTravelAgent, or with status {@code 400 (Bad Request)} if the ProductTravelAgent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-travel-agents")
    public ResponseEntity<ProductTravelAgent> createProductTravelAgent(@Valid @RequestBody ProductTravelAgent ProductTravelAgent) throws URISyntaxException {
        log.debug("REST request to save ProductTravelAgent : {}", ProductTravelAgent);
        if (ProductTravelAgent.getId() != null) {
            throw new BadRequestAlertException("A new ProductTravelAgent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductTravelAgent result = ProductTravelAgentService.save(ProductTravelAgent);
        return ResponseEntity.created(new URI("/api/product-travel-agents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-travel-agents} : Updates an existing ProductTravelAgent.
     *
     * @param ProductTravelAgent the ProductTravelAgent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ProductTravelAgent,
     * or with status {@code 400 (Bad Request)} if the ProductTravelAgent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ProductTravelAgent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-travel-agents")
    public ResponseEntity<ProductTravelAgent> updateProductTravelAgent(@Valid @RequestBody ProductTravelAgent ProductTravelAgent) throws URISyntaxException {
        log.debug("REST request to update ProductTravelAgent : {}", ProductTravelAgent);
        if (ProductTravelAgent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductTravelAgent result = ProductTravelAgentService.save(ProductTravelAgent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ProductTravelAgent.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /product-travel-agents} : get all the product-travel-agents.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of product-travel-agents in body.
     */
    @GetMapping("/product-travel-agents")
    public ResponseEntity<List<ProductTravelAgent>> getAll(Pageable pageable) {
        log.debug("REST request to get Plan Types");
        Page<ProductTravelAgent> page = ProductTravelAgentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-travel-agents/count} : count all the product-travel-agents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    // @GetMapping("/product-travel-agents/count")
    // public ResponseEntity<Long> countCities(ProductTravelAgentCriteria criteria) {
    //     log.debug("REST request to count Cities by criteria: {}", criteria);
    //     return ResponseEntity.ok().body(ProductTravelAgentQueryService.countByCriteria(criteria));
    // }

    /**
     * {@code GET  /product-travel-agents/:id} : get the "id" ProductTravelAgent.
     *
     * @param id the id of the ProductTravelAgent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ProductTravelAgent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-travel-agents/{id}")
    public ResponseEntity<ProductTravelAgent> getOne(@PathVariable Long id) {
        log.debug("REST request to get ProductTravelAgent : {}", id);
        Optional<ProductTravelAgent> ProductTravelAgent = ProductTravelAgentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ProductTravelAgent);
    }

    /**
     * {@code DELETE  /product-travel-agents/:id} : delete the "id" ProductTravelAgent.
     *
     * @param id the id of the ProductTravelAgent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-travel-agents/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable Long id) {
        log.debug("REST request to delete ProductTravelAgent : {}", id);
        ProductTravelAgentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

package com.atibusinessgroup.amanyaman.web.rest;

import com.atibusinessgroup.amanyaman.domain.Product;
import com.atibusinessgroup.amanyaman.service.ProductService;
import com.atibusinessgroup.amanyaman.util.HeaderUtil;
import com.atibusinessgroup.amanyaman.util.PaginationUtil;
import com.atibusinessgroup.amanyaman.util.ResponseUtil;
import com.atibusinessgroup.amanyaman.web.rest.dto.ProductSearchRequestDTO;
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
 * REST controller for managing {@link com.atibusinessgroup.amanyaman.domain.Product}.
 */
@RestController
@RequestMapping("/api/app")
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private static final String ENTITY_NAME = "Product";

    private String applicationName ="AMANYAMAN";

    private final ProductService ProductService;

    public ProductResource(ProductService ProductService) {
        this.ProductService = ProductService;
    }

    /**
     * {@code POST  /products} : Create a new Product.
     *
     * @param Product the Product to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new Product, or with status {@code 400 (Bad Request)} if the Product has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product Product) throws URISyntaxException {
        log.debug("REST request to save Product : {}", Product);
        if (Product.getId() != null) {
            throw new BadRequestAlertException("A new Product cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Product result = ProductService.save(Product);
        return ResponseEntity.created(new URI("/api/products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /products} : Updates an existing Product.
     *
     * @param Product the Product to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated Product,
     * or with status {@code 400 (Bad Request)} if the Product is not valid,
     * or with status {@code 500 (Internal Server Error)} if the Product couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/products")
    public ResponseEntity<Product> updateProduct(@Valid @RequestBody Product Product) throws URISyntaxException {
        log.debug("REST request to update Product : {}", Product);
        if (Product.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Product result = ProductService.save(Product);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, Product.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /products} : get all the products.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of products in body.
     */
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAll(ProductSearchRequestDTO productSearchRequestDTO, Pageable pageable) {
        log.debug("REST request to get Plan Types");
        Page<Product> page = ProductService.findAllBy(pageable, productSearchRequestDTO);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /products/count} : count all the products.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    // @GetMapping("/products/count")
    // public ResponseEntity<Long> countCities(ProductCriteria criteria) {
    //     log.debug("REST request to count Cities by criteria: {}", criteria);
    //     return ResponseEntity.ok().body(ProductQueryService.countByCriteria(criteria));
    // }

    /**
     * {@code GET  /products/:id} : get the "id" Product.
     *
     * @param id the id of the Product to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the Product, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getOne(@PathVariable Long id) {
        log.debug("REST request to get Product : {}", id);
        Optional<Product> Product = ProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Product);
    }

    /**
     * {@code DELETE  /products/:id} : delete the "id" Product.
     *
     * @param id the id of the Product to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable Long id) {
        log.debug("REST request to delete Product : {}", id);
        ProductService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

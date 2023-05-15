package com.atibusinessgroup.amanyaman.web.rest;

import com.atibusinessgroup.amanyaman.domain.Authority;
import com.atibusinessgroup.amanyaman.service.AuthorityService;
import com.atibusinessgroup.amanyaman.util.PaginationUtil;
import com.atibusinessgroup.amanyaman.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link Authority}.
 */
@RestController
@RequestMapping("/api/app")
public class AuthorityResource {

    private final Logger log = LoggerFactory.getLogger(AuthorityResource.class);

    private static final String ENTITY_NAME = "authority";

    private String applicationName = "AMANYAMAN";

    private final AuthorityService authorityService;

    public AuthorityResource(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    /**
     * {@code GET  /authorities} : get all the authorities.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of authorities in body.
     */
    @GetMapping("/authorities")
    public ResponseEntity<List<Authority>> getAllauthorities(Pageable pageable, ServerHttpRequest serverRequest) {
        log.debug("REST request to get a page of authorities");
        Page<Authority> page = authorityService.findAll(pageable);

        URI uri = serverRequest.getURI();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(uri);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(builder, page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}

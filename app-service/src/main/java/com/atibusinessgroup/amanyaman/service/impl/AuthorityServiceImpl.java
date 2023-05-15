package com.atibusinessgroup.amanyaman.service.impl;

import com.atibusinessgroup.amanyaman.domain.Authority;
import com.atibusinessgroup.amanyaman.repository.AuthorityRepository;
import com.atibusinessgroup.amanyaman.service.AuthorityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Authority}.
 */
@Service
@Transactional
public class AuthorityServiceImpl implements AuthorityService {

    private final Logger log = LoggerFactory.getLogger(AuthorityServiceImpl.class);

    private final AuthorityRepository authorityRepository;

    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Authority save(Authority authority) {
        log.debug("Request to save Authority : {}", authority);
        return authorityRepository.save(authority);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Authority> findAll(Pageable pageable) {
        log.debug("Request to get all Countries");
        return authorityRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Authority> findOne(String id) {
        log.debug("Request to get Authority : {}", id);
        return authorityRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Authority : {}", id);
        authorityRepository.deleteById(id);
    }
}

package com.atibusinessgroup.amanyaman.service.impl;

import com.atibusinessgroup.amanyaman.service.ProductTravelAgentService;
import com.atibusinessgroup.amanyaman.domain.ProductTravelAgent;
import com.atibusinessgroup.amanyaman.repository.ProductTravelAgentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ProductTravelAgent}.
 */
@Service
@Transactional
public class ProductTravelAgentServiceImpl implements ProductTravelAgentService {

    private final Logger log = LoggerFactory.getLogger(ProductTravelAgentServiceImpl.class);

    private final ProductTravelAgentRepository ProductTravelAgentRepository;

    public ProductTravelAgentServiceImpl(ProductTravelAgentRepository ProductTravelAgentRepository) {
        this.ProductTravelAgentRepository = ProductTravelAgentRepository;
    }

    @Override
    public ProductTravelAgent save(ProductTravelAgent ProductTravelAgent) {
        log.debug("Request to save ProductTravelAgent : {}", ProductTravelAgent);
        return ProductTravelAgentRepository.save(ProductTravelAgent);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductTravelAgent> findAll(Pageable pageable) {
        log.debug("Request to get all Cities");
        return ProductTravelAgentRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ProductTravelAgent> findOne(Long id) {
        log.debug("Request to get ProductTravelAgent : {}", id);
        return ProductTravelAgentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductTravelAgent : {}", id);
        ProductTravelAgentRepository.deleteById(id);
    }
}

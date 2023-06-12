package com.atibusinessgroup.amanyaman.service.impl;

import com.atibusinessgroup.amanyaman.service.ProductService;
import com.atibusinessgroup.amanyaman.domain.Product;
import com.atibusinessgroup.amanyaman.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository ProductRepository;

    public ProductServiceImpl(ProductRepository ProductRepository) {
        this.ProductRepository = ProductRepository;
    }

    @Override
    public Product save(Product Product) {
        log.debug("Request to save Product : {}", Product);
        return ProductRepository.save(Product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findAll(Pageable pageable) {
        log.debug("Request to get all Cities");
        return ProductRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return ProductRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        ProductRepository.deleteById(id);
    }
}

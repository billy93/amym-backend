package com.atibusinessgroup.amanyaman.service.impl;

import com.atibusinessgroup.amanyaman.service.PlanTypeService;
import com.atibusinessgroup.amanyaman.domain.PlanType;
import com.atibusinessgroup.amanyaman.repository.PlanTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PlanType}.
 */
@Service
@Transactional
public class PlanTypeServiceImpl implements PlanTypeService {

    private final Logger log = LoggerFactory.getLogger(PlanTypeServiceImpl.class);

    private final PlanTypeRepository PlanTypeRepository;

    public PlanTypeServiceImpl(PlanTypeRepository PlanTypeRepository) {
        this.PlanTypeRepository = PlanTypeRepository;
    }

    @Override
    public PlanType save(PlanType PlanType) {
        log.debug("Request to save PlanType : {}", PlanType);
        return PlanTypeRepository.save(PlanType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlanType> findAll(Pageable pageable) {
        log.debug("Request to get all Cities");
        return PlanTypeRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<PlanType> findOne(Long id) {
        log.debug("Request to get PlanType : {}", id);
        return PlanTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PlanType : {}", id);
        PlanTypeRepository.deleteById(id);
    }
}

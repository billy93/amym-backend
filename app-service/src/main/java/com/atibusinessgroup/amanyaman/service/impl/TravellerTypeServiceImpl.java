package com.atibusinessgroup.amanyaman.service.impl;

import com.atibusinessgroup.amanyaman.service.TravellerTypeService;
import com.atibusinessgroup.amanyaman.domain.TravellerType;
import com.atibusinessgroup.amanyaman.repository.TravellerTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TravellerType}.
 */
@Service
@Transactional
public class TravellerTypeServiceImpl implements TravellerTypeService {

    private final Logger log = LoggerFactory.getLogger(TravellerTypeServiceImpl.class);

    private final TravellerTypeRepository TravellerTypeRepository;

    public TravellerTypeServiceImpl(TravellerTypeRepository TravellerTypeRepository) {
        this.TravellerTypeRepository = TravellerTypeRepository;
    }

    @Override
    public TravellerType save(TravellerType TravellerType) {
        log.debug("Request to save TravellerType : {}", TravellerType);
        return TravellerTypeRepository.save(TravellerType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TravellerType> findAll(Pageable pageable) {
        log.debug("Request to get all Cities");
        return TravellerTypeRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<TravellerType> findOne(Long id) {
        log.debug("Request to get TravellerType : {}", id);
        return TravellerTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TravellerType : {}", id);
        TravellerTypeRepository.deleteById(id);
    }
}

package com.atibusinessgroup.amanyaman.service.impl;

import com.atibusinessgroup.amanyaman.service.BandTypeService;
import com.atibusinessgroup.amanyaman.domain.BandType;
import com.atibusinessgroup.amanyaman.repository.BandTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link BandType}.
 */
@Service
@Transactional
public class BandTypeServiceImpl implements BandTypeService {

    private final Logger log = LoggerFactory.getLogger(BandTypeServiceImpl.class);

    private final BandTypeRepository BandTypeRepository;

    public BandTypeServiceImpl(BandTypeRepository BandTypeRepository) {
        this.BandTypeRepository = BandTypeRepository;
    }

    @Override
    public BandType save(BandType BandType) {
        log.debug("Request to save BandType : {}", BandType);
        return BandTypeRepository.save(BandType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BandType> findAll(Pageable pageable) {
        log.debug("Request to get all Cities");
        return BandTypeRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<BandType> findOne(Long id) {
        log.debug("Request to get BandType : {}", id);
        return BandTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BandType : {}", id);
        BandTypeRepository.deleteById(id);
    }
}

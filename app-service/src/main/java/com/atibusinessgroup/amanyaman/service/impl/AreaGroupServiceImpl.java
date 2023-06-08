package com.atibusinessgroup.amanyaman.service.impl;

import com.atibusinessgroup.amanyaman.service.AreaGroupService;
import com.atibusinessgroup.amanyaman.domain.AreaGroup;
import com.atibusinessgroup.amanyaman.repository.AreaGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link AreaGroup}.
 */
@Service
@Transactional
public class AreaGroupServiceImpl implements AreaGroupService {

    private final Logger log = LoggerFactory.getLogger(AreaGroupServiceImpl.class);

    private final AreaGroupRepository areaGroupRepository;

    public AreaGroupServiceImpl(AreaGroupRepository areaGroupRepository) {
        this.areaGroupRepository = areaGroupRepository;
    }

    @Override
    public AreaGroup save(AreaGroup areaGroup) {
        log.debug("Request to save AreaGroup : {}", areaGroup);
        return areaGroupRepository.save(areaGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AreaGroup> findAll(Pageable pageable) {
        log.debug("Request to get all AreaGroups");
        return areaGroupRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AreaGroup> findOne(Long id) {
        log.debug("Request to get AreaGroup : {}", id);
        return areaGroupRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AreaGroup : {}", id);
        areaGroupRepository.deleteById(id);
    }
}

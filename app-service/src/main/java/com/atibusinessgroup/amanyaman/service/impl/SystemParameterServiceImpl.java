package com.atibusinessgroup.amanyaman.service.impl;

import com.atibusinessgroup.amanyaman.service.SystemParameterService;
import com.atibusinessgroup.amanyaman.domain.SystemParameter;
import com.atibusinessgroup.amanyaman.repository.SystemParameterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link SystemParameter}.
 */
@Service
@Transactional
public class SystemParameterServiceImpl implements SystemParameterService {

    private final Logger log = LoggerFactory.getLogger(SystemParameterServiceImpl.class);

    private final SystemParameterRepository systemParameterRepository;

    public SystemParameterServiceImpl(SystemParameterRepository systemParameterRepository){
        this.systemParameterRepository = systemParameterRepository;
    }

    @Override
    public SystemParameter save(SystemParameter systemParameter) {
        log.debug("Request to save SystemParameter : {}", systemParameter);
        return systemParameterRepository.save(systemParameter);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemParameter> findAll(Pageable pageable) {
        log.debug("Request to get all SystemParameters");
        return systemParameterRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SystemParameter> findOne(Long id) {
        log.debug("Request to get SystemParameter : {}", id);
        return systemParameterRepository.findById(id);
    }

    @Override
    public Optional<SystemParameter> findOneByName(String name) {
        return systemParameterRepository.findOneByName(name);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SystemParameter : {}", id);
        systemParameterRepository.deleteById(id);
    }
}

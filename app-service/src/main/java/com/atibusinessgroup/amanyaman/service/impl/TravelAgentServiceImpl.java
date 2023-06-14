package com.atibusinessgroup.amanyaman.service.impl;


import com.atibusinessgroup.amanyaman.service.TravelAgentService;
import com.atibusinessgroup.amanyaman.web.rest.dto.TravelAgentSearchRequestDTO;
import com.atibusinessgroup.amanyaman.domain.TravelAgent;
import com.atibusinessgroup.amanyaman.repository.TravelAgentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link TravelAgent}.
 */
@Service
@Transactional
public class TravelAgentServiceImpl implements TravelAgentService {

    private final Logger log = LoggerFactory.getLogger(TravelAgentServiceImpl.class);

    private final TravelAgentRepository travelAgentRepository;

    public TravelAgentServiceImpl(TravelAgentRepository travelAgentRepository) {
        this.travelAgentRepository = travelAgentRepository;
    }

    @Override
    public TravelAgent save(TravelAgent travelAgent) {
        log.debug("Request to save TravelAgent : {}", travelAgent);
        return travelAgentRepository.save(travelAgent);
    }

    @Override
    public void saveAll(List<TravelAgent> travelAgent) {
        travelAgentRepository.saveAll(travelAgent);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TravelAgent> findAll(Pageable pageable) {
        log.debug("Request to get all TravelAgents");
        return travelAgentRepository.findAll(pageable);
    }

    @Override
    public List<TravelAgent> findAll() {
        return travelAgentRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<TravelAgent> findOne(Long id) {
        log.debug("Request to get TravelAgent : {}", id);
        return travelAgentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TravelAgent : {}", id);
        travelAgentRepository.deleteById(id);
    }

    @Override
    public Optional<TravelAgent> findOneByCustcode(String custcode) {
        return travelAgentRepository.findOneByCustcode(custcode);
    }

    @Override
    public Optional<TravelAgent> findOneByCustcodeAndApiPassword(String username, String apiPassword) {
        return travelAgentRepository.findOneByCustcodeAndApiPassword(username, apiPassword);
    }

    @Override
    public List<TravelAgent> findAllByCgroupAndApiPassword(String cgroup, String password) {
        return travelAgentRepository.findAllByCgroupAndApiPassword(cgroup, password);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TravelAgent> findAllBy(Pageable pageable, TravelAgentSearchRequestDTO travelAgentSearchRequestDTO) {
        return travelAgentRepository.findAllBy(travelAgentSearchRequestDTO, pageable);
    }
}

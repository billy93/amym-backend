package com.atibusinessgroup.amanyaman.service;


import com.atibusinessgroup.amanyaman.domain.TravelAgent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link TravelAgent}.
 */
public interface TravelAgentService {

    /**
     * Save a travelAgent.
     *
     * @param travelAgent the entity to save.
     * @return the persisted entity.
     */
    TravelAgent save(TravelAgent travelAgent);

    /**
     * Save multiple travelAgent.
     *
     * @param travelAgent the entity to save.
     * @return the persisted entity.
     */
    void saveAll(List<TravelAgent> travelAgent);

    /**
     * Get all the travelAgents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TravelAgent> findAll(Pageable pageable);

    List<TravelAgent> findAll();


    /**
     * Get the "id" travelAgent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TravelAgent> findOne(Long id);

    /**
     * Delete the "id" travelAgent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<TravelAgent> findOneByCustcode(String custcode);

    Optional<TravelAgent> findOneByCustcodeAndApiPassword(String custcode, String password);

    List<TravelAgent> findAllByCgroupAndApiPassword(String cgroup, String password);
}
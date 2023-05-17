package com.atibusinessgroup.amanyaman.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.atibusinessgroup.amanyaman.domain.TravelAgent;

/**
 * Spring Data  repository for the TravelAgent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TravelAgentRepository extends JpaRepository<TravelAgent, Long>, JpaSpecificationExecutor<TravelAgent> {
    Optional<TravelAgent> findOneByCustcode(String custcode);

    Optional<TravelAgent> findOneByCustcodeAndApiPassword(String username, String apiPassword);

    List<TravelAgent> findAllByCgroupAndApiPassword(String cgroup, String password);
}

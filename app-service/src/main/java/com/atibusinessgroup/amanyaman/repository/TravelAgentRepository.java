package com.atibusinessgroup.amanyaman.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.atibusinessgroup.amanyaman.domain.TravelAgent;
import com.atibusinessgroup.amanyaman.web.rest.dto.TravelAgentSearchRequestDTO;

/**
 * Spring Data  repository for the TravelAgent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TravelAgentRepository extends JpaRepository<TravelAgent, Long>, JpaSpecificationExecutor<TravelAgent> {
    Optional<TravelAgent> findOneByCustcode(String custcode);

    Optional<TravelAgent> findOneByCustcodeAndApiPassword(String username, String apiPassword);

    List<TravelAgent> findAllByCgroupAndApiPassword(String cgroup, String password);

    @Query("SELECT t FROM TravelAgent t WHERE (:#{#criteria.travelAgentName} IS NULL OR t.travelAgentName LIKE %:#{#criteria.travelAgentName}%) " +
    "AND (:#{#criteria.custcode} IS NULL OR t.custcode LIKE %:#{#criteria.custcode}%)")
    Page<TravelAgent> findAllBy(@Param("criteria")TravelAgentSearchRequestDTO criteria, Pageable pageable);
}

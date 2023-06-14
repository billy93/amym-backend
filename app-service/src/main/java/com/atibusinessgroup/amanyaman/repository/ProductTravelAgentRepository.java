package com.atibusinessgroup.amanyaman.repository;

import com.atibusinessgroup.amanyaman.domain.ProductTravelAgent;
import com.atibusinessgroup.amanyaman.domain.TravelAgent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the City entity.
 */
@Repository
public interface ProductTravelAgentRepository extends JpaRepository<ProductTravelAgent, Long>, JpaSpecificationExecutor<ProductTravelAgent> {

	Page<ProductTravelAgent> findAllByTravelAgent(TravelAgent travelAgent, Pageable pageable);
}

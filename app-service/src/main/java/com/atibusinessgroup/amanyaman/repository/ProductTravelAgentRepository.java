package com.atibusinessgroup.amanyaman.repository;

import com.atibusinessgroup.amanyaman.domain.ProductTravelAgent;
import com.atibusinessgroup.amanyaman.web.rest.dto.ProductTravelAgentSearchRequestDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the City entity.
 */
@Repository
public interface ProductTravelAgentRepository extends JpaRepository<ProductTravelAgent, Long>, JpaSpecificationExecutor<ProductTravelAgent> {

    @Query("SELECT pta FROM ProductTravelAgent pta " +
    "JOIN pta.productMapping p " +
    "WHERE (:#{#criteria.productCode} IS NULL OR p.productCode = :#{#criteria.productCode}) " +
    "AND (:#{#criteria.travellerType} IS NULL OR p.travellerType.id = :#{#criteria.travellerType}) " +
    "AND (:#{#criteria.bandType} IS NULL OR p.bandType.id = :#{#criteria.bandType}) " +
    "AND (:#{#criteria.areaGroup} IS NULL OR p.areaGroup.id = :#{#criteria.areaGroup}) " +
    "AND (:#{#criteria.planType} IS NULL OR p.planType.id = :#{#criteria.planType})")
    Page<ProductTravelAgent> findAllBy(ProductTravelAgentSearchRequestDTO productTravelAgentSearchRequestDTO, Pageable pageable);
}

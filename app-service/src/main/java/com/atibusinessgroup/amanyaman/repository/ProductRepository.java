package com.atibusinessgroup.amanyaman.repository;

import com.atibusinessgroup.amanyaman.domain.Product;
import com.atibusinessgroup.amanyaman.web.rest.dto.ProductSearchRequestDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the City entity.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("SELECT p FROM Product p " +
    "WHERE (:#{#criteria.productCode} IS NULL OR p.productCode = :#{#criteria.productCode}) " +
    "AND (:#{#criteria.travellerType} IS NULL OR p.travellerType.id = :#{#criteria.travellerType}) " +
    "AND (:#{#criteria.bandType} IS NULL OR p.bandType.id = :#{#criteria.bandType}) " +
    "AND (:#{#criteria.areaGroup} IS NULL OR p.areaGroup.id = :#{#criteria.areaGroup}) " +
    "AND (:#{#criteria.planType} IS NULL OR p.planType.id = :#{#criteria.planType})")
    Page<Product> findAllBy(ProductSearchRequestDTO productSearchRequestDTO, Pageable pageable);
}

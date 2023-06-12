package com.atibusinessgroup.amanyaman.repository;

import com.atibusinessgroup.amanyaman.domain.Product;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the City entity.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
}

package com.atibusinessgroup.amanyaman.repository;

import com.atibusinessgroup.amanyaman.domain.City;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the City entity.
 */
@Repository
public interface CityRepository extends JpaRepository<City, Long>, JpaSpecificationExecutor<City> {
}

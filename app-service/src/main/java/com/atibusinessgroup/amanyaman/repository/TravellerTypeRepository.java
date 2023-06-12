package com.atibusinessgroup.amanyaman.repository;

import com.atibusinessgroup.amanyaman.domain.TravellerType;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the City entity.
 */
@Repository
public interface TravellerTypeRepository extends JpaRepository<TravellerType, Long>, JpaSpecificationExecutor<TravellerType> {
}

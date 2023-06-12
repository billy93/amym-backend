package com.atibusinessgroup.amanyaman.repository;

import com.atibusinessgroup.amanyaman.domain.PlanType;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the City entity.
 */
@Repository
public interface PlanTypeRepository extends JpaRepository<PlanType, Long>, JpaSpecificationExecutor<PlanType> {
}

package com.atibusinessgroup.amanyaman.repository;

import com.atibusinessgroup.amanyaman.domain.BandType;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the City entity.
 */
@Repository
public interface BandTypeRepository extends JpaRepository<BandType, Long>, JpaSpecificationExecutor<BandType> {
}

package com.atibusinessgroup.amanyaman.repository;

import com.atibusinessgroup.amanyaman.domain.Area;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Area entity.
 */
@Repository
public interface AreaRepository extends JpaRepository<Area, Long>, JpaSpecificationExecutor<Area> {
}

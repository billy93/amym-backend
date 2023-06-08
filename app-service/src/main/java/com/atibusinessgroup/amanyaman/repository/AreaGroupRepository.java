package com.atibusinessgroup.amanyaman.repository;

import com.atibusinessgroup.amanyaman.domain.AreaGroup;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AreaGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AreaGroupRepository extends JpaRepository<AreaGroup, Long> {
}

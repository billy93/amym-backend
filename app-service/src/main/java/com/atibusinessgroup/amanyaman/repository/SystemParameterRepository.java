package com.atibusinessgroup.amanyaman.repository;

import com.atibusinessgroup.amanyaman.domain.SystemParameter;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the SystemParameter entity.
 */
@Repository
public interface SystemParameterRepository extends JpaRepository<SystemParameter, Long>, JpaSpecificationExecutor<SystemParameter> {
    Optional<SystemParameter> findOneByName(String name);
}

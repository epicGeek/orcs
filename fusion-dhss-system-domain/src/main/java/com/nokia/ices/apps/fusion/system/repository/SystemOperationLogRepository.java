package com.nokia.ices.apps.fusion.system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.system.domain.SystemOperationLog;

@RepositoryRestResource(collectionResourceRel = "system-operation-log", path = "system-operation-log", itemResourceRel = "system-operation-log")
public interface SystemOperationLogRepository extends JpaRepository<SystemOperationLog, Long>, JpaSpecificationExecutor<SystemOperationLog> {
    public Page<SystemOperationLog> findListByLoginUserName(@Param("loginUserName") String loginUserName,Pageable pageable);
}

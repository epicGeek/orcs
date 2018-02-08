package com.nokia.ices.apps.fusion.log.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.nokia.ices.apps.fusion.log.domain.WorkType;

/**
 * 工作包类型
 * @author Administrator
 */
@RepositoryRestResource(collectionResourceRel = "workType", path = "workType" ,itemResourceRel="workType")
public interface WorkTypeRepository extends JpaRepository<WorkType, Long>,JpaSpecificationExecutor<WorkType> {

}
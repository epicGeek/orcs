package com.nokia.ices.apps.fusion.maintain.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.maintain.domain.MaintainResult;


@RepositoryRestResource(collectionResourceRel = "maintain-result", path = "maintain-result" ,itemResourceRel="maintain-result")
public interface MaintainResultRepository extends CrudRepository<MaintainResult, Long>,JpaSpecificationExecutor<MaintainResult>{
	List<MaintainResult> findResultByOperationIdAndResponseTimeIsNotNull(@Param("q") Long operationId);
	List<MaintainResult> findResultByOperationId(@Param("q") Long operationId);
}

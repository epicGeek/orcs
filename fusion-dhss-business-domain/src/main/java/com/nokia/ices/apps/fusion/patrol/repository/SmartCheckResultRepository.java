package com.nokia.ices.apps.fusion.patrol.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckResult;


@RepositoryRestResource(collectionResourceRel="smart-check-result",path="smart-check-result",itemResourceRel="smart-check-result")
public interface SmartCheckResultRepository extends JpaRepository<SmartCheckResult, Long>,JpaSpecificationExecutor<SmartCheckResult>{

	
}

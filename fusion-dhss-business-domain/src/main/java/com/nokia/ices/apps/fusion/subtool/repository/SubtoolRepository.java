package com.nokia.ices.apps.fusion.subtool.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.subtool.domain.CheckSubtoolResult;


@RepositoryRestResource(collectionResourceRel = "subtool-item", path = "subtool-item" ,itemResourceRel="subtool-item")
public interface SubtoolRepository extends JpaRepository<CheckSubtoolResult, Long>,JpaSpecificationExecutor<CheckSubtoolResult>{

	
}


package com.nokia.ices.apps.fusion.maintain.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.maintain.domain.MaintainOperation;

@RepositoryRestResource(collectionResourceRel = "maintain-operation", path = "maintain-operation" ,itemResourceRel="maintain-operation")
public interface MaintainOperationRepository extends CrudRepository<MaintainOperation, Long>,JpaSpecificationExecutor<MaintainOperation>{
	public List<MaintainOperation> findByCheckNameIsLike(@Param("q") String q);
}

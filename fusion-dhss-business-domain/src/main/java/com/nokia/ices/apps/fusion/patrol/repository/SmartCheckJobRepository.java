package com.nokia.ices.apps.fusion.patrol.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckJob;



@RepositoryRestResource(collectionResourceRel = "smart-check-job", path = "smart-check-job" ,itemResourceRel="smart-check-job")
public interface SmartCheckJobRepository extends CrudRepository<SmartCheckJob, Long>,JpaSpecificationExecutor<SmartCheckJob> {
	List<SmartCheckJob> findSmartCheckJobListByJobNameContains(@Param("jobName") String jobName,Sort sort);
}

package com.nokia.ices.apps.fusion.ems.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.ems.domain.EmsCheckJob;

@RepositoryRestResource(collectionResourceRel = "ems-check-job", path = "ems-check-job")
public interface EmsCheckJobRepository extends CrudRepository<EmsCheckJob, Long> ,JpaSpecificationExecutor<EmsCheckJob>{

}

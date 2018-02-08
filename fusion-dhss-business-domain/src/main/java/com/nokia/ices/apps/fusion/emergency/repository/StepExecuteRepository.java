package com.nokia.ices.apps.fusion.emergency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.emergency.domian.StepExecute;

@RepositoryRestResource(collectionResourceRel = "step-execute", path = "step-execute" ,itemResourceRel="step-execute")
public interface StepExecuteRepository extends JpaRepository<StepExecute, Long>,JpaSpecificationExecutor<StepExecute> {

}

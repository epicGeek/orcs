package com.nokia.ices.apps.fusion.emergency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.emergency.domian.StepConfTable;

@RepositoryRestResource(collectionResourceRel = "step-conf-table", path = "step-conf-table" ,itemResourceRel="step-conf-table")
public interface StepConfTableRepository  extends JpaRepository<StepConfTable, Long>,JpaSpecificationExecutor<StepConfTable>{

}

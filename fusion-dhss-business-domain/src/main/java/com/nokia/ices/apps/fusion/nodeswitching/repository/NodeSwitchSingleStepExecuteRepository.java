package com.nokia.ices.apps.fusion.nodeswitching.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchSingleStepExecute;

@RepositoryRestResource(collectionResourceRel = "node-switch-single-step-execute", path = "node-switch-single-step-execute" ,itemResourceRel="node-switch-single-step-execute")
public interface NodeSwitchSingleStepExecuteRepository extends CrudRepository<NodeSwitchSingleStepExecute,Long> ,JpaSpecificationExecutor<NodeSwitchSingleStepExecute> {

}

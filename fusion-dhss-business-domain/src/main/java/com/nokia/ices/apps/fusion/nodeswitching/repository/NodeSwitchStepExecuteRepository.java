package com.nokia.ices.apps.fusion.nodeswitching.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchStepExecute;

@RepositoryRestResource(collectionResourceRel = "node-switch-step-execute", path = "node-switch-step-execute" ,itemResourceRel="node-switch-step-execute")
public interface NodeSwitchStepExecuteRepository  extends CrudRepository<NodeSwitchStepExecute,Long> ,JpaSpecificationExecutor<NodeSwitchStepExecute>{

}

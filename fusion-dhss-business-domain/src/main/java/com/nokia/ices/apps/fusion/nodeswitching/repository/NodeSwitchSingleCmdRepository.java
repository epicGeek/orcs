package com.nokia.ices.apps.fusion.nodeswitching.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchSingleCmd;

@RepositoryRestResource(collectionResourceRel = "node-switch-single-cmd", path = "node-switch-single-cmd" ,itemResourceRel="node-switch-single-cmd")
public interface NodeSwitchSingleCmdRepository extends CrudRepository<NodeSwitchSingleCmd,Long> ,JpaSpecificationExecutor<NodeSwitchSingleCmd>  {

}

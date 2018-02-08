package com.nokia.ices.apps.fusion.nodeswitching.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchCmd;

@RepositoryRestResource(collectionResourceRel = "node-switch-cmd", path = "node-switch-cmd" ,itemResourceRel="node-switch-cmd")
public interface NodeSwitchCmdRepository extends CrudRepository<NodeSwitchCmd,Long> ,JpaSpecificationExecutor<NodeSwitchCmd> {

}

package com.nokia.ices.apps.fusion.nodeswitching.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitch;

@RepositoryRestResource(collectionResourceRel = "node-switch", path = "node-switch" ,itemResourceRel="node-switch")
public interface NodeSwitchRepository extends CrudRepository<NodeSwitch,Long> ,JpaSpecificationExecutor<NodeSwitch> {

}

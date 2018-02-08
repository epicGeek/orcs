package com.nokia.ices.apps.fusion.nodeswitching.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchCase;

@RepositoryRestResource(collectionResourceRel = "node-switch-case", path = "node-switch-case" ,itemResourceRel="node-switch-case")
public interface NodeSwitchCaseRepository extends CrudRepository<NodeSwitchCase,Long> ,JpaSpecificationExecutor<NodeSwitchCase> {

}

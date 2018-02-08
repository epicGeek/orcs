package com.nokia.ices.apps.fusion.nodeswitching.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchSingleHistory;

@RepositoryRestResource(collectionResourceRel = "node-switch-single-history", path = "node-switch-single-history" ,itemResourceRel="node-switch-single-history")
public interface NodeSwitchSingleHistoryRepository extends CrudRepository<NodeSwitchSingleHistory,Long> ,JpaSpecificationExecutor<NodeSwitchSingleHistory> {

}

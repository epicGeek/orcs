package com.nokia.ices.apps.fusion.monitor.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.monitor.domain.MonitorTable;

@RepositoryRestResource(collectionResourceRel = "monitor-table", path = "monitor-table" ,itemResourceRel="monitor-table")
public interface MonitorTableRepository extends CrudRepository<MonitorTable,Long> ,JpaSpecificationExecutor<MonitorTable> {
	

}

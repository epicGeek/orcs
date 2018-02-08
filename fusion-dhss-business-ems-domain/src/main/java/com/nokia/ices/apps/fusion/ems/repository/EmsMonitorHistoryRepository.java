package com.nokia.ices.apps.fusion.ems.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.ems.domain.EmsMonitorHistory;

@RepositoryRestResource(collectionResourceRel = "ems-monitor-history", path = "ems-monitor-history")
public interface EmsMonitorHistoryRepository extends CrudRepository<EmsMonitorHistory, Long> ,JpaSpecificationExecutor<EmsMonitorHistory>{

}

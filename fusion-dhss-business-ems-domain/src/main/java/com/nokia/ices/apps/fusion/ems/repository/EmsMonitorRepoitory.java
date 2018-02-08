package com.nokia.ices.apps.fusion.ems.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.ems.domain.EmsMonitor;

@RepositoryRestResource(collectionResourceRel = "ems-monitor", path = "ems-monitor")
public interface EmsMonitorRepoitory extends CrudRepository<EmsMonitor, Long>,JpaSpecificationExecutor<EmsMonitor> {

}

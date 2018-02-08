package com.nokia.ices.apps.fusion.quota.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.quota.domain.QuotaMonitor;





@RepositoryRestResource(collectionResourceRel = "quota-monitor", path = "quota-monitor" ,itemResourceRel="quota-monitor")
public interface QuotaMonitorRepository extends CrudRepository<QuotaMonitor, Long>,JpaSpecificationExecutor<QuotaMonitor>{

}

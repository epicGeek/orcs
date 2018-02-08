package com.nokia.ices.apps.fusion.quota.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.quota.domain.QuotaMonitorHistory;



@RepositoryRestResource(collectionResourceRel = "quota-monitor-history", path = "quota-monitor-history" ,itemResourceRel="quota-monitor-history")
public interface QuotaMonitorHistoryRepository extends CrudRepository<QuotaMonitorHistory, Long>,JpaSpecificationExecutor<QuotaMonitorHistory>{
}

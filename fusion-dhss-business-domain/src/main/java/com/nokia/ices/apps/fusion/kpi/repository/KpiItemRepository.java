package com.nokia.ices.apps.fusion.kpi.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.kpi.domain.KpiItem;

@RepositoryRestResource(collectionResourceRel = "kpi-item", path = "kpi-item" ,itemResourceRel="kpi-item")
public interface KpiItemRepository extends CrudRepository<KpiItem, Long>,JpaSpecificationExecutor<KpiItem> {

}

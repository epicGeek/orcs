package com.nokia.ices.apps.fusion.alarm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.alarm.AlarmMonitor;

@RepositoryRestResource(collectionResourceRel = "alarm-monitor", path = "alarm-monitor", itemResourceRel = "alarm-monitor")
public interface AlarmMonitorRepository extends CrudRepository<AlarmMonitor, Long>,
        JpaSpecificationExecutor<AlarmMonitor> {

	Page<AlarmMonitor> findAll(Specification<AlarmMonitor> spec, Pageable pageable);
}

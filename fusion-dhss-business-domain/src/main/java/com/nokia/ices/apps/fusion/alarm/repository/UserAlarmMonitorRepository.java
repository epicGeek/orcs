package com.nokia.ices.apps.fusion.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.alarm.UserAlarmMonitor;

@RepositoryRestResource(collectionResourceRel = "user-alarm-monitor", path = "user-alarm-monitor", itemResourceRel = "user-alarm-monitor")
public interface UserAlarmMonitorRepository extends JpaRepository<UserAlarmMonitor, Long>, JpaSpecificationExecutor<UserAlarmMonitor>{

}

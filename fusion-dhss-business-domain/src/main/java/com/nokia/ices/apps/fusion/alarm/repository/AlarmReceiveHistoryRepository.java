package com.nokia.ices.apps.fusion.alarm.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.alarm.AlarmReceiveHistory;

@RepositoryRestResource(collectionResourceRel = "alarm_receive_history", path = "alarm_receive_history",itemResourceRel = "alarm_receive_history")
public interface AlarmReceiveHistoryRepository extends CrudRepository<AlarmReceiveHistory, Long>, JpaSpecificationExecutor<AlarmReceiveHistory> {

}

package com.nokia.ices.apps.fusion.alarm.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.alarm.AlarmReceiveRecord;

@RepositoryRestResource(collectionResourceRel = "alarm_receive_record", path = "alarm_receive_record", itemResourceRel = "alarm_receive_record")
public interface AlarmReceiveRecordRepository  extends CrudRepository<AlarmReceiveRecord, Long>,
JpaSpecificationExecutor<AlarmReceiveRecord> {

}

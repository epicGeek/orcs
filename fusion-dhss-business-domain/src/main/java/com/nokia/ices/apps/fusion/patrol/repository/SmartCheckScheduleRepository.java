package com.nokia.ices.apps.fusion.patrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckSchedule;


@RepositoryRestResource(collectionResourceRel="smart-check-schedule",path="smart-check-schedule",itemResourceRel="smart-check-schedule")
public interface SmartCheckScheduleRepository extends JpaRepository<SmartCheckSchedule, Long>,JpaSpecificationExecutor<SmartCheckSchedule>{

}

package com.nokia.ices.apps.fusion.patrol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckJob;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckScheduleResult;


@RepositoryRestResource(collectionResourceRel="smart-check-schedule-result",path="smart-check-schedule-result",itemResourceRel="smart-check-schedule-result")
public interface SmartCheckScheduleResultRepository extends JpaRepository<SmartCheckScheduleResult, Long>,JpaSpecificationExecutor<SmartCheckScheduleResult> {
	

	public List<SmartCheckScheduleResult> findListBySmartCheckJob(SmartCheckJob smartCheckJob);
	
}

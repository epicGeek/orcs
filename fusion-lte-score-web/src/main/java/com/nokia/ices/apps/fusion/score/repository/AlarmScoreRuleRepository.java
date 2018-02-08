package com.nokia.ices.apps.fusion.score.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.score.domain.AlarmScoreRule;


@RepositoryRestResource(collectionResourceRel = "alarmScore", path = "alarmScore" ,itemResourceRel="alarmScore")
public interface AlarmScoreRuleRepository extends JpaRepository<AlarmScoreRule, Long>,JpaSpecificationExecutor<AlarmScoreRule>{

	
}

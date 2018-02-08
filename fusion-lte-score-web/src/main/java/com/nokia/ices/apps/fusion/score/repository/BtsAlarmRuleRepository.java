package com.nokia.ices.apps.fusion.score.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.score.domain.BtsAlarmRule;

@RepositoryRestResource(collectionResourceRel = "btsAlarmRule", path = "btsAlarmRule" ,itemResourceRel="btsAlarmRule")
public interface BtsAlarmRuleRepository extends JpaRepository<BtsAlarmRule, Long>,JpaSpecificationExecutor<BtsAlarmRule>{

	
}

package com.nokia.ices.apps.fusion.alarm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.alarm.IcesAlarmRule;


@RepositoryRestResource(collectionResourceRel = "system-alarm-rule", path = "system-alarm-rule", itemResourceRel = "system-alarm-rule")
public interface IcesAlarmRuleRepository extends JpaRepository<IcesAlarmRule, Long>, JpaSpecificationExecutor<IcesAlarmRule> {

	public Page<IcesAlarmRule> findAll(
			Specification<IcesAlarmRule> specification,
			Pageable pageable);

}

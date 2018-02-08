package com.nokia.ices.apps.fusion.alarm.service.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.alarm.IcesAlarmRule;
import com.nokia.ices.apps.fusion.alarm.IcesAlarmRule_;
import com.nokia.ices.apps.fusion.alarm.repository.IcesAlarmRuleRepository;
import com.nokia.ices.apps.fusion.alarm.service.IcesAlarmRuleService;


/**
 * 告警规则
 */
@Service
public class IcesAlarmRuleServiceImpl implements IcesAlarmRuleService {

	@Autowired
	IcesAlarmRuleRepository icesAlarmRulerepository;
	
	@Override
	public Page<IcesAlarmRule> getAlarmRule(String alarmNo, Pageable pageable) {
		
		return icesAlarmRulerepository.findAll(new Specification<IcesAlarmRule>() {
			@Override
			public Predicate toPredicate(Root<IcesAlarmRule> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (null != alarmNo && !alarmNo.isEmpty()) {
					return	builder.like(builder.concat(root.get("alarmNo"), root.get("faultId")), matchAny(alarmNo));
				}
				return null;
			}
		}, pageable );
	}
	

	private String matchAny(String para){
		return "%"+para+"%";
	}

}

package com.nokia.ices.apps.fusion.alarm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nokia.ices.apps.fusion.alarm.IcesAlarmRule;


/**
 * 告警规则
 */
public interface IcesAlarmRuleService {

	public Page<IcesAlarmRule> getAlarmRule(String alarmNo, Pageable page);
}

package com.nokia.ices.apps.fusion.service.impl;

import java.util.Map;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.console.domain.ConsoleLog;
import com.nokia.ices.apps.fusion.console.repository.ConsoleLogRepository;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.service.ConsoleLogService;


@Service("consoleLogService")
public class ConsoleLogServiceImpl implements ConsoleLogService{

	@Autowired
	private ConsoleLogRepository consoleLogRepository;
	
	@Override
	public Page<ConsoleLog> findConsoleLogByUnitName(Map<String, Object> searchParams, Pageable pageable) {
		 Map<String,SearchFilter> filter = SearchFilter.parse(searchParams);
	        Specification<ConsoleLog> spec = 
	                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, ConsoleLog.class);
	     return consoleLogRepository.findAll(spec, pageable);
	}

}

package com.nokia.ices.apps.fusion.quota.service.impl;

import java.util.Map;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.quota.domain.QuotaMonitorHistory;
import com.nokia.ices.apps.fusion.quota.repository.QuotaMonitorHistoryRepository;
import com.nokia.ices.apps.fusion.quota.service.QuotaMonitorHistoryService;

@Service("quotaMonitorHistoryService")
public class QuotaMonitorHistoryServiceImpl implements QuotaMonitorHistoryService {
	@Autowired
	private QuotaMonitorHistoryRepository quotaMonitorHistoryRepository;
	
	@Override
	public Page<QuotaMonitorHistory> findQuotaMonitorHistoryFilter(Map<String, Object> paramMap, Pageable pageable) {
		Map<String, SearchFilter> filter = SearchFilter.parse(paramMap);
		Specification<QuotaMonitorHistory> spec = DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND,
				QuotaMonitorHistory.class);
		Page<QuotaMonitorHistory> page = quotaMonitorHistoryRepository.findAll(spec, pageable);
		return page;
	}

	
}

package com.nokia.ices.apps.fusion.quota.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nokia.ices.apps.fusion.quota.domain.QuotaMonitorHistory;

public interface QuotaMonitorHistoryService {
	/**
	 * 查询指标监控的数据的方法
	 * @param paramMap
	 * @param pageable
	 * @return
	 */
	Page<QuotaMonitorHistory> findQuotaMonitorHistoryFilter(Map<String, Object> paramMap, Pageable pageable);
}

package com.nokia.ices.apps.fusion.quota.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nokia.ices.apps.fusion.quota.domain.QuotaMonitor;
import com.nokia.ices.apps.fusion.quota.model.QuotaMonitorExport;
import com.nokia.ices.apps.fusion.quota.model.QuotaMonitorHistoryExportModel;
import com.nokia.ices.apps.fusion.quota.model.QuotaMonitorModel;

public interface QuotaMonitorService {
	public List<Map<String, Object>> queryQuotaMonitorData(Map<String, Object> m);
	public List<Map<String, Object>> getQuotaMonitorDataList(Map<String, Object> m);
	public Long queryQuotaMonitorDataCount(Map<String, Object> m);
	public List<Map<String, Object>> queryAllBscType(Map<String, Object> m);
	public List<Map<String, Object>> queryAllQuotaData(Map<String, Object> m);
	public List<QuotaMonitor> findPageByCriteria(Map<String, Object> m);
	public List<Map<String, Object>> queryAllBscName(Map<String, Object> m);
	public List<QuotaMonitorExport> findPageByCriteriaExport(Map<String, Object> m);
	public List<QuotaMonitorHistoryExportModel> findPageByCriteriaHistoryExport(Map<String, Object> m);
	Page<QuotaMonitor> findQuotaMonitorFilter(Map<String, Object> paramMap, Pageable pageable);
	public List<QuotaMonitor> queryQuotaMonitorModelData(Map<String, Object> paramMap);
	
	List<QuotaMonitor> findQuotaMonitor(Map<String, Object> paramMap);
}

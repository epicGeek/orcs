package com.nokia.ices.apps.fusion.quota.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.nokia.ices.apps.fusion.quota.domain.QuotaMonitor;
import com.nokia.ices.apps.fusion.quota.domain.QuotaMonitorHistory;
import com.nokia.ices.apps.fusion.quota.model.AllQuota;
import com.nokia.ices.apps.fusion.quota.model.QuotaMonitorModel;

public interface AllQuotaService {
	public List<QuotaMonitorHistory> queryAllQuotaData(Map<String, Object> m,Pageable pageable);
/*	public List<Map<String, Object>> queryAllBscName(Map<String, Object> m);*/	
	public List<Map<String, Object>> queryQuotaKpiData(Map<String, Object> m);
	public List<QuotaMonitorModel> findPageByCriteriaAllQuota(Map<String, Object> m);
	public List<AllQuota> findPageByCriteriaAllQuotaExport(Map<String, Object> m);
	List<QuotaMonitor> queryQuotaMonitorModelData(Map<String, Object> paramMap);
}

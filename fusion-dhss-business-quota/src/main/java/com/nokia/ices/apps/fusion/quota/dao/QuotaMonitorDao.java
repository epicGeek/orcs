package com.nokia.ices.apps.fusion.quota.dao;

import java.util.List;
import java.util.Map;

public interface QuotaMonitorDao {
	public List<Map<String, Object>> queryQuotaMonitorData(Map<String, Object> m);
	public Long queryQuotaMonitorDataCount(Map<String, Object> m);
	public List<Map<String, Object>> queryAllBscType(Map<String, Object> m);
	public List<Map<String, Object>> queryAllQuotaData(Map<String, Object> m);
	public List<Map<String, Object>> queryAllBscName(Map<String, Object> m);
}

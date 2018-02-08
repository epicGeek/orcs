package com.nokia.ices.apps.fusion.quota.dao;

import java.util.List;
import java.util.Map;

public interface AllQuotaDao {
	public List<Map<String, Object>> queryAllQuotaData(Map<String, Object> m);
	public List<Map<String, Object>> queryAllBscName(Map<String, Object> m);
	public List<Map<String, Object>> queryQuotaKpiData(Map<String, Object> m);
}

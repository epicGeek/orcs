package com.nokia.ices.apps.fusion.patrol.service;

import java.util.List;
import java.util.Map;

public interface SmartCheckService {

	public Map<String, Object> getSmartCheckJobResultPageList(
			Map<String, Object> params);

	public Map<String, Object> getSmartCheckResultPageList(
			Map<String, Object> params);

	public Map<String, Object> getSmartCheckDetailResultPageList(
			Map<String, Object> map);

	public List<Map<String, Object>> getDownLoadPath(Map<String, Object> params);

	public Map<String, Object> getSmartCheckJob(
			Map<String, Object> map);

	public Integer saveJob(Map<String, Object> map) throws Exception;

	public void removeJobByID(String jobId);

	public List<Map<String, Object>> getNeList(Map<String, Object> map);

	public void saveNeList(Map<String, Object> map);

	@SuppressWarnings("rawtypes")
	public void batchUpdate(String jobId, String addOrRemove, List<Map> neList);

	public List<Map<String, Object>> getCheckItemList(Map<String, Object> map);

	public void saveCheckItemList(Map<String, Object> map);

	@SuppressWarnings("rawtypes")
	public void batchUpdateCheckItem(String jobId, String addOrRemove,
			List<Map> checkItemList);

	public Integer updateJobState(String jobId);

}
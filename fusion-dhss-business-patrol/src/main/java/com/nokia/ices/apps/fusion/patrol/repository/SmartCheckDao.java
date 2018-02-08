package com.nokia.ices.apps.fusion.patrol.repository;

import java.util.List;
import java.util.Map;

public interface SmartCheckDao {

	public List<Map<String, Object>> getSmartCheckJobResultPageList(Map<String, Object> m);

	public int getSmartCheckJobResultCount(Map<String, Object> m);
	
	public List<Map<String, Object>> getSmartCheckResultPageList(Map<String, Object> m);

	public int getSmartCheckResultCount(Map<String, Object> m);

	public List<Map<String, Object>> getSmartCheckDetailResultPageList(
			Map<String, Object> params);

	public int getSmartCheckDetailResultCount(Map<String, Object> params);

	public List<Map<String, Object>> getSmartCheckJob(Map<String, Object> params);

	public int getSmartCheckJobCount(Map<String, Object> params);

	public void addJob(Map<String, Object> map);

	public void updateJob(Map<String, Object> map);
 
	public void removeNeByJobID(String jobId);
	
	public void removeScheduleByJobID(String jobId);
	
	public void removeJobByID(String jobId);

	public List<Map<String, Object>> getNeList(Map<String, Object> map);

	public void saveNeList(Map<String, Object> map);

	@SuppressWarnings("rawtypes")
	public void batchUpdate(List<Map> neList);

	public void deleteNe(Map<String, Object> map);

	public List<Map<String, Object>> getCheckItemList(Map<String, Object> map);

	public void saveCheckItemList(Map<String, Object> map);

	@SuppressWarnings("rawtypes")
	public void batchUpdateCheckItem(List<Map> checkItemList);

	public void deleteCheckItem(Map<String, Object> map);

	public int getCountJobNe(Map<String, Object> map);

	public int getCountJobItem(Map<String, Object> map);

	public List<Map<String, Object>> getCountNeAndItem(String jobId);
	

}

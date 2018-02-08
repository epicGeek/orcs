package com.nokia.ices.apps.fusion.patrol.repository;

import java.util.List;
import java.util.Map;

public interface SmartCheckSearchDao {
	
	Map<String, Object> getSmartCheckResultPageList(Map<String, Object> map);
	
	List<Map<String, Object>> getSmartCheckCountResult();
	
	
	List<Map<String, Object>> getSmartCheckDetailResultPageList(Map<String, Object> map);
	
	
	public List<Map<String, Object>> getSmartCheckDetailResultPageList(String id);
	

}

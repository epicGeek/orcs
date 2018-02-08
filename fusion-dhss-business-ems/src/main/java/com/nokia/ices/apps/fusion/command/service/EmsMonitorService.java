package com.nokia.ices.apps.fusion.command.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

import com.nokia.ices.apps.fusion.ems.domain.EmsMonitor;
import com.nokia.ices.apps.fusion.ems.domain.EmsMonitorHistory;
import com.nokia.ices.apps.fusion.ems.domain.EmsMutedItem;

public interface EmsMonitorService {
	
	public Map<String,List<Map<String,Object>>> findEmsResult(String ids);
	public List<EmsMonitorHistory> findEmsMonitorHistoryTrend(Map<String,Object> map,Sort sort);
	public List<EmsMutedItem> findEmsMutedItem(Map<String, Object> map);
	public List<EmsMonitor> findEmsMonitor(Map<String,Object> map);

}

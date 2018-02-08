//package com.nokia.ices.apps.fusion.patrol.repository;
//
//import java.util.Map;
//
//import com.nokia.ices.apps.fusion.command.domain.CommandCheckItem;
//
//public interface SmartCheckTastDao {
//	
//	//巡检定时任务
//	public void updateCheckItem(Map<String, Object> map);
//	
//	public void updateJob(Map<String, Object> map);
//	
//	public Map<String, Object> getScheduleResult(Map<String, Object> map);
//	
//	public CommandCheckItem searchCheckDetailTmp(Map<String, Object> map);
//	
//	public Map<String, Object> getTimeOutCheckDetailTmpList(Map<String, Object> map);
//	
//	public Map<String, Object> getCheckDetailTmpList(Map<String, Object> map);
//	
//	public Map<String, Object> findAllWithDHLR(Map<String, Object> map);
//	
//	public Map<String, Object> getAllJob(Map<String, Object> map);
//	
//	public Map<String, Object> findUnfinishedId();
//	
//	public Map<String, Object> findJobIdByTime(Map<String, Object> map);
//	
//	public void deleteByUUID(String uuId);
//	
//	public void saveScheduleResult(Map<String, Object> map);
//	
//	public void saveCheckDetailTmp(String contexts);
//	
//	public void saveCheckDetailTmpList(Map<String, Object> map);
//	
//	public void deleteTimeOutCheckDetailTmpList(Map<String, Object> map);
//	
//	public void checkState(Map<String, Object> map);
//
//}

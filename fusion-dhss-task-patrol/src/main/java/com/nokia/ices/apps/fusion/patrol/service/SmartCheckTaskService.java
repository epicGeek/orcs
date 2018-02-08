package com.nokia.ices.apps.fusion.patrol.service;

import java.text.ParseException;
import java.util.Map;

import com.nokia.ices.apps.fusion.command.domain.CommandCheckItem;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckJob;

public interface SmartCheckTaskService {
	
	public static final String TASKNAEM = "DHLR-TASK-SMART";
	
	
	public static final String TASKSCRIPTNAEM = "DHLR-TASK-SMART-SCRIPT";
	
	
//	List<Map<String, Object>> findNextDayAndType(String session);
	
//	void updateNextDay(Map<String, Object> obj,String session);
	

	void updateSmartCheckResultTmp(String resultCode, String session, String errorMessage);

	void persistLua(String session, String resultCode, String luaResult);

	Map<String, Object> getSmartCheckResultTmpByUUID(String session);

	void persist(String session, String resultCode, String location, Map<String, Object> smartCheckResultTmpMap,
			CommandCheckItem check);

	void addSmartCheckResult(String resultCode,  Map<String, Object> map)
			throws ParseException;

	void updateSmartCheckResultTmp(String execFlag, String session);

	void updateSmartCheckScheduleResults(String execFlag, String session,String execTime);

	void updateSmartCheckScheduleResultBySession( String schedule_id);

//	List<Map<String, Object>> findNextDayAndTypes(String session);


	void updateNextDay(SmartCheckJob smartCheckJob);

	void updateSmartCheckResultTmp(String resultCode, String session, String errorMessage, String path);

	
	void saveAlarm(Map<String, Object> map);
    
    
//    //定时巡检
//   
//    
//    
//    Map<String, Object> findUnfinishedId();
//    
//    
//    CommandCheckItem searchCheckDetailTmp(Map<String, Object> map);
//	
//	Map<String, Object> getTimeOutCheckDetailTmpList(Map<String, Object> map);
//	
//	Map<String, Object> getCheckDetailTmpList(Map<String, Object> map);
//	
//	Map<String, Object> getAllJob(Map<String, Object> map);
//	
//	Map<String, Object> findJobIdByTime(Map<String, Object> map);
//    
//	void deleteByUUID(String uuId);
//	
//	void saveCheckResult(SmartCheckResult result);
//	
//	void saveCheckDetailTmp(List<SmartCheckResultTmp> list);
//	
//	void saveCheckDetailTmpList(Map<String, Object> map);
//	
//	void deleteTimeOutCheckDetailTmpList(Map<String, Object> map);
//	
//	void checkState(Map<String, Object> map);
    
}

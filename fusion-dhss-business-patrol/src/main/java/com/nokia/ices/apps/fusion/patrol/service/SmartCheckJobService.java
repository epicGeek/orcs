package com.nokia.ices.apps.fusion.patrol.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckJob;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckResult;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckResultTmp;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckScheduleResult;

public interface SmartCheckJobService {
	
	public List<Map<String, Object>> getAllDownLoadPath(String id);
	
	List<SmartCheckResult> findSmartCheckResultByFilter(Map<String,Object> map);

	Page<SmartCheckResult> getSmartCheckDetailResultPageList(Map<String, Object> map, Pageable pageable);

	Page<SmartCheckScheduleResult> getSmartCheckJobResultPageList(Map<String, Object> searchField, Pageable pageable);

	// Page<SmartCheckResult> getSmartCheckResultPageList(Map<String, Object>
	// map, Pageable pageable);


	int getSmartCheckJobCount(Map<String, Object> map);

	Map<String, Object> getSmartCheckResultPageList(Map<String, Object> map);
	
	List<Map<String, Object>> getSmartCheckCountResult();

	// Map<String, Object> getSmartCheckJob(Map<String, Object> map);

	// List<Map<String, Object>> getNeList(Map<String, Object> map);

	// List<Map<String, Object>> getCheckItemList(Map<String, Object> map);

	// List<Map<String, Object>> getCountNeAndItem(Map<String, Object> map);

	List<Map<String, Object>> getDownLoadPath(Map<String, Object> params);

	SmartCheckJob getCheckJobById(long id);

	Page<SmartCheckJob> getSmartCheckJob(String jobName, Object object, Pageable pageable);

	// List<Map<String, Object>> findNeAll();

	List<SmartCheckJob> getScheduleResult(Map<String, Object> map);

	// void saveScheduleResult(String thisTime, List<SmartCheckJob> jobList);

	List<SmartCheckResultTmp> getCheckDetailTmpList(Map<String, Object> map);

	List<EquipmentNe> findAllWithDHLR();

	List<SmartCheckResultTmp> getTimeOutCheckDetailTmpList(Map<String, Object> map);

	void saveCheckResult(SmartCheckResult result);

	List<SmartCheckScheduleResult> searchCheckDetailTmp(Map<String, Object> map);

	void invokeTimeOut(int threadTimeOut);

	void saveScheduleResult(Map<String, Object> m, List<SmartCheckJob> jobList);

	void sendMessage(SmartCheckJob smart, int status);

}

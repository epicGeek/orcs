package com.nokia.ices.apps.fusion.ems.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.nokia.ices.apps.fusion.command.domain.CommandCheckItem;
import com.nokia.ices.apps.fusion.ems.domain.EmsCheckJob;
import com.nokia.ices.apps.fusion.ems.domain.EmsMonitor;
import com.nokia.ices.apps.fusion.ems.domain.EmsMonitorHistory;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;

public interface EmsCheckTaskJobService {
	
	
	public List<EmsCheckJob> findJobByMap(Map<String,Object> map);
	
	
	public void updateJobExecTime(EmsCheckJob emsCheckJob) throws ParseException;
	
	
	public void execEmsJob(EmsCheckJob emsCheckJob);
	
	
	public List<EquipmentUnit> findUnits(Map<String,Object> map);
	
	
	public List<CommandCheckItem> findCommands(Map<String,Object> map);
	
	
	public void saveEmsResult(Map<String, Object> json,EquipmentUnit equipmentUnit,CommandCheckItem commandCheckItem,String groupId);
	
	
	public void sendMessageService(Map<String, Object> json,EquipmentUnit equipmentUnit,CommandCheckItem commandCheckItem);
	
	
	public void saveEmsMonitor(EmsMonitor emsMonitor);
	
	
	public List<EmsMonitor> findEmsMonitors(Map<String, Object> map);
	
	
	public void saveEmsMonitorHistory(EmsMonitorHistory emsMonitorHistory);
	
	
	public void sendMessageSms(String moblie,String smscontent);
	
	
	public void noticeGroup(String groupId,String message,String unit, String item,String msg,boolean flag,String command);
	
	
	public int isNotCancel(String unit,String item );


	void taskErrorSendMessage(String message);
		

}

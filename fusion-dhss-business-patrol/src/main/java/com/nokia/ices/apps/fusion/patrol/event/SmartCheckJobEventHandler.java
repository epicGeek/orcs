package com.nokia.ices.apps.fusion.patrol.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.collection.internal.PersistentSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterLinkDelete;
import org.springframework.data.rest.core.annotation.HandleAfterLinkSave;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckJob;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckScheduleResult;
import com.nokia.ices.apps.fusion.patrol.repository.SmartCheckResultRepository;
import com.nokia.ices.apps.fusion.patrol.repository.SmartCheckResultTmpRepository;
import com.nokia.ices.apps.fusion.patrol.repository.SmartCheckScheduleResultRepository;
import com.nokia.ices.apps.fusion.patrol.service.SmartCheckJobService;
import com.nokia.ices.apps.fusion.system.domain.SystemOperationLog;
import com.nokia.ices.apps.fusion.system.repository.SystemOperationLogRepository;
import com.nokia.ices.apps.fusion.system.repository.types.SystemOperationLogOpType;

@RepositoryEventHandler(SmartCheckJob.class)
public class SmartCheckJobEventHandler {
	

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	
	@Autowired
	private SmartCheckJobService  smartCheckJobService;
	private static final Logger log = LoggerFactory.getLogger(SmartCheckJobEventHandler.class);


	@HandleBeforeCreate
	public void handleBeforeCreate(SmartCheckJob smartCheckJob) {
//		smartCheckJob.setIsDisable(1);
		try {
			generateMissed(smartCheckJob);
			
			//增加方案
//			smartCheckJobService.sendMessage(smartCheckJob,0);
		} catch ( Exception e) {
			e.printStackTrace();
		}
	}
	@HandleBeforeSave
	public void handleBeforeSave(SmartCheckJob smartCheckJob) {
//		smartCheckJob.setIsDisable(1);
		try {
			generateMissed(smartCheckJob);
			//修改方案
//			smartCheckJobService.sendMessage(smartCheckJob,1);
		} catch ( Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static void generateMissed(SmartCheckJob smartCheckJob)
			throws ParseException {

		String currentExecTime = smartCheckJob.getExecTime();
		System.out.println(currentExecTime);
		Date startDay = format.parse(currentExecTime);
		
		Long startDateTime = startDay.getTime();
		smartCheckJob.setNextDay(startDay);
		smartCheckJob.setLoopHour(timeFormat.format(startDateTime));
		smartCheckJob.setExecDay(startDay);
		
	}
	
	
	
	@HandleBeforeDelete
	public void HandleBeforeDelete(SmartCheckJob smartCheckJob){
		//删除方案
//		smartCheckJobService.sendMessage(smartCheckJob,2);
		
		List<SmartCheckScheduleResult> schedule = smartCheckScheduleResultRepository.findListBySmartCheckJob(smartCheckJob);
		smartCheckScheduleResultRepository.delete(schedule);
		
		
	}
	
	
	
	public static final String smartCheckItemManage = "巡检方案配置";
	@Autowired
	private SystemOperationLogRepository systemOperationLogRepository;
	
	@Autowired
	SmartCheckScheduleResultRepository smartCheckScheduleResultRepository;
	
	@Autowired
	SmartCheckResultTmpRepository smartCheckResultTmpRepository;
	
	@Autowired
	SmartCheckResultRepository smartCheckResultRepository;

	
	private SmartCheckJob smartCheckJob;
	
	@HandleAfterCreate
	public void handleAfterCreate(SmartCheckJob smartCheckJob) {
		this.smartCheckJob = smartCheckJob;
		saveSystemOperationLog("增加巡检方案 ",SystemOperationLogOpType.Add);
	}
	
	@HandleAfterSave
	public void handleAfterSave(SmartCheckJob smartCheckJob) {
		this.smartCheckJob = smartCheckJob;
		saveSystemOperationLog("修改巡检方案",SystemOperationLogOpType.Update);
	}
	
	@HandleAfterDelete
	public void handleAfterDelete(SmartCheckJob smartCheckJob) {
		this.smartCheckJob = smartCheckJob;
		saveSystemOperationLog("删除巡检方案 ",SystemOperationLogOpType.Del);
	}
	
	
	
	@HandleAfterLinkSave
    @HandleAfterLinkDelete
    public void handleAfterLinkSave(SmartCheckJob smartCheckJob,PersistentSet obj){
		System.out.println("================="+obj.getRole());
		String clazz = obj.getRole().substring(obj.getRole().lastIndexOf(".")+1);
		String name = clazz.equals("checkItem") ? "修改指令配置" : "修改网元配置";
    	this.smartCheckJob = smartCheckJob;
    	saveSystemOperationLog(name,SystemOperationLogOpType.Update);
    }
	
	public void saveSystemOperationLog( String OpText,SystemOperationLogOpType OpType) {
		SystemOperationLog systemOperationLog = new SystemOperationLog();
		systemOperationLog.setLoginUserName(smartCheckJob.getCreator());
		systemOperationLog.setApp("DHSS");
		systemOperationLog.setAppModule(smartCheckItemManage);
		systemOperationLog.setOpText(OpText +" "+smartCheckJob.getJobName());
		systemOperationLog.setOpType(OpType);
		systemOperationLog.setLogTime(new Date());
		systemOperationLogRepository.save(systemOperationLog);
	}
}

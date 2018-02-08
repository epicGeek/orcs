package com.nokia.ices.apps.fusion.system.event;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterLinkDelete;
import org.springframework.data.rest.core.annotation.HandleAfterLinkSave;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import com.nokia.ices.apps.fusion.system.domain.SystemArea;
import com.nokia.ices.apps.fusion.system.domain.SystemOperationLog;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.system.repository.SystemOperationLogRepository;
import com.nokia.ices.apps.fusion.system.repository.types.SystemOperationLogOpType;

@RepositoryEventHandler(SystemArea.class)
public class SystemAreaEvent {
	public static final String areaManage = "地区管理";
	@Autowired
	private SystemOperationLogRepository systemOperationLogRepository;

	
	private SystemArea systemArea;
	
	@HandleAfterCreate
	public void handleAfterCreate(SystemArea systemArea) {
		this.systemArea = systemArea;
		saveSystemOperationLog("增加地区 ",SystemOperationLogOpType.Add);
	}
	
	@HandleAfterSave
	public void handleAfterSave(SystemArea systemArea) {
		this.systemArea = systemArea;
		saveSystemOperationLog("修改地区",SystemOperationLogOpType.Update);
	}
	
	@HandleAfterDelete
	public void handleAfterDelete(SystemArea systemArea) {
		this.systemArea = systemArea;
		saveSystemOperationLog("删除地区 ",SystemOperationLogOpType.Del);
	}
	
	
	/*@HandleAfterLinkSave
    @HandleAfterLinkDelete
    public void handleAfterLinkSave(SystemArea systemArea,Object obj){
    	this.systemArea = systemArea;
    	saveSystemOperationLog("修改地区配置 ",SystemOperationLogOpType.Update);
    }*/
	
	public void saveSystemOperationLog( String OpText,SystemOperationLogOpType OpType) {
		SystemOperationLog systemOperationLog = new SystemOperationLog();
		systemOperationLog.setLoginUserName(systemArea.getCreator());
		systemOperationLog.setApp("DHSS");
		systemOperationLog.setAppModule(areaManage);
		systemOperationLog.setOpText(OpText +" "+systemArea.getAreaName());
		systemOperationLog.setOpType(OpType);
		systemOperationLog.setLogTime(new Date());
		systemOperationLogRepository.save(systemOperationLog);
	}

}

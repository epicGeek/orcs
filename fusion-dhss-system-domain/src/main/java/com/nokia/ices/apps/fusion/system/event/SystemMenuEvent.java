package com.nokia.ices.apps.fusion.system.event;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import com.nokia.ices.apps.fusion.system.domain.SystemMenu;
import com.nokia.ices.apps.fusion.system.domain.SystemOperationLog;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.system.repository.SystemOperationLogRepository;
import com.nokia.ices.apps.fusion.system.repository.types.SystemOperationLogOpType;

@RepositoryEventHandler(SystemMenu.class)
public class SystemMenuEvent {
	public static final String menuManage = "菜单管理";
	@Autowired
	private SystemOperationLogRepository systemOperationLogRepository;

	
	private SystemMenu systemMenu;
	
	@HandleAfterCreate
	public void handleAfterCreate(SystemMenu systemMenu) {
		this.systemMenu = systemMenu;
		saveSystemOperationLog("增加菜单 ",SystemOperationLogOpType.Add);
	}
	
	@HandleAfterSave
	public void handleAfterSave(SystemMenu systemMenu) {
		this.systemMenu = systemMenu;
		saveSystemOperationLog("修改菜单",SystemOperationLogOpType.Update);
	}
	
	@HandleAfterDelete
	public void handleAfterDelete(SystemMenu systemMenu) {
		this.systemMenu = systemMenu;
		
		saveSystemOperationLog("删除菜单 ",SystemOperationLogOpType.Del);
	}
	
	@HandleBeforeDelete
	public void handleBeforeDelete(SystemMenu systemMenu) {
		this.systemMenu = systemMenu;
		Set<SystemRole> roles = systemMenu.getSystemRole();
		for (SystemRole systemRole : roles) {
			systemRole.getSystemResource().remove(systemMenu);
		}
	}
	
	public void saveSystemOperationLog( String OpText,SystemOperationLogOpType OpType) {
		SystemOperationLog systemOperationLog = new SystemOperationLog();
		systemOperationLog.setLoginUserName(systemMenu.getCreator());
		systemOperationLog.setApp("DHSS");
		systemOperationLog.setAppModule(menuManage);
		systemOperationLog.setOpText(OpText +" "+systemMenu.getMenuName());
		systemOperationLog.setOpType(OpType);
		systemOperationLog.setLogTime(new Date());
		systemOperationLogRepository.save(systemOperationLog);
	}

}

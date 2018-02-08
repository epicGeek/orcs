package com.nokia.ices.apps.fusion.command.event;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterLinkDelete;
import org.springframework.data.rest.core.annotation.HandleAfterLinkSave;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import com.nokia.ices.apps.fusion.command.domain.CommandCheckItem;
import com.nokia.ices.apps.fusion.system.domain.SystemOperationLog;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.system.repository.SystemOperationLogRepository;
import com.nokia.ices.apps.fusion.system.repository.types.SystemOperationLogOpType;

@RepositoryEventHandler(CommandCheckItem.class)
public class CommandCheckItemEvent {
	public static final String checkItemManage = "指令管理";
	@Autowired
	private SystemOperationLogRepository systemOperationLogRepository;

	
	private CommandCheckItem checkItem;
	
	@HandleAfterCreate
	public void handleAfterCreate(CommandCheckItem checkItem) {
		this.checkItem = checkItem;
		saveSystemOperationLog("增加指令 ",SystemOperationLogOpType.Add);
	}
	
	@HandleAfterSave
	public void handleAfterSave(CommandCheckItem checkItem) {
		this.checkItem = checkItem;
		saveSystemOperationLog("修改指令",SystemOperationLogOpType.Update);
	}
	
	@HandleAfterDelete
	public void handleAfterDelete(CommandCheckItem checkItem) {
		this.checkItem = checkItem;
		saveSystemOperationLog("删除指令 ",SystemOperationLogOpType.Del);
	}
	
	@HandleAfterLinkSave
    @HandleAfterLinkDelete
    public void handleAfterLinkSave(CommandCheckItem checkItem,Object obj){
    	this.checkItem = checkItem;
    	saveSystemOperationLog("修改指令配置 ",SystemOperationLogOpType.Update);
    }
	
	public void saveSystemOperationLog( String OpText,SystemOperationLogOpType OpType) {
		SystemOperationLog systemOperationLog = new SystemOperationLog();
		systemOperationLog.setLoginUserName(checkItem.getCreator());
		systemOperationLog.setApp("DHSS");
		systemOperationLog.setAppModule(checkItemManage);
		systemOperationLog.setOpText(OpText +" "+checkItem.getName());
		systemOperationLog.setOpType(OpType);
		systemOperationLog.setLogTime(new Date());
		systemOperationLogRepository.save(systemOperationLog);
	}

}

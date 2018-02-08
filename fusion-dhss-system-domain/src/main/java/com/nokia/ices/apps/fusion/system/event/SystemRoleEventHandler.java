package com.nokia.ices.apps.fusion.system.event;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.collection.internal.PersistentSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterLinkDelete;
import org.springframework.data.rest.core.annotation.HandleAfterLinkSave;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import com.nokia.ices.apps.fusion.system.domain.SystemOperationLog;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.system.repository.SystemOperationLogRepository;
import com.nokia.ices.apps.fusion.system.repository.types.SystemOperationLogOpType;

@RepositoryEventHandler(SystemRole.class)
public class SystemRoleEventHandler {
	
	private  static final String userGroupManage = "用户组管理";
    
    @Autowired
    private SystemOperationLogRepository systemOperationLogRepository;
    
    private SystemRole role;
    
    @HandleBeforeCreate
    public void handleBeforeCreate(SystemRole role) {
        
    }
    @HandleAfterCreate
    public void handleAfterCreate(SystemRole role){
        String roleCreatorPath = StringUtils.substring(role.getCreator(),role.getCreator().indexOf("/"));
        role.setPath(roleCreatorPath+role.getId()+"/");
        this.role = role;
        saveSystemLog("新增角色 ",SystemOperationLogOpType.Add);
    }
    
    @HandleAfterSave
    public void handleAfterSave(SystemRole role){
    	this.role = role;
    	saveSystemLog("修改角色",SystemOperationLogOpType.Update);
    }
    
    @HandleAfterDelete
    public void handleAfterDelete(SystemRole role){
    	this.role = role;
    	saveSystemLog("删除角色",SystemOperationLogOpType.Del);
    }
    
    @HandleAfterLinkSave
    @HandleAfterLinkDelete
    public void handleAfterLinkSave(SystemRole role,PersistentSet ps){
    	this.role = role;
    	saveSystemLog("修改角色权限 ",SystemOperationLogOpType.Update);
    }
    
    
    public void saveSystemLog(String opText,SystemOperationLogOpType opType){
    	SystemOperationLog systemOperationLogItem = new SystemOperationLog();
        systemOperationLogItem.setApp("DHSS");
        systemOperationLogItem.setAppModule(userGroupManage);
        systemOperationLogItem.setOpType(opType);
        systemOperationLogItem.setLogTime(new Date());
        systemOperationLogItem.setOpText(opText + role.getRoleName());
        systemOperationLogItem.setLoginUserName(role.getCreator());
        systemOperationLogRepository.save(systemOperationLogItem);
    }
}

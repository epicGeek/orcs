package com.nokia.ices.apps.fusion.equipment.event;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterLinkDelete;
import org.springframework.data.rest.core.annotation.HandleAfterLinkSave;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe;
import com.nokia.ices.apps.fusion.system.domain.SystemOperationLog;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.system.repository.SystemOperationLogRepository;
import com.nokia.ices.apps.fusion.system.repository.types.SystemOperationLogOpType;

@RepositoryEventHandler(EquipmentNe.class)
public class EquipmentNeEvent {
	public static final String networkElementManage = "网元管理";
	@Autowired
	private SystemOperationLogRepository systemOperationLogRepository;

	private EquipmentNe equipmentNe;
	
	@HandleAfterCreate
	public void handleAfterCreate(EquipmentNe equipmentNe) {
		this.equipmentNe = equipmentNe;
		saveSystemOperationLog("增加网元 ",SystemOperationLogOpType.Add);
	}
	
	@HandleAfterSave
	public void handleAfterSave(EquipmentNe equipmentNe) {
		this.equipmentNe = equipmentNe;
		saveSystemOperationLog("修改网元 ",SystemOperationLogOpType.Update);
	}
	
	@HandleAfterDelete
	public void handleAfterDelete(EquipmentNe equipmentNe) {
		this.equipmentNe = equipmentNe;
		saveSystemOperationLog("删除网元 ",SystemOperationLogOpType.Del);
	}
	
	 @HandleAfterLinkSave
	 @HandleAfterLinkDelete
	 public void handleAfterLinkSave(EquipmentNe equipmentNe,Object obj){
	    this.equipmentNe = equipmentNe;
	    saveSystemOperationLog("修改网元配置 ",SystemOperationLogOpType.Update);
	 }
	
	public void saveSystemOperationLog( String OpText,SystemOperationLogOpType OpType) {
		SystemOperationLog systemOperationLog = new SystemOperationLog();
		systemOperationLog.setLoginUserName(equipmentNe.getCreator());
		systemOperationLog.setApp("DHSS");
		systemOperationLog.setAppModule(networkElementManage);
		systemOperationLog.setOpText(OpText+" "+equipmentNe.getNeName());
		systemOperationLog.setOpType(OpType);
		systemOperationLog.setLogTime(new Date());
		systemOperationLogRepository.save(systemOperationLog);
	}

}

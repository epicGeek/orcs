package com.nokia.ices.apps.fusion.equipment.event;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNumberSection;
import com.nokia.ices.apps.fusion.system.domain.SystemOperationLog;
import com.nokia.ices.apps.fusion.system.repository.SystemOperationLogRepository;
import com.nokia.ices.apps.fusion.system.repository.types.SystemOperationLogOpType;

@RepositoryEventHandler(EquipmentNumberSection.class)
public class EquipmentNumberSectionEvent {
	public static final String equipmentNumberSectionManage = "号段管理";
	@Autowired
	private SystemOperationLogRepository systemOperationLogRepository;

	private EquipmentNumberSection equipmentNumberSection;
	
	@HandleAfterCreate
	public void handleAfterCreate(EquipmentNumberSection equipmentNumberSection) {
		this.equipmentNumberSection = equipmentNumberSection;
		saveSystemOperationLog("增加号段 ",SystemOperationLogOpType.Add);
	}
	
	@HandleAfterSave
	public void handleAfterSave(EquipmentNumberSection equipmentNumberSection) {
		this.equipmentNumberSection = equipmentNumberSection;
		saveSystemOperationLog("修改号段 ",SystemOperationLogOpType.Update);
	}
	
	@HandleAfterDelete
	public void handleAfterDelete(EquipmentNumberSection equipmentNumberSection) {
		this.equipmentNumberSection = equipmentNumberSection;
		saveSystemOperationLog("删除号段 ",SystemOperationLogOpType.Del);
	}
	
	public void saveSystemOperationLog( String OpText,SystemOperationLogOpType OpType) {
		SystemOperationLog systemOperationLog = new SystemOperationLog();
		systemOperationLog.setLoginUserName(equipmentNumberSection.getCreator());
		systemOperationLog.setApp("DHSS");
		systemOperationLog.setAppModule(equipmentNumberSectionManage);
		systemOperationLog.setOpText(OpText+" "+equipmentNumberSection.getNumber());
		systemOperationLog.setOpType(OpType);
		systemOperationLog.setLogTime(new Date());
		systemOperationLogRepository.save(systemOperationLog);
	}

}

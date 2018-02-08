package com.nokia.ices.apps.fusion.node.isolation.service;


import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nokia.ices.apps.fusion.emergency.domian.EmergencySecurityState;
import com.nokia.ices.apps.fusion.emergency.domian.StepConfTable;
import com.nokia.ices.apps.fusion.emergency.domian.StepExecute;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;



public interface EmergencySecurityService {

	public List<String> queryLocation();

	public List<Map<String, Object>> queryNeType(String location);

	public List<Map<String, Object>> queryNe(String location);

	public List<Map<String, Object>> getAllNe();

	public int getNeTypeByUnit(int unitId);

	public int getNeTypeByNeId(int neId);

	public String getNeTypeNameByNeId(int neId);

	public String getNeNameById(int neId);

	public String getUnitNameById(int unitId);

	public List<Map<String, Object>> queryUnitByNe(Map<String, Object> params);

	public List<Map<String, Object>> queryUnit(Map<String, Object> params);

	public List<StepConfTable> getStepByType(Map<String, Object> params);


	public void saveEmergencySecurity(Map<String, Object> params);

	public void saveStepExecute(Map<String, Object> params);

	public EmergencySecurityState getEmergencySecurityByUnit(Map<String, Object> params);

	public String getOperateStateByUnit(Map<String, Object> params);

	public String getOperateStateByNe(Map<String, Object> params);

	public List<StepExecute> queryEmergencySecurity(
			Map<String, Object> params);

	public Page<EmergencySecurityState> querySecurityState(
			Map<String, Object> params,Pageable pageable);
	public List<EmergencySecurityState> querySecurityStateList(
			Map<String, Object> params);

	public int getSecurityStateTotal(Map<String, Object> params);

	public void updateStepExecute(Map<String, Object> params);

	public void updateEmergencySecurity(Map<String, Object> params);

	public void updateReturnInfo(Map<String, Object> params);

	public List<EquipmentUnit> queryAccount(Map<String, Object> params);

}

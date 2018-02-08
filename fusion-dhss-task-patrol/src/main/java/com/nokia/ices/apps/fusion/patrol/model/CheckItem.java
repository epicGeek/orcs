package com.nokia.ices.apps.fusion.patrol.model;

import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentNeType;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;


public class CheckItem {
	private Long id; 
	private String uuId;
    private Long unitId;
    private String unitName;
    private String command;
    private String username;
    private String rootPwd;
    private int neId;
    private int neTypeId;
    private String neTypeName;
    private String neName;
    private int unitTypeId;
    private String unitTypeName;
    private Long scheduleId;
    private String startTime;
    private Long checkItemId;
    private String checkItemName;
    private int execFlag;
    private Short resultCode;
    private String errorMessage;
    private String filePath;
    private String logContents;//报文    
	private Short logState;
    private String script;//LUA脚本
    private String jobName;
    private EquipmentNeType neType;
    private EquipmentUnitType unitType;
    
    public EquipmentUnitType getUnitType() {
		return unitType;
	}
	public void setUnitType(EquipmentUnitType unitType) {
		this.unitType = unitType;
	}
	public EquipmentNeType getNeType() {
		return neType;
	}
	public void setNeType(EquipmentNeType neType) {
		this.neType = neType;
	}
	public Long getId() {
 		return id;
 	}
 	public void setId(Long id) {
 		this.id = id;
 	}
    public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public Short getLogState() {
		return logState;
	}
	public void setLogState(Short logState) {
		this.logState = logState;
	}
	public void setUuId(String uuId) {
		this.uuId = uuId;
	}
	public String getUuId() {
		return uuId;
	}
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRootPwd() {
		return rootPwd;
	}
	public void setRootPwd(String rootPwd) {
		this.rootPwd = rootPwd;
	}
	public int getNeId() {
		return neId;
	}
	public void setNeId(int neId) {
		this.neId = neId;
	}
	public int getNeTypeId() {
		return neTypeId;
	}
	public void setNeTypeId(int neTypeId) {
		this.neTypeId = neTypeId;
	}
	public String getNeTypeName() {
		return neTypeName;
	}
	public void setNeTypeName(String neTypeName) {
		this.neTypeName = neTypeName;
	}
	public String getNeName() {
		return neName;
	}
	public void setNeName(String neName) {
		this.neName = neName;
	}
	public int getUnitTypeId() {
		return unitTypeId;
	}
	public void setUnitTypeId(int unitTypeId) {
		this.unitTypeId = unitTypeId;
	}
	public String getUnitTypeName() {
		return unitTypeName;
	}
	public void setUnitTypeName(String unitTypeName) {
		this.unitTypeName = unitTypeName;
	}
	public Long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public Long getCheckItemId() {
		return checkItemId;
	}
	public void setCheckItemId(Long checkItemId) {
		this.checkItemId = checkItemId;
	}
	public String getCheckItemName() {
		return checkItemName;
	}
	public void setCheckItemName(String checkItemName) {
		this.checkItemName = checkItemName;
	}
	public int getExecFlag() {
		return execFlag;
	}
	public void setExecFlag(int execFlag) {
		this.execFlag = execFlag;
	}
	public Short getResultCode() {
		return resultCode;
	}
	public void setResultCode(Short resultCode) {
		this.resultCode = resultCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getLogContents() {
		return logContents;
	}
	public void setLogContents(String logContents) {
		this.logContents = logContents;
	}
}
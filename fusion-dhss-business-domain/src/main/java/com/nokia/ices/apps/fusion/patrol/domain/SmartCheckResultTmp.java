package com.nokia.ices.apps.fusion.patrol.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentNeType;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;

@Entity
@Table(name = "smart_check_result_tmp")
public class SmartCheckResultTmp {

    @Id
    @GeneratedValue
    private Long id;

    private String uuId;

    // 单元ID
    private Long unitId;

    // 单元名称
    private String unitName;

    // 指令
    @Column(length = 1024)
    private String command;
    
    @Column(length=4096)
	private String script;
    
    public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	private String port;

    public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	private String protocol;

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	private String ip;
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	// 需要切换的用户
    private String userName;

    // 16进制的rootpwd
    private String rootPwd;
    
    private String loginPwd;

    public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	// unsigned网元ID
    private Long neId;


    // 网元名称
    private String neName;

    @Enumerated(EnumType.STRING)
    private EquipmentNeType neType;

    @Enumerated(EnumType.STRING)
    private EquipmentUnitType unitType;

    // 检查任务ID
    private Long scheduleId;

    // 巡检开始时间
    private Date startTime;

    // 检查项ID
    private Long checkItemId;

    // 检查项名称
    private String checkItemName;

    // 执行状态 0：等待执行 1：执行中 2：执行成功 3：执行失败
    private SmartCheckResultTmpExecFlag execFlag;

    // 巡检结果 1：失败 0：成功
    private boolean resultCode;

    // 出错信息
    @Column(length=4096)
    private String errorMessage;

    // 报文路径
    private String filePath;
    
    private String hostname;
    
    private String netFlag;


    public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getNetFlag() {
		return netFlag;
	}

	public void setNetFlag(String netFlag) {
		this.netFlag = netFlag;
	}

    // 日志内容
    private String logContents;

    private boolean logState;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRootPwd() {
        return rootPwd;
    }

    public void setRootPwd(String rootPwd) {
        this.rootPwd = rootPwd;
    }

    public Long getNeId() {
        return neId;
    }

    public void setNeId(Long neId) {
        this.neId = neId;
    }

    public String getNeName() {
        return neName;
    }

    public void setNeName(String neName) {
        this.neName = neName;
    }

    public EquipmentNeType getNeType() {
        return neType;
    }

    public void setNeType(EquipmentNeType neType) {
        this.neType = neType;
    }

    public EquipmentUnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(EquipmentUnitType unitType) {
        this.unitType = unitType;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
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

    public SmartCheckResultTmpExecFlag getExecFlag() {
        return execFlag;
    }

    public void setExecFlag(SmartCheckResultTmpExecFlag execFlag) {
        this.execFlag = execFlag;
    }

    public boolean isResultCode() {
        return resultCode;
    }

    public void setResultCode(boolean resultCode) {
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

    public boolean isLogState() {
        return logState;
    }

    public void setLogState(boolean logState) {
        this.logState = logState;
    }
}

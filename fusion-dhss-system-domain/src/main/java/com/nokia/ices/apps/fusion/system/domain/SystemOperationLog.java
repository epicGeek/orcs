package com.nokia.ices.apps.fusion.system.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.nokia.ices.apps.fusion.system.repository.types.SystemOperationLogOpType;

@Entity
public class SystemOperationLog implements Serializable {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale="zh", timezone="GMT+8")
	private Date LogTime;
	
	private String loginUserName;
	
	private String App;
	
	private String Sip;
	
	private String AppModule;
	
	@Enumerated(EnumType.STRING)
	private SystemOperationLogOpType OpType;
	
	
	@Column(length=4096)
	private String OpText;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogTime() {
		return sdf.format(LogTime);
	}

	public void setLogTime(Date logTime) {
		LogTime = logTime;
	}

	
	public String getLoginUserName() {
		return loginUserName;
	}

	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}

	public String getApp() {
		return App;
	}

	public void setApp(String app) {
		App = app;
	}

	public String getSip() {
		return Sip;
	}

	public void setSip(String sip) {
		Sip = sip;
	}

	public String getAppModule() {
		return AppModule;
	}

	public void setAppModule(String appModule) {
		AppModule = appModule;
	}

	public SystemOperationLogOpType getOpType() {
        return OpType;
    }

    public void setOpType(SystemOperationLogOpType opType) {
        OpType = opType;
    }

    public String getOpText() {
		return OpText;
	}

	public void setOpText(String opText) {
		OpText = opText;
	}

	

}

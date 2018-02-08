package com.nokia.ices.apps.fusion.maintain.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nokia.ices.apps.fusion.command.domain.CommandCheckItem;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;

@Entity
public class MaintainResult implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private EquipmentNe ne;

    private String reportPath;
    
    private String errorLog;
    
    private String uuId;

    @ManyToOne
    private EquipmentUnit unit;
    
    @ManyToOne
    private CommandCheckItem commandCheckItem;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestTime;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale="zh", timezone="GMT+8")
    private Date responseTime;

    @ManyToOne
    private MaintainOperation operation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EquipmentNe getNe() {
        return ne;
    }
    
    

    public String getErrorLog() {
		return errorLog;
	}

	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}

	public void setNe(EquipmentNe ne) {
        this.ne = ne;
    }

    public String getReportPath() {
        return reportPath;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    public EquipmentUnit getUnit() {
        return unit;
    }

    public void setUnit(EquipmentUnit unit) {
        this.unit = unit;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public MaintainOperation getOperation() {
        return operation;
    }

    public void setOperation(MaintainOperation operation) {
        this.operation = operation;
    }

	public CommandCheckItem getCommandCheckItem() {
		return commandCheckItem;
	}

	public void setCommandCheckItem(CommandCheckItem commandCheckItem) {
		this.commandCheckItem = commandCheckItem;
	}

	public String getUuId() {
		return uuId;
	}

	public void setUuId(String uuId) {
		this.uuId = uuId;
	}
    
}

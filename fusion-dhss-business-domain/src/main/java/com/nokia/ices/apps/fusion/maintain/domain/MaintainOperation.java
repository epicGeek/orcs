package com.nokia.ices.apps.fusion.maintain.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nokia.ices.apps.fusion.command.domain.types.CommandCategory;

@Entity
public class MaintainOperation implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue
    private Long id;
    
   
    private String createBy;
    
    private String checkName;
    
    
    
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale="zh", timezone="GMT+8")
    private Date requestTime;
    
    
    private Boolean isDone;


    public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public String getCheckName() {
		return checkName;
	}

	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}

	public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }


    @Enumerated(EnumType.STRING)
	private CommandCategory commandCategory;

   

    @OneToMany(mappedBy="operation")
    @JsonIgnore
    private Set<MaintainResult> result;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	

	public CommandCategory getCommandCategory() {
		return commandCategory;
	}

	public void setCommandCategory(CommandCategory commandCategory) {
		this.commandCategory = commandCategory;
	}

	public Set<MaintainResult> getResult() {
		return result;
	}

	public void setResult(Set<MaintainResult> result) {
		this.result = result;
	}
    
	public Long getOperationId() {
		 return getId();
	}
}

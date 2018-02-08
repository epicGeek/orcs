package com.nokia.ices.apps.fusion.console.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@EntityListeners({AuditingEntityListener.class})
public class ConsoleLog {
    /**
     * 
     */
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String loginUserName;
    
    private String loginUnitName;


    @CreatedDate
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale="zh", timezone="GMT+8")
    private Date startTime;

    private String logPath;

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Date getStartTime() {
        return startTime;
    }


    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getLogPath() {
        return logPath;
    }


    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getLoginUserName() {
        return loginUserName;
    }


    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }


    public String getLoginUnitName() {
        return loginUnitName;
    }


    public void setLoginUnitName(String loginUnitName) {
        this.loginUnitName = loginUnitName;
    }
}

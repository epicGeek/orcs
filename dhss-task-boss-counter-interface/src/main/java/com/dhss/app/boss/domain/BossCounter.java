package com.dhss.app.boss.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class BossCounter {
	@Id
	@GeneratedValue
	private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
	private Date counterStartTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
	private Date counterEndTime;
	private Integer counter;
	private String hlrsn;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCounterStartTime() {
		return counterStartTime;
	}
	public void setCounterStartTime(Date counterStartTime) {
		this.counterStartTime = counterStartTime;
	}
	public Date getCounterEndTime() {
		return counterEndTime;
	}
	public void setCounterEndTime(Date counterEndTime) {
		this.counterEndTime = counterEndTime;
	}
	public Integer getCounter() {
		return counter;
	}
	public void setCounter(Integer counter) {
		this.counter = counter;
	}
	public String getHlrsn() {
		return hlrsn;
	}
	public void setHlrsn(String hlrsn) {
		this.hlrsn = hlrsn;
	}
	
}

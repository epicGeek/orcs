package com.nokia.ices.apps.notifier.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class NotificationMessageRecord implements Serializable{
	private static final long serialVersionUID = -7842706187944145827L;
	@Id
	@GeneratedValue
	private Long id;
	private String mobile;
	private String smscontent;
	private String smsport;
	private Date sendDate;
	
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getSmscontent() {
		return smscontent;
	}
	public void setSmscontent(String smscontent) {
		this.smscontent = smscontent;
	}
	public String getSmsport() {
		return smsport;
	}
	public void setSmsport(String smsport) {
		this.smsport = smsport;
	}
}

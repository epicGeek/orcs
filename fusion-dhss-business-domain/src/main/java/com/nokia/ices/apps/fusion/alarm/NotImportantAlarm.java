package com.nokia.ices.apps.fusion.alarm;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;

@Entity
public class NotImportantAlarm {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(length = 16777215)
	private String alarmNoArray;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<String> getAlarmNoList() {
		String [] array = alarmNoArray.split("_");
		List<String> list = new ArrayList<String>();
		for (String string : array) {
			if(!StringUtils.isNotEmpty(string)){
				continue;
			}
			list.add(string);
		}
		return list;
	}

	public String getAlarmNoArray() {
		return alarmNoArray;
	}

	public void setAlarmNoArray(String alarmNoArray) {
		this.alarmNoArray = alarmNoArray;
	}

}

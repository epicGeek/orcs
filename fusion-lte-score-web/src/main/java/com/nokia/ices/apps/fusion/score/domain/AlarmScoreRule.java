package com.nokia.ices.apps.fusion.score.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 基站告警规则
 * @author Administrator
 */
@SuppressWarnings("serial")
@Entity
@Table(name="ices_delay_score_rule")
public class AlarmScoreRule implements Serializable{
	
	@Id
	@GeneratedValue  
	private int	id;
	private int	manufacturer;
	private int alarmNo;
	private String	 alarmNameEn;
	private String	 alarmNameCn;
	private String	 alarmExplain;  //告警解释
	private String	 alarmType;  //告警分类 时钟:5 光路:2  传输:3 天馈:1 硬件:7  电源:4  软件:6
	private Integer	delayHour_1;
	private Integer	delayHour_2;
	private Integer	delayHour_3;
	private Integer	delayScore_1;
	private Integer	delayScore_2;
	private Integer	delayScore_3;
	private Integer	alarmFrequency;
	private Integer	frequency_1;
	private Integer	frequency_2;
	private Integer	frequency_3;
	private Integer	frequency_4;
	private Integer	freScore_1;
	private Integer	freScore_2;
	private Integer	freScore_3;
	private Integer	freScore_4;
	
	/**
	 * 检索字段
	 */
	@Transient
	private String searchField;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(int manufacturer) {
		this.manufacturer = manufacturer;
	}

	public int getAlarmNo() {
		return alarmNo;
	}
	public void setAlarmNo(int alarmNo) {
		this.alarmNo = alarmNo;
	}
	public String getAlarmNameEn() {
		return alarmNameEn;
	}
	public void setAlarmNameEn(String alarmNameEn) {
		this.alarmNameEn = alarmNameEn;
	}
	public String getAlarmNameCn() {
		return alarmNameCn;
	}
	public void setAlarmNameCn(String alarmNameCn) {
		this.alarmNameCn = alarmNameCn;
	}
	public String getAlarmExplain() {
		return alarmExplain;
	}

	public void setAlarmExplain(String alarmExplain) {
		this.alarmExplain = alarmExplain;
	}

	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	
	public Integer getDelayHour_1() {
		return delayHour_1;
	}
	public void setDelayHour_1(Integer delayHour_1) {
		this.delayHour_1 = delayHour_1;
	}
	public Integer getDelayHour_2() {
		return delayHour_2;
	}
	public void setDelayHour_2(Integer delayHour_2) {
		this.delayHour_2 = delayHour_2;
	}
	public Integer getDelayHour_3() {
		return delayHour_3;
	}
	public void setDelayHour_3(Integer delayHour_3) {
		this.delayHour_3 = delayHour_3;
	}
	public Integer getDelayScore_1() {
		return delayScore_1;
	}
	public void setDelayScore_1(Integer delayScore_1) {
		this.delayScore_1 = delayScore_1;
	}
	public Integer getDelayScore_2() {
		return delayScore_2;
	}
	public void setDelayScore_2(Integer delayScore_2) {
		this.delayScore_2 = delayScore_2;
	}
	public Integer getDelayScore_3() {
		return delayScore_3;
	}
	public void setDelayScore_3(Integer delayScore_3) {
		this.delayScore_3 = delayScore_3;
	}
	public Integer getAlarmFrequency() {
		return alarmFrequency;
	}
	public void setAlarmFrequency(Integer alarmFrequency) {
		this.alarmFrequency = alarmFrequency;
	}
	public Integer getFrequency_1() {
		return frequency_1;
	}
	public void setFrequency_1(Integer frequency_1) {
		this.frequency_1 = frequency_1;
	}
	public Integer getFrequency_2() {
		return frequency_2;
	}
	public void setFrequency_2(Integer frequency_2) {
		this.frequency_2 = frequency_2;
	}
	public Integer getFrequency_3() {
		return frequency_3;
	}
	public void setFrequency_3(Integer frequency_3) {
		this.frequency_3 = frequency_3;
	}
	public Integer getFrequency_4() {
		return frequency_4;
	}
	public void setFrequency_4(Integer frequency_4) {
		this.frequency_4 = frequency_4;
	}
	public Integer getFreScore_1() {
		return freScore_1;
	}
	public void setFreScore_1(Integer freScore_1) {
		this.freScore_1 = freScore_1;
	}
	public Integer getFreScore_2() {
		return freScore_2;
	}
	public void setFreScore_2(Integer freScore_2) {
		this.freScore_2 = freScore_2;
	}
	public Integer getFreScore_3() {
		return freScore_3;
	}
	public void setFreScore_3(Integer freScore_3) {
		this.freScore_3 = freScore_3;
	}
	public Integer getFreScore_4() {
		return freScore_4;
	}
	public void setFreScore_4(Integer freScore_4) {
		this.freScore_4 = freScore_4;
	}
	public String getSearchField() {
		return searchField;
	}
	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}

	
}

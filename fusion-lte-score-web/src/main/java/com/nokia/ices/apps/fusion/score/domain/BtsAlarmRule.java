package com.nokia.ices.apps.fusion.score.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基站退服规则
 */
@SuppressWarnings("serial")
@Entity
@Table(name="out_of_alarm_type")
public class BtsAlarmRule implements Serializable{
	
	@Id
	@GeneratedValue  
	private Long id;
	private Integer alarmNo;  //告警号
	private String networkType;
	private String alarmTitle;  //告警标题
	private String alarmType;  //告警对象类型
	private String alarmExplain; //告警解释
	private String alarmLogic;
	private String alarmLogicSubclass;
	private String neAlarmLevel;
	private String custom;
	private Integer customLevel;
	private Integer manufacturer;
	private Integer	delayOne;
	private Integer	delayTwo;
	private Integer	delayThree;
	private Integer	delayScoreOne;
	private Integer	delayScoreTwo;
	private Integer	delayScoreThree;
	private Integer	frequencyOne;
	private Integer	frequencyTwo;
	private Integer	frequencyThree;
	private Integer	frequencyFour;
	private Integer	freScoreOne;
	private Integer	freScoreTwo;
	private Integer	freScoreThree;
	private Integer	freScoreFour;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getAlarmNo() {
		return alarmNo;
	}
	public void setAlarmNo(Integer alarmNo) {
		this.alarmNo = alarmNo;
	}
	public String getNetworkType() {
		return networkType;
	}
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}
	public String getAlarmTitle() {
		return alarmTitle;
	}
	public void setAlarmTitle(String alarmTitle) {
		this.alarmTitle = alarmTitle;
	}
	public String getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	public String getAlarmExplain() {
		return alarmExplain;
	}
	public void setAlarmExplain(String alarmExplain) {
		this.alarmExplain = alarmExplain;
	}
	public String getAlarmLogic() {
		return alarmLogic;
	}
	public void setAlarmLogic(String alarmLogic) {
		this.alarmLogic = alarmLogic;
	}
	public String getAlarmLogicSubclass() {
		return alarmLogicSubclass;
	}
	public void setAlarmLogicSubclass(String alarmLogicSubclass) {
		this.alarmLogicSubclass = alarmLogicSubclass;
	}
	public String getNeAlarmLevel() {
		return neAlarmLevel;
	}
	public void setNeAlarmLevel(String neAlarmLevel) {
		this.neAlarmLevel = neAlarmLevel;
	}
	public String getCustom() {
		return custom;
	}
	public void setCustom(String custom) {
		this.custom = custom;
	}
	public Integer getCustomLevel() {
		return customLevel;
	}
	public void setCustomLevel(Integer customLevel) {
		this.customLevel = customLevel;
	}
	public Integer getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(Integer manufacturer) {
		this.manufacturer = manufacturer;
	}
	public Integer getDelayOne() {
		return delayOne;
	}
	public void setDelayOne(Integer delayOne) {
		this.delayOne = delayOne;
	}

	public Integer getDelayTwo() {
		return delayTwo;
	}
	public void setDelayTwo(Integer delayTwo) {
		this.delayTwo = delayTwo;
	}
	public Integer getDelayThree() {
		return delayThree;
	}
	public void setDelayThree(Integer delayThree) {
		this.delayThree = delayThree;
	}
	public Integer getDelayScoreOne() {
		return delayScoreOne;
	}
	public void setDelayScoreOne(Integer delayScoreOne) {
		this.delayScoreOne = delayScoreOne;
	}
	public Integer getDelayScoreTwo() {
		return delayScoreTwo;
	}
	public void setDelayScoreTwo(Integer delayScoreTwo) {
		this.delayScoreTwo = delayScoreTwo;
	}
	public Integer getDelayScoreThree() {
		return delayScoreThree;
	}
	public void setDelayScoreThree(Integer delayScoreThree) {
		this.delayScoreThree = delayScoreThree;
	}
	public Integer getFrequencyOne() {
		return frequencyOne;
	}
	public void setFrequencyOne(Integer frequencyOne) {
		this.frequencyOne = frequencyOne;
	}
	public Integer getFrequencyTwo() {
		return frequencyTwo;
	}
	public void setFrequencyTwo(Integer frequencyTwo) {
		this.frequencyTwo = frequencyTwo;
	}
	public Integer getFrequencyThree() {
		return frequencyThree;
	}
	public void setFrequencyThree(Integer frequencyThree) {
		this.frequencyThree = frequencyThree;
	}
	public Integer getFrequencyFour() {
		return frequencyFour;
	}
	public void setFrequencyFour(Integer frequencyFour) {
		this.frequencyFour = frequencyFour;
	}
	public Integer getFreScoreOne() {
		return freScoreOne;
	}
	public void setFreScoreOne(Integer freScoreOne) {
		this.freScoreOne = freScoreOne;
	}
	public Integer getFreScoreTwo() {
		return freScoreTwo;
	}
	public void setFreScoreTwo(Integer freScoreTwo) {
		this.freScoreTwo = freScoreTwo;
	}
	public Integer getFreScoreThree() {
		return freScoreThree;
	}
	public void setFreScoreThree(Integer freScoreThree) {
		this.freScoreThree = freScoreThree;
	}
	public Integer getFreScoreFour() {
		return freScoreFour;
	}
	public void setFreScoreFour(Integer freScoreFour) {
		this.freScoreFour = freScoreFour;
	}

	
}

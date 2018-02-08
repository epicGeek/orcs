package com.nokia.ices.apps.fusion.score.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="ices_emos_alarm_rule")
public class JobManageRule implements Serializable{
	
	@Id
	@GeneratedValue  
	private Long id;
	private Integer alarmNo;  //告警号
	/*'告警类别 0：未定义 1:操作维护 2:处理器模块 3:动力环境告警 4:话务处理
	 *  5:设备告警 6:输入输出外部设备 7:数据配置 8：信令与ip 9：中继与传输'*/
	private Integer alarmType;  //不能为空 
	//'告警级别:0:无 1:一级,2:二级,3:三级,4:四级'
	private Integer alarmLevel;  //不能为空
	private String  alarmTitle;  //告警标题
	private String alarmDesc;    //告警含义
	private Integer rule_1;
	private Integer rule_2;
	private Integer rule_3;
	private Integer rule_4;
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
	public Integer getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(Integer alarmType) {
		this.alarmType = alarmType;
	}
	public Integer getAlarmLevel() {
		return alarmLevel;
	}
	public void setAlarmLevel(Integer alarmLevel) {
		this.alarmLevel = alarmLevel;
	}
	public String getAlarmTitle() {
		return alarmTitle;
	}
	public void setAlarmTitle(String alarmTitle) {
		this.alarmTitle = alarmTitle;
	}
	public String getAlarmDesc() {
		return alarmDesc;
	}
	public void setAlarmDesc(String alarmDesc) {
		this.alarmDesc = alarmDesc;
	}
	public Integer getRule_1() {
		return rule_1;
	}
	public void setRule_1(Integer rule_1) {
		this.rule_1 = rule_1;
	}
	public Integer getRule_2() {
		return rule_2;
	}
	public void setRule_2(Integer rule_2) {
		this.rule_2 = rule_2;
	}
	public Integer getRule_3() {
		return rule_3;
	}
	public void setRule_3(Integer rule_3) {
		this.rule_3 = rule_3;
	}
	public Integer getRule_4() {
		return rule_4;
	}
	public void setRule_4(Integer rule_4) {
		this.rule_4 = rule_4;
	}
	
	
}

package com.nokia.ices.apps.fusion.score.domain;

import java.util.Date;

/**
 * 健康度评分
 * 
 * */
public class BtsScoreHour{
	

	//基站
	private String  areaName;   //地区名称
	private String cityName;    //区县名称
	private Integer  neCode;    //基站ID
	private String neNameCn;   //基站名称
	private Integer grade;    //'星级'  //等级
	private Date cycle; //'统计时间 yyyy-mm-dd hh:00:00', 
	
	private Integer cycleHour;  //小时
	private Date cycleDate; //日期  yyyy-MM-dd
	private Integer  totalScore;   //分数 '总分数，如果负分则需要置为0'
	private Integer cycleYear;  //'统计日期所在的年份',
	private Integer cycleMonth; //'统计日期所在的月份', 
	
	private Integer cycleWeek; //'今天是今年的第几周',
	private Integer cycleYearOfWeek; //'周计入的年份'
	//@Temporal(TemporalType.DATE)
	private Date cycleWeekFirst;   //'本周第一天',
	//@Temporal(TemporalType.DATE)
	private Date cycleWeekEnd;  //'本周最后一天',
	//@Temporal(TemporalType.DATE)
	private Date cycleMonthFirst;  //本月第一天',
	//@Temporal(TemporalType.DATE)
	private Date cycleMonthEnd;    //'本月最后一天',
	private Integer areaCode;    //'地区id'
	private Integer cityCode;   //区县id
	private Integer sectorId;  //扇区ID;
	
	private Integer cellId;    //小区id'
	private String cellNameCn;  //小区名称'
	
	//指标明细
	private String kpi001Value;  //'kpi值'
	private Integer kpi001Score;  //'kpi扣分
	
	private String kpi002Value;  //'kpi值'
	private Integer kpi002Score;  //'kpi扣分
	
	private String kpi003Value;  //'kpi值'
	private Integer kpi003Score;  //'kpi扣分
	
	private String kpi004Value;  //'kpi值'
	private Integer kpi004Score;  //'kpi扣分
	
	private String kpi005Value;  //'kpi值'
	private Integer kpi005Score;  //'kpi扣分
	
	private String kpi006Value;  //'kpi值'
	private Integer kpi006Score;  //'kpi扣分
	
	private String kpi007Value;  //'kpi值'
	private Integer kpi007Score;  //'kpi扣分
	
	private String kpi008alue;  //'kpi值'
	private Integer kpi008Score;  //'kpi扣分
	
	private String kpi009Value;  //'kpi值'
	private Integer kpi009Score;  //'kpi扣分

	private String kpi010Value;  //'kpi值'
	private Integer kpi010Score;  //'kpi扣分
	
	private Integer kpiState;     //'kpi是否满足基站分数直接为0的要求',
	private Integer alarmScore;   //告警扣分
	private Integer alarmState;   //告警是否满足基站分数直接为0的要求'
	
	public Date getCycle() {
		return cycle;
	}
	public void setCycle(Date cycle) {
		this.cycle = cycle;
	}
	public Integer getCycleYear() {
		return cycleYear;
	}
	public void setCycleYear(Integer cycleYear) {
		this.cycleYear = cycleYear;
	}
	public Integer getCycleMonth() {
		return cycleMonth;
	}
	public void setCycleMonth(Integer cycleMonth) {
		this.cycleMonth = cycleMonth;
	}
	public Integer getCycleWeek() {
		return cycleWeek;
	}
	public void setCycleWeek(Integer cycleWeek) {
		this.cycleWeek = cycleWeek;
	}
	public Integer getCycleYearOfWeek() {
		return cycleYearOfWeek;
	}
	public void setCycleYearOfWeek(Integer cycleYearOfWeek) {
		this.cycleYearOfWeek = cycleYearOfWeek;
	}
	public Date getCycleWeekFirst() {
		return cycleWeekFirst;
	}
	public void setCycleWeekFirst(Date cycleWeekFirst) {
		this.cycleWeekFirst = cycleWeekFirst;
	}
	public Date getCycleWeekEnd() {
		return cycleWeekEnd;
	}
	public void setCycleWeekEnd(Date cycleWeekEnd) {
		this.cycleWeekEnd = cycleWeekEnd;
	}
	public Date getCycleMonthFirst() {
		return cycleMonthFirst;
	}
	public void setCycleMonthFirst(Date cycleMonthFirst) {
		this.cycleMonthFirst = cycleMonthFirst;
	}
	public Date getCycleMonthEnd() {
		return cycleMonthEnd;
	}
	public void setCycleMonthEnd(Date cycleMonthEnd) {
		this.cycleMonthEnd = cycleMonthEnd;
	}
	public Integer getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(Integer areaCode) {
		this.areaCode = areaCode;
	}
	public Integer getCityCode() {
		return cityCode;
	}
	public void setCityCode(Integer cityCode) {
		this.cityCode = cityCode;
	}
	public Integer getSectorId() {
		return sectorId;
	}
	public void setSectorId(Integer sectorId) {
		this.sectorId = sectorId;
	}
	public Integer getCellId() {
		return cellId;
	}
	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}
	public String getCellNameCn() {
		return cellNameCn;
	}
	public void setCellNameCn(String cellNameCn) {
		this.cellNameCn = cellNameCn;
	}
	public Date getCycleDate() {
		return cycleDate;
	}
	public void setCycleDate(Date cycleDate) {
		this.cycleDate = cycleDate;
	}
	public Integer getCycleHour() {
		return cycleHour;
	}
	public void setCycleHour(Integer cycleHour) {
		this.cycleHour = cycleHour;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public Integer getNeCode() {
		return neCode;
	}
	public void setNeCode(Integer neCode) {
		this.neCode = neCode;
	}
	public String getNeNameCn() {
		return neNameCn;
	}
	public void setNeNameCn(String neNameCn) {
		this.neNameCn = neNameCn;
	}
	public Integer getGrade() {
		return grade;
	}
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	public Integer getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}
	public String getKpi001Value() {
		return kpi001Value;
	}
	public void setKpi001Value(String kpi001Value) {
		this.kpi001Value = kpi001Value;
	}
	public Integer getKpi001Score() {
		return kpi001Score;
	}
	public void setKpi001Score(Integer kpi001Score) {
		this.kpi001Score = kpi001Score;
	}
	public String getKpi002Value() {
		return kpi002Value;
	}
	public void setKpi002Value(String kpi002Value) {
		this.kpi002Value = kpi002Value;
	}
	public Integer getKpi002Score() {
		return kpi002Score;
	}
	public void setKpi002Score(Integer kpi002Score) {
		this.kpi002Score = kpi002Score;
	}
	public String getKpi003Value() {
		return kpi003Value;
	}
	public void setKpi003Value(String kpi003Value) {
		this.kpi003Value = kpi003Value;
	}
	public Integer getKpi003Score() {
		return kpi003Score;
	}
	public void setKpi003Score(Integer kpi003Score) {
		this.kpi003Score = kpi003Score;
	}
	public String getKpi004Value() {
		return kpi004Value;
	}
	public void setKpi004Value(String kpi004Value) {
		this.kpi004Value = kpi004Value;
	}
	public Integer getKpi004Score() {
		return kpi004Score;
	}
	public void setKpi004Score(Integer kpi004Score) {
		this.kpi004Score = kpi004Score;
	}
	public String getKpi005Value() {
		return kpi005Value;
	}
	public void setKpi005Value(String kpi005Value) {
		this.kpi005Value = kpi005Value;
	}
	public Integer getKpi005Score() {
		return kpi005Score;
	}
	public void setKpi005Score(Integer kpi005Score) {
		this.kpi005Score = kpi005Score;
	}
	public String getKpi006Value() {
		return kpi006Value;
	}
	public void setKpi006Value(String kpi006Value) {
		this.kpi006Value = kpi006Value;
	}
	public Integer getKpi006Score() {
		return kpi006Score;
	}
	public void setKpi006Score(Integer kpi006Score) {
		this.kpi006Score = kpi006Score;
	}
	public String getKpi007Value() {
		return kpi007Value;
	}
	public void setKpi007Value(String kpi007Value) {
		this.kpi007Value = kpi007Value;
	}
	public Integer getKpi007Score() {
		return kpi007Score;
	}
	public void setKpi007Score(Integer kpi007Score) {
		this.kpi007Score = kpi007Score;
	}
	public String getKpi008alue() {
		return kpi008alue;
	}
	public void setKpi008alue(String kpi008alue) {
		this.kpi008alue = kpi008alue;
	}
	public Integer getKpi008Score() {
		return kpi008Score;
	}
	public void setKpi008Score(Integer kpi008Score) {
		this.kpi008Score = kpi008Score;
	}
	public String getKpi009Value() {
		return kpi009Value;
	}
	public void setKpi009Value(String kpi009Value) {
		this.kpi009Value = kpi009Value;
	}
	public Integer getKpi009Score() {
		return kpi009Score;
	}
	public void setKpi009Score(Integer kpi009Score) {
		this.kpi009Score = kpi009Score;
	}
	public String getKpi010Value() {
		return kpi010Value;
	}
	public void setKpi010Value(String kpi010Value) {
		this.kpi010Value = kpi010Value;
	}
	public Integer getKpi010Score() {
		return kpi010Score;
	}
	public void setKpi010Score(Integer kpi010Score) {
		this.kpi010Score = kpi010Score;
	}
	public Integer getKpiState() {
		return kpiState;
	}
	public void setKpiState(Integer kpiState) {
		this.kpiState = kpiState;
	}
	public Integer getAlarmScore() {
		return alarmScore;
	}
	public void setAlarmScore(Integer alarmScore) {
		this.alarmScore = alarmScore;
	}
	public Integer getAlarmState() {
		return alarmState;
	}
	public void setAlarmState(Integer alarmState) {
		this.alarmState = alarmState;
	}
	
/*	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}*/

	
}

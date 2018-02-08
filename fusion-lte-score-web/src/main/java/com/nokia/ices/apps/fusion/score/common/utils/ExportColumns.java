package com.nokia.ices.apps.fusion.score.common.utils;

public class ExportColumns {
	public static String[] AREA_SCORE_DAY = new String[] { "cycle_date", "area_name", "city_name", "grade1", "grade2", "grade3", "grade4", "grade5" };
	public static String[] AREA_SCORE_DAY_HEADER = new String[] { "日期", "地市", "区县", "一级占比", "二级占比", "三级占比", "四级占比", "五级占比" };
	public static String[] AREA_SCORE_HOUR = new String[] { "cycle_date", "cycle_hour", "area_name", "city_name", "grade1", "grade2", "grade3", "grade4", "grade5" };
	public static String[] AREA_SCORE_HOUR_HEADER = new String[] { "日期", "小时", "地市", "区县", "一级占比", "二级占比", "三级占比", "四级占比", "五级占比" };
	public static String[] AREA_SCORE_MONTH = new String[] { "cycle_year", "cycle_month", "area_name", "city_name", "grade1", "grade2", "grade3", "grade4", "grade5" };
	public static String[] AREA_SCORE_MONTH_HEADER = new String[] { "年度", "月份", "地市", "区县", "一级占比", "二级占比", "三级占比", "四级占比", "五级占比" };
	public static String[] AREA_SCORE_WEEK = new String[] { "cycle_year_of_week", "cycle_week", "cycle_week_first", "cycle_week_end", "area_name", "city_name", "grade1", "grade2", "grade3", "grade4",
			"grade5" };
	public static String[] AREA_SCORE_WEEK_HEADER = new String[] { "年度", "周", "周第一天", "周最后一天", "地市", "区县", "一级占比", "二级占比", "三级占比", "四级占比", "五级占比" };
	//=======故障评分统计导出
	public static String[] FAULT_ANALYSIS_DAY = new String[] { "area_name","city_name", "cycle_date", "alarm" };
	public static String[] FAULT_ANALYSIS_DAY_HEADER = new String[] {"地市","区县", "日期", "基站告警占比" };
	public static String[] FAULT_ANALYSIS_HOUR = new String[] {"area_name","city_name","cycle","alarm" };
	public static String[] FAULT_ANALYSIS_HOUR_HEADER = new String[] { "地市","区县", "日期", "基站告警占比" };
	public static String[] FAULT_ANALYSIS_MONTH = new String[] {"area_name","city_name", "cycle_year", "cycle_month", "alarm" };
	public static String[] FAULT_ANALYSIS_MONTH_HEADER = new String[] {"地市","区县","年度", "月份", "基站告警占比" };
	public static String[] FAULT_ANALYSIS_WEEK = new String[] {"area_name","city_name", "cycle_year", "cycle_week", "alarm" };
	public static String[] FAULT_ANALYSIS_WEEK_HEADER = new String[] {"地市","区县", "年度", "周", "基站告警占比" };
	
	public static String[] KPI_HOUR_SCORE = new String[] { "area_name", "city_name", "ne_code", "ne_name_cn", "cell_id", "cell_name_cn", "cycle_date", "cycle_hour","alarm_score"};
	public static String[] KPI_HOUR_SCORE_HEADER = new String[] { "地市", "区县", "基站ID", "基站名称", "小区ID", "小区名称", "日期", "时间","告警" };
	
	//===========基站评分明细 小时 天 周  月  start=====
	public static String[] BASE_SCORE_HOUR = new String[] { "area_name", "city_name", "ne_code", "ne_name_cn","kpiAll", "total_score", "grade", "cycle"};
	public static String[] BASE_SCORE_HOUR_HEADER = new String[] { "地市", "区县", "基站ID", "基站名称","评分故障原因", "分数", "等级","时间" };
	
	public static String[] BASE_SCORE_DAY = new String[] { "area_name", "city_name", "ne_code", "ne_name_cn", "kpiAll","total_score", "grade", "cycleDate"};
	public static String[] BASE_SCORE_DAY_HEADER = new String[] { "地市", "区县", "基站ID", "基站名称","评分故障原因", "分数", "等级", "日期"};
	
	public static String[] BASE_SCORE_WEEK = new String[] { "area_name", "city_name", "ne_code", "ne_name_cn","kpiAll", "total_score", "grade", "cycle_year","cycle_week"};
	public static String[] BASE_SCORE_WEEK_HEADER = new String[] { "地市", "区县", "基站ID", "基站名称", "评分故障原因","分数", "等级","年","周" };
	
	public static String[] BASE_SCORE_MONTH = new String[] { "area_name", "city_name", "ne_code", "ne_name_cn","kpiAll", "total_score", "grade", "cycle_year","cycle_month"};
	public static String[] BASE_SCORE_MONTH_HEADER = new String[] { "地市", "区县", "基站ID", "基站名称", "评分故障原因","分数", "等级", "年","月" };
	
	//=======end ========
	
	public static String[] BASE_KPI = new String[] {"area_name","city_name","cell_id","cell_name_cn","cycle_date", "cycle_hour","kpi"};
	public static String[] BASE_KPI_HEADER  = new String[] { "地市名称", "区县名称", "小区ID", "小区名称", "日期", "时间", "KPI值"};
	
	public static String[] ICES_ALARM_RECORD = new String[] {"ne_code","ne_name_cn","area_name","city_name","alarm_title","alarm_desc",
			"start_time","cancel_time","delay","manufacturer"};
	public static String[] ICES_ALARM_RECORD_HEADER  = new String[] { "基站ID","基站名称","地市名称", "区县名称",
			"告警码","告警信息", "告警开始时间", "告警取消时间","时长(小时)","厂家"};
	
	public static String[] ICES_FRE_RECORD = new String[] {"ne_code","ne_name_cn","area_name","city_name","alarm_title","alarm_desc","start_time", "cancel_time",
			"frequency","manufacturer"};
	public static String[] ICES_FRE_RECORD_HEADER  = new String[] { "基站ID","基站名称","地市名称", "区县名称",
			"告警码","告警信息", "告警开始时间", "告警取消时间","频次(次数)","厂家"};
	
	//时长 天 周 月
	public static String[] ALARM_DELAY_DAY = new String[] {"area_name","city_name","ne_code","period_date","alarm_no","difftime","start_time", "cancel_time","manufacturer"};
	public static String[] ALARM_DELAY__DAY_HEADER  = new String[] { "地市名称", "区县名称", "基站ID","日", "告警号", "告警时长","告警开始时间","告警取消时间","厂家"};
	
	public static String[] ALARM_DELAY_WEEK = new String[] {"area_name","city_name","ne_code","period_date","alarm_no","difftime","start_time", "cancel_time","manufacturer"};
	public static String[] ALARM_DELAY__WEEK_HEADER  = new String[] { "地市名称", "区县名称", "基站ID","周", "告警号", "告警时长","告警开始时间","告警取消时间","厂家"};
	
	public static String[] ALARM_DELAY_MONTH = new String[] {"area_name","city_name","ne_code","period_date","alarm_no","difftime","start_time", "cancel_time","manufacturer"};
	public static String[] ALARM_DELAY__MONTH_HEADER  = new String[] { "地市名称", "区县名称", "基站ID","月", "告警号", "告警时长","告警开始时间","告警取消时间","厂家"};
	//频次 天  周 月
	public static String[] ALARM_FREQUENCY_DAY = new String[] {"area_name","city_name","ne_code","period_date","alarm_no","difftime","manufacturer"};
	public static String[] ALARM_FREQUENCY__DAY_HEADER  = new String[] { "地市名称", "区县名称", "基站ID","日", "告警号", "告警次数","厂家"};
	
	public static String[] ALARM_FREQUENCY_WEEK = new String[] {"area_name","city_name","ne_code","period_date","alarm_no","difftime","manufacturer"};
	public static String[] ALARM_FREQUENCY_WEEK_HEADER  = new String[] { "地市名称", "区县名称", "基站ID","周", "告警号", "告警次数","厂家"};
	
	public static String[] ALARM_FREQUENCY_MONTH = new String[] {"area_name","city_name","ne_code","period_date","alarm_no","difftime","manufacturer"};
	public static String[] ALARM_FREQUENCY_MONTH_HEADER  = new String[] { "地市名称", "区县名称", "基站ID","月", "告警号", "告警次数","厂家"};
	
	//基站性能告警得分
	public static String[] BTS_ALARM_SCORE = new String[] {"area_name","city_name","ne_code","ne_name_cn","cycle","score","manufacturer"};
	public static String[] BTS_ALARM_SCORE_HEADER  = new String[] { "地市名称", "区县名称", "基站ID","基站名称","时间", "分数", "厂家"};
	
	//基础基站信息
	public static String[] ICES_CEll = new String[] {"area_name","city_name","cell_name_cn","cell_id","ne_code","ne_name_cn","sector_id","proxy_x","proxy_y"};
	public static String[] ICES_CEll_HEADER  = new String[] { "地市名称", "区县名称", "小区名称","小区ID","基站ID","基站名称","扇区", "经度", "纬度"};
	
	
}

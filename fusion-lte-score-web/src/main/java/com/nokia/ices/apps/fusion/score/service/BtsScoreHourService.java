package com.nokia.ices.apps.fusion.score.service;

import java.util.List;
import java.util.Map;


public interface BtsScoreHourService {

	  //基站评分
	  Map<String,Object>  findBtsScoreHourAll(Map<String, Object> params);
	  
	  //基站评分 导出
	  Map<String,Object>  exportAll(Map<String, Object> params);
	  
	  //指标评分
	  Map<String,Object>  findKpiScoreHourAll(Map<String, Object> params);
	  
	  //基站指标评分明细
	  List<Map<String,Object>> findByBtsHourCodeAll(Map<String, Object> params);

	  //KPI查询
	  Map<String, Object> findKpiQueryAll(Map<String, Object> params);

	  //kpi图形指标_图
	  Map<String, Object> findKpiChartAll(Map<String, Object> params);
	  
	  //kpi图形指标_图明细
	  List<Map<String, Object>> findKpiGraphicDetailAll(Map<String, Object> params);
	  
	  //基站指标评分图形
	  List<Map<String,Object>> findByBtsScoreGraphicAll(Map<String, Object> params);

	Integer getKpiTotal(Map<String, Object> searchParams);

	List<Map<String, Object>> getAllList(Map<String, Object> searchParams);
	
	Map<String, Object> getKpiValueData(Map<String, Object> params) ;

	
}

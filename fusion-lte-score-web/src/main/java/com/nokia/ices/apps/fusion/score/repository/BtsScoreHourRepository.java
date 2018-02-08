package com.nokia.ices.apps.fusion.score.repository;

import java.util.List;
import java.util.Map;

//评分
public interface BtsScoreHourRepository{
	
	//获取基站评分，并按照基站ID去重
	List<Map<String,Object>> findBtsHourAll(Map<String, Object> params);
	
	Integer findBtsHourCount(Map<String, Object> params);
	
	/**
	 * 根据基站neCode查询该基站的所有小区的评分折线图数据
	 * @param params
	 * @return
	 */
	List<Map<String,Object>> findByBtsHourCodeAll(Map<String, Object> params);
	
	//指标
	List<Map<String,Object>> findKpiHourAll(Map<String, Object> params);
	
	Integer findKpiHourCount(Map<String, Object> params);

	//KPI查询
	Integer findKpiQueryCount(Map<String, Object> params);

	//KPI查询
	List<Map<String, Object>> findKpiQueryAll(Map<String, Object> params);
	
    /**
     *  kpi 指标图形
     * @param params
     * @return
     */
	List<Map<String,Object>> findKpiChartAll(Map<String, Object> params);
	
	//指标图形--指标明细
	List<Map<String,Object>> kpiGraphicDetailAll(Map<String, Object> params);

	//基站--指标图形
	List<Map<String, Object>> findByBtsHourGraphicAll(Map<String, Object> params);

	Integer getKpiTotal(Map<String, Object> searchParams);
	
	List<Map<String, Object>> queryAll(Map<String, Object> params) ;
	
	
	List<Map<String, Object>> getKpiValueData(Map<String, Object> params) ;
	
	
	
	
}

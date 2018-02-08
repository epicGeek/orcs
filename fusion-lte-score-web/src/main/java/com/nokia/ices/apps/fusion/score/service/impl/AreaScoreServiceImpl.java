package com.nokia.ices.apps.fusion.score.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.mybatis.MapperLoadDao;
import com.nokia.ices.apps.fusion.score.common.utils.DateUtils;
import com.nokia.ices.apps.fusion.score.repository.AreaScoreRepository;
import com.nokia.ices.apps.fusion.score.service.AreaScoreService;

@Service("areaScoreService")
public class AreaScoreServiceImpl implements AreaScoreService, InitializingBean {
	@Autowired
	MapperLoadDao mapperLoadDao;

	AreaScoreRepository areaScoreRepository;

	@Override
	public Map<String, Object> queryAllAreaScore(Map<String, Object> params) {
		String scoreType =params.get("scoreType")!=null?params.get("scoreType").toString():"";
		if("1".equals(scoreType)){
			params.put("tableName", "area_score_hour");
			params.put("orderBy", "cycle_date desc,cycle_hour desc,");
			params.put("row", "DATE_FORMAT(cycle,'%Y-%m-%d %H:%00:%00')as cycle");
		}else if("2".equals(scoreType)){
			params.put("tableName", "area_score_day");
			params.put("orderBy", "cycle_date desc,");
			params.put("row", "DATE_FORMAT(cycle_date,'%Y-%m-%d')as cycle_date");
		}else if("3".equals(scoreType)){
			params =DateUtils.getStartDateAndEndDate(params);
			params.put("tableName", "area_score_week");
			params.put("orderBy", "cycle_year_of_week desc,cycle_week desc,");
			params.put("row", "cycle_year_of_week,cycle_week");
		}else if("4".equals(scoreType)){
			params =DateUtils.getStartDateAndEndDate(params);
			params.put("tableName", "area_score_month");
			params.put("orderBy", "cycle_year desc, cycle_month desc,");
			params.put("row", "cycle_year,cycle_month");
		}
		
		
		Integer total = areaScoreRepository.findAreaScoreCount(params);
		List<Map<String, Object>> areaScoreList = areaScoreRepository.queryAreaScoreList(params);
		Map<String, Object> resusltMap = new HashMap<String, Object>();
		resusltMap.put("total", total);
		resusltMap.put("rows", areaScoreList);

		return resusltMap;
	}

	//评分故障统计
	@Override
	public Map<String, Object> queryAllBreakReason(Map<String, Object> params) {
		String scoreType =params.get("scoreType")!=null?params.get("scoreType").toString():"";
		if("1".equals(scoreType)){
			params.put("tableName", "fault_analysis_hour");
			params.put("groupBy", "cycle");
			params.put("row", "DATE_FORMAT(cycle,'%Y-%m-%d %H:%00:%00')as cycle");
		}else if("2".equals(scoreType)){
			params.put("tableName", "fault_analysis_day");
			params.put("groupBy", "cycle_date");
			params.put("row", "DATE_FORMAT(cycle_date,'%Y-%m-%d')as cycle_date");
		}else if("3".equals(scoreType)){
			params =DateUtils.getStartDateAndEndDate(params);
			params.put("tableName", "fault_analysis_week");
			params.put("groupBy", "cycle_year,cycle_week");
			params.put("row", "cycle_week");
		}else if("4".equals(scoreType)){
			params =DateUtils.getStartDateAndEndDate(params);
			params.put("tableName", "fault_analysis_month");
			params.put("groupBy", "cycle_year,cycle_month");
			params.put("row", "cycle_month");
		}
		
		Integer total = areaScoreRepository.findBreakReasonCount(params);
		List<Map<String, Object>> areaScoreList = areaScoreRepository.queryBreakReasonList(params);
		Map<String, Object> resusltMap = new HashMap<String, Object>();
		resusltMap.put("total", total);
		resusltMap.put("rows", areaScoreList);

		return resusltMap;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		areaScoreRepository = mapperLoadDao.getBtsObject(AreaScoreRepository.class);

	}

}

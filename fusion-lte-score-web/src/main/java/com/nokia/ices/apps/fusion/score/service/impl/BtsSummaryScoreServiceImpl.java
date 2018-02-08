package com.nokia.ices.apps.fusion.score.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nokia.ices.apps.fusion.mybatis.MapperLoadDao;
import com.nokia.ices.apps.fusion.score.repository.BtsSummaryScoreRepository;
import com.nokia.ices.apps.fusion.score.service.BtsSummaryScoreService;

@Service("btsScoreSumService")
public class BtsSummaryScoreServiceImpl implements BtsSummaryScoreService,InitializingBean{

	private final static Logger logger = LoggerFactory.getLogger(BtsSummaryScoreServiceImpl.class);
	
	@Autowired
	MapperLoadDao mapperLoadDao;

	BtsSummaryScoreRepository btsScoreSumRepository;
	
	//基站
	public  Map<String,Object> findBtsSumAll(Map<String, Object> params) {
		Integer total = btsScoreSumRepository.findBtsSumCount(params);
		
		//默认日表 bts_score_day
		String orderBy = "ta1.cycle_date";
		String title = "ta1.city_name,ta1.area_name,ta1.ne_code,ta1.ne_name_cn,ta1.cycle_month,ta1.cycle_week,ta1.cycle_date,ta1.cycle_year,ta1.grade,ta1.total_score";
		String table = params.get("tableName").toString();
		if("bts_score_week".equals(table)){//bts_score_week
			orderBy = "ta1.cycle_week";
			title = "ta1.city_name,ta1.area_name,ta1.ne_code,ta1.ne_name_cn,ta1.cycle_month,ta1.cycle_week,ta1.cycle_year,ta1.grade,ta1.total_score";
		}else if("bts_score_month".equals(table)){//
			orderBy = "ta1.cycle_month";
			title = "ta1.city_name,ta1.area_name,ta1.ne_code,ta1.ne_name_cn,ta1.cycle_month,ta1.grade,ta1.cycle_year,ta1.total_score";
		}
		params.put("title", title);
		params.put("orderBy", orderBy);
		List<Map<String,Object>> btsList = btsScoreSumRepository.findBtsSumAll(params);
	    Map<String,Object> resusltMap  = new HashMap<String, Object>();
	    resusltMap.put("totalElements", total);
	    resusltMap.put("content", btsList);
	        
		return resusltMap;
	}
	
	
	 @Override
     public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub
        logger.debug("init btsScoreHourRepository Bean success.............");
        btsScoreSumRepository = mapperLoadDao.getBtsObject(BtsSummaryScoreRepository.class);
        
     }
	/**
	 * 图形
	 * */
	@Override
	public List<Map<String, Object>> findSumchartAll(Map<String, Object> params) {
	
		//默认日表 bts_score_day
		String orderBy = "ta1.cycle_date";
		String title = "ta1.ne_code,ta1.ne_name_cn,ta1.cycle_date,ta1.total_score";//基站ID，基站名称，分数，时间
		String table = params.get("tableName").toString();
		if("bts_score_week".equals(table)){
			orderBy = "ta1.cycle_week_first";
			title = "ta1.ne_code,ta1.ne_name_cn,ta1.cycle_week_first,ta1.cycle_week,ta1.total_score";//基站ID、名称，分数，周一，周 如：2015-10-25 44周
		}else if("bts_score_month".equals(table)){
			orderBy = "ta1.cycle_month_first";
			title = "ta1.ne_code,ta1.ne_name_cn,ta1.cycle_month,ta1.cycle_year,ta1.cycle_month_first,ta1.total_score";//基站ID、名称，分数,月，月的第一天，年 如：2015-09
		}
		params.put("title", title);
		params.put("orderBy", orderBy);
		
		return btsScoreSumRepository.findBtsSumChart(params);
		
	}


}

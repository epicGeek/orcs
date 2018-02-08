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
import com.nokia.ices.apps.fusion.score.repository.BtsPerformanceAlarmScoreRepository;
import com.nokia.ices.apps.fusion.score.service.BtsAlarmScoreService;

//性能告警的得分配置
@Service("btsAlarmScoreService")
public class BtsAlarmScoreServiceImpl implements BtsAlarmScoreService,InitializingBean{

	private final static Logger logger = LoggerFactory.getLogger(BtsAlarmScoreServiceImpl.class);

	@Autowired
	MapperLoadDao mapperLoadDao;
	
	BtsPerformanceAlarmScoreRepository  btsAlarmScore;
	

	/**
	 * 健康评分 ----- 基站性能告警得分查询
	 */
	@Override
	public Map<String, Object> findBtsPerformanceAlarmScore(Map<String, Object> params) {
		// TODO Auto-generated method stub
	//	Integer total =5000; //btsAlarmScore.findBtsPerformanceAlarmScoreCount(params);
	//	params.put("pageSize", total);
		//	resusltMap.put("totalElements", total=btsAlarmScoreList.size()<total?btsAlarmScoreList.size():total);
		//resusltMap.put("content", btsAlarmScoreList);
		Integer total =btsAlarmScore.findBtsPerformanceAlarmScoreCount(params);
		List<Map<String, Object>> btsAlarmScoreList = btsAlarmScore.findBtsPerformanceAlarmScoreAll(params);
		Map<String, Object> resusltMap = new HashMap<String, Object>();
		resusltMap.put("totalElements", total);
		resusltMap.put("content", btsAlarmScoreList);

		return resusltMap;
		
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		logger.debug("init btsAlarmScoreRepository Bean success.............");
		//xml是bts-congfiguration配置就mapperLoadDao.getBtsObject
		btsAlarmScore = mapperLoadDao.getBtsObject(BtsPerformanceAlarmScoreRepository.class);
	}


	@Override
	public Map<String, Object> findAlarmNo(Map<String, Object> params) {
		
		Integer total =btsAlarmScore.findBtsAlarmNoCount(params);
		List<Map<String, Object>> btsAlarmScoreList = btsAlarmScore.findBtsAlarmNo(params);
		Map<String, Object> resusltMap = new HashMap<String, Object>();
		resusltMap.put("totalElements",total);
		resusltMap.put("content", btsAlarmScoreList);

		return resusltMap;
	}

}

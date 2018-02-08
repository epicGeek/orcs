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
import com.nokia.ices.apps.fusion.score.repository.BtsAlarmScoreRepository;
import com.nokia.ices.apps.fusion.score.service.BtsAlarmScoreRuleService;

//性能告警的得分配置
@Service("btsAlarmScoreRuleService")
public class BtsAlarmScoreRuleServiceImpl implements BtsAlarmScoreRuleService,InitializingBean{

	private final static Logger logger = LoggerFactory.getLogger(BtsAlarmScoreRuleServiceImpl.class);

	@Autowired
	MapperLoadDao mapperLoadDao;
	
	BtsAlarmScoreRepository btsAlarmScoreRepository;
	

	@Override
	public Map<String, Object> findBtsAlarmScoreAll(Map<String, Object> params) {
		
		Integer total = btsAlarmScoreRepository.findAlarmScoreCount(params);
		List<Map<String, Object>> alarmScoreList = btsAlarmScoreRepository.findAlarmScoreAll(params);
		Map<String, Object> resusltMap = new HashMap<String, Object>();
		resusltMap.put("totalElements", total);
		resusltMap.put("content", alarmScoreList);

		return resusltMap;
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.debug("init btsAlarmScoreRepository Bean success.............");
		btsAlarmScoreRepository = mapperLoadDao.getObject(BtsAlarmScoreRepository.class);
	}

	@Override
	public void alarmScoreRuleEdit(Map<String, Object> params) {
		// TODO Auto-generated method stub
		btsAlarmScoreRepository.alarmScoreRuleEdit(params);
	}


}

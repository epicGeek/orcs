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
import com.nokia.ices.apps.fusion.score.repository.BtsAlarmJobRepository;
import com.nokia.ices.apps.fusion.score.service.BtsAlarmJobService;

/**
 * 工单 基站性能告警
 * @author Administrator
 *
 */
@Service("btsAlarmJobService")
public class BtsAlarmJobServiceImpl implements BtsAlarmJobService,InitializingBean{

	private final static Logger logger = LoggerFactory.getLogger(BtsAlarmJobServiceImpl.class);

	@Autowired
	MapperLoadDao mapperLoadDao;
	
	BtsAlarmJobRepository  btsJob;

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		logger.debug("init btsAlarmJobRepository Bean success.............");
		//xml是bts-congfiguration配置就mapperLoadDao.getBtsObject 数据库ice
		btsJob = mapperLoadDao.getBtsObject(BtsAlarmJobRepository.class);
	}

	/**
	 * 工单系统 ----- 基站性能告警工单
	 */
	@Override
	public Map<String, Object> findBtsAlarmJob(Map<String, Object> params) {
		// TODO Auto-generated method stub
		Integer total =btsJob.findBtsJobCount(params);
		List<Map<String, Object>> btsAlarmJob = btsJob.findBtsJobAll(params);
		Map<String, Object> resusltMap = new HashMap<String, Object>();
		resusltMap.put("totalElements", total);
		resusltMap.put("content", btsAlarmJob);

		return resusltMap;
	}

	@Override
	public Map<String, Object> findBtsScoreJob(Map<String, Object> params) {
		// TODO Auto-generated method stub
		Integer total =btsJob.findBtsScoreJobCount(params);
		List<Map<String, Object>> btsScoreJob = btsJob.findBtsScoreJobAll(params);
		Map<String, Object> resusltMap = new HashMap<String, Object>();
		resusltMap.put("totalElements", total);
		resusltMap.put("content", btsScoreJob);

		return resusltMap;
	}

}

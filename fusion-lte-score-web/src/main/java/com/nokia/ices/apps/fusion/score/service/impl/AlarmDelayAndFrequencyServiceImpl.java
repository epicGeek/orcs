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
import com.nokia.ices.apps.fusion.score.repository.AlarmDelayAndFrequencyRepository;
import com.nokia.ices.apps.fusion.score.service.AlarmDelayAndFrequencyService;

@Service("alarmDelayService")
public class AlarmDelayAndFrequencyServiceImpl implements AlarmDelayAndFrequencyService,InitializingBean{
	
	private final static Logger logger = LoggerFactory.getLogger(AlarmDelayAndFrequencyServiceImpl.class);
	
	@Autowired
    MapperLoadDao mapperLoadDao;
	
	AlarmDelayAndFrequencyRepository alarmCount;

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		logger.debug("init alamDelayRepository Bean success.............");
		//表在ices库里  xml是bts-congfiguration配置就mapperLoadDao.getBtsObject
		alarmCount = mapperLoadDao.getBtsObject(AlarmDelayAndFrequencyRepository.class);
		
	}

	//时长
	@Override
	public Map<String, Object> findAlarmDelayAll(Map<String, Object> params) {
		
		Map<String, Object> resusltMap = new HashMap<String, Object>();
		Integer total =alarmCount.findAlarmDelayCount(params);
		List<Map<String, Object>> alarmDelayList = alarmCount.findAlarmDelayAll(params);
		resusltMap.put("totalElements", total);
		resusltMap.put("content", alarmDelayList);

		return resusltMap;
	}
	
	//频次
	@Override
	public Map<String, Object> findAlarmFrequencyAll(Map<String, Object> params) {
		
		Integer total =alarmCount.findAlarmFrequencyCount(params);
		List<Map<String, Object>> alarmDelayList = alarmCount.findAlarmFrequencyAll(params);
		Map<String, Object> resusltMap = new HashMap<String, Object>();
		resusltMap.put("totalElements", total);
		resusltMap.put("content", alarmDelayList);

		return resusltMap;
	}

	//总时长统计占比 排名 饼图 柱状图
	@Override
	public Map<String,Object> findAlarmDelayAllData(Map<String, Object> params) {
		// TODO Auto-generated method stub
		
		Map<String, Object> mapList =  new HashMap<>();
		String period_date =  alarmCount.MaxDate(params);
		params.put("period_date", period_date);
		List<Map<String,Object>> barList =  alarmCount.findAlarmDelayBarChart(params);
		List<Map<String,Object>> pieList =  alarmCount.findAlarmDelayPie(params);
		mapList.put("bar", barList);//柱状图数据
		mapList.put("pie", pieList);//饼图数据
		return mapList;
		
	}

	@Override
	public Double findAlarmDelaySum(Map<String, Object> params) {
		// TODO Auto-generated method stub
		String type = params.get("areaType").toString();
		String sumType = params.get("sumType").toString();
		
		if(!type.equals("0")){
			params.put("areaType", type);
			if("delay".equals(sumType)){
				return alarmCount.findAlarmDelaySum(params);
			}else{
				return alarmCount.findAlarmFrequencySum(params);
			}
		}
		
		return null;
	}

}

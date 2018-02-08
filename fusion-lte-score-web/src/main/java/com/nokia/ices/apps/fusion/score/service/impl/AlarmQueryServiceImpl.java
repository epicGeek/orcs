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
import com.nokia.ices.apps.fusion.score.repository.BtsInformationCellRepository;
import com.nokia.ices.apps.fusion.score.service.AlarmQueryService;

/**
 * 告警查询 退服查询
 * @author Administrator
 *
 */
@Service("alarmQueryService")
public class AlarmQueryServiceImpl implements AlarmQueryService,InitializingBean{
	
	private final static Logger logger = LoggerFactory.getLogger(AlarmQueryServiceImpl.class);
	
	@Autowired
    MapperLoadDao mapperLoadDao;
	
	BtsInformationCellRepository btsInformationCellRepository;

	@Override
	public Map<String, Object> findAlarmQueryAll(Map<String, Object> searchParams) {
		
		String tableName = searchParams.get("tableName")==null?"":searchParams.get("tableName").toString();
		
		if("ices_alarm_monitor_delay".equals(tableName)){
			searchParams.put("value", "delay");
		}else if("ices_alarm_monitor_frequency".equals(tableName)){
			searchParams.put("value", "frequency");
		}
		Integer total = btsInformationCellRepository.findAlarmQueryCount(searchParams);
		List<Map<String,Object>> btsList = btsInformationCellRepository.findAlarmQueryAll(searchParams);
	    Map<String,Object> resusltMap  = new HashMap<String, Object>();
	    resusltMap.put("totalElements", total);
	    resusltMap.put("content", btsList);
	        
		return resusltMap;
	}
	 @Override
     public void afterPropertiesSet() throws Exception {
        logger.debug("init bossBusinessDao Bean success.............");
        btsInformationCellRepository = mapperLoadDao.getObject(BtsInformationCellRepository.class);
        
     }
	@Override
	public Integer findAlarmQueryCount(Map<String, Object> searchParams) {
		return btsInformationCellRepository.findAlarmQueryCount(searchParams);
	}
	
	/**
	 * 退服查询
	 */
	@Override
	public Map<String, Object> findOutOfQueryAll(Map<String, Object> searchParams) {
		
		String tableName = searchParams.get("tableName")==null?"":searchParams.get("tableName").toString();
		String page = searchParams.get("page")==null?"":searchParams.get("page").toString();
		String pageSize = searchParams.get("pageSize")==null?"":searchParams.get("pageSize").toString();
		
		if("ices_alarm_out_of_delay".equals(tableName)){
			searchParams.put("value", "delay");
		}else if("ices_alarm_out_of_frequency".equals(tableName)){
			searchParams.put("value", "frequency");
		}
		
		searchParams.put("page", (Integer.parseInt(page)-1)*Integer.parseInt(pageSize));
		searchParams.put("pageSize", pageSize);
		
		Integer total = btsInformationCellRepository.findOutOfQueryCount(searchParams);
		List<Map<String,Object>> btsList = btsInformationCellRepository.findOutOfQueryAll(searchParams);
		Map<String,Object> resusltMap  = new HashMap<String, Object>();
		resusltMap.put("totalElements", total);
		resusltMap.put("content", btsList);
		
		return resusltMap;
	}
	
	@Override
	public Integer findOutOfQueryCount(Map<String, Object> searchParams) {
		return btsInformationCellRepository.findOutOfQueryCount(searchParams);
	}
	
}

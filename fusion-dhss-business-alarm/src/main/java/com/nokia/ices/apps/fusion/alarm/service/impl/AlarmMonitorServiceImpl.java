package com.nokia.ices.apps.fusion.alarm.service.impl;




import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.alarm.AlarmMonitor;
import com.nokia.ices.apps.fusion.alarm.AlarmReceiveRecord;
import com.nokia.ices.apps.fusion.alarm.repository.AlarmMonitorRepository;
import com.nokia.ices.apps.fusion.alarm.repository.AlarmReceiveRecordRepository;
import com.nokia.ices.apps.fusion.alarm.service.AlarmMonitorService;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;

/**
 * 告警监控业务接口实现类
 * @author xiaojun
 *
 */

@Service("alarmMonitorService")
public class AlarmMonitorServiceImpl implements AlarmMonitorService{

//	@Autowired
//	AlarmMonitorDao alarmMonitorDao;
	@Autowired
	AlarmMonitorRepository alarmMonitorRepository;
	
	@Autowired
	AlarmReceiveRecordRepository alarmReceiveRecordRepository;

//	
//	@Override
//	public List<AlarmMonitor> queryAlarmMonitor(Map<String, Object> m) {
//		List<AlarmMonitor> returnList=new ArrayList<AlarmMonitor>();
//		List<Map<String, Object>> temp=alarmMonitorDao.queryAlarmMonitor(m);
//		if(temp==null ||temp.size()<=0)
//			return returnList;
//		AlarmMonitor qm=null;
//		for(Map<String, Object> ms:temp){
//			qm=new AlarmMonitor();
//			qm.setId(Integer.parseInt(ms.get("id").toString().trim()));
//			qm.setAlarm_content(ms.get("alarm_content").toString().trim());
//			qm.setAlarm_level(ms.get("alarm_level").toString().trim());
//			if(ms.get("alarm_limit")!=null)
//				qm.setAlarm_limit(ms.get("alarm_limit").toString().trim());
//			else
//				qm.setAlarm_limit("");
//			qm.setAlarm_scene(ms.get("alarm_scene").toString().trim());
//			qm.setAlarm_title(ms.get("alarm_title").toString().trim());
//			qm.setAlarm_type(ms.get("alarm_type").toString().trim());
//			qm.setBelong_site(ms.get("belong_site").toString().trim());
//			qm.setCancel_time(ms.get("cancel_time").toString().trim());
//			qm.setNe_name(ms.get("ne_name").toString().trim());
//			qm.setNe_type(ms.get("ne_type").toString().trim());
//			qm.setStart_time(ms.get("start_time").toString().trim());
//			qm.setUnit_name(ms.get("unit_name").toString().trim());
//			qm.setUnit_type(ms.get("unit_type").toString().trim());
//			returnList.add(qm);
//		}
//		return returnList;
//	}
//
//	@Override
//	public Long queryAlarmMonitorCount(Map<String, Object> m) {
//		return alarmMonitorDao.queryAlarmMonitorCount(m);
//	}
//
//	@Override
//	public int findCurrentCount() {
//		return alarmMonitorDao.findCurrentCount();
//	}
	@Override
	public Page<AlarmMonitor> findAlarmMonitorList(Map<String, Object> searchParams, Pageable pageable) {
		 Map<String,SearchFilter> filter = SearchFilter.parse(searchParams);
	        Specification<AlarmMonitor> spec = 
	                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, AlarmMonitor.class);
	        
	     return alarmMonitorRepository.findAll(spec, pageable);
	}
	
	@Override
	public List<AlarmReceiveRecord> findAlarmReceiveRecordCount(Map<String, Object> searchParams) {
		 Map<String,SearchFilter> filter = SearchFilter.parse(searchParams);
	        Specification<AlarmReceiveRecord> spec = 
	                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, AlarmReceiveRecord.class);
	        
	     return alarmReceiveRecordRepository.findAll(spec);
	}
	
	
}

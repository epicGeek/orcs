package com.nokia.ices.apps.fusion.ems.run;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.ems.domain.EmsMonitor;
import com.nokia.ices.apps.fusion.ems.domain.EmsMonitorHistory;
import com.nokia.ices.apps.fusion.ems.repository.EmsMonitorHistoryRepository;
import com.nokia.ices.apps.fusion.ems.repository.EmsMonitorRepoitory;
import com.nokia.ices.apps.fusion.ems.service.EmsCheckTaskJobService;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;

public class ClearHistoryAlarmTask implements Runnable{
	
	public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public  EmsMonitorRepoitory emsMonitorRepoitory;

	public  EmsCheckTaskJobService emsCheckTaskJobService;
	
	public  EmsMonitorHistoryRepository emsMonitorHistoryRepository;
	
	private Logger logger = LoggerFactory.getLogger(ClearHistoryAlarmTask.class);
	
	


	public ClearHistoryAlarmTask(EmsMonitorRepoitory emsMonitorRepoitory, EmsCheckTaskJobService emsCheckTaskJobService,
			EmsMonitorHistoryRepository emsMonitorHistoryRepository) {
		super();
		this.emsMonitorRepoitory = emsMonitorRepoitory;
		this.emsCheckTaskJobService = emsCheckTaskJobService;
		this.emsMonitorHistoryRepository = emsMonitorHistoryRepository;
	}




	@Override
	public void run() {
		logger.info(" send history alarm ");
		GregorianCalendar todayCal = new GregorianCalendar();
		String groupDate = format.format(todayCal.getTime().getTime()-1000*60*60*24);
		String thisDate = format.format(todayCal.getTime().getTime());
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			map.put("executeTime_GE", format.parse(groupDate));
			map.put("executeTime_LT", format.parse(thisDate));
			map.put("resultLevel_NOTEQ", "0");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Map<String, SearchFilter> filter = SearchFilter.parse(map);
		Specification<EmsMonitor> spec = DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND,EmsMonitor.class);
		List<EmsMonitor> list = emsMonitorRepoitory.findAll(spec);
		for (EmsMonitor emsMonitor : list) {
			if(StringUtils.isNotBlank(emsMonitor.getErrorCode())){
				if(emsMonitor.getErrorCode().equals("0") || ProjectProperties.getSendErrorCode().contains(emsMonitor.getErrorCode())){
					emsCheckTaskJobService.noticeGroup(emsMonitor.getGroupId(), emsMonitor.getNotificationContent(),
							String.valueOf(emsMonitor.getMonitoredUnitId()), String.valueOf(emsMonitor.getMonitoredCommandId())
							, "(ALARM_REMIND)"+emsMonitor.getMonitoredUnitName(), false, "");
				}
			}
		}
		
		logger.info(" clear history monitor ");
		map.clear();
		try {
			map.put("executeTime_LT", format.parse(format.format(todayCal.getTime().getTime()-1000*60*60*24*7)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Map<String, SearchFilter> filter1 = SearchFilter.parse(map);
		Specification<EmsMonitorHistory> spec1 = DynamicSpecifications.bySearchFilter(filter1.values(), BooleanOperator.AND,EmsMonitorHistory.class);
		List<EmsMonitorHistory> list1 = emsMonitorHistoryRepository.findAll(spec1);
		emsMonitorHistoryRepository.delete(list1);
	}
	
//	public static void main(final String[] args) {
//        new Thread(new ClearHistoryAlarm()).start();
//    }

}

package com.nokia.ices.apps.fusion.alarm.controll;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.alarm.AlarmMonitor;
import com.nokia.ices.apps.fusion.alarm.AlarmReceiveRecord;
import com.nokia.ices.apps.fusion.alarm.NotImportantAlarm;
import com.nokia.ices.apps.fusion.alarm.repository.NotImportantAlarmRepository;
import com.nokia.ices.apps.fusion.alarm.service.AlarmMonitorService;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentNeRepository;

import scala.sys.process.ProcessBuilderImpl.Simple;

/**
 * 告警监控Controller对象
 * @author xiaojun
 *
 */
//@RequestMapping("/alarmMonitor")
//@RestController
@RepositoryRestController
@RestController
public class AlarmMonitorController {
	
	public static final Logger logger = LoggerFactory.getLogger(AlarmMonitorController.class);

	
	@Autowired
	AlarmMonitorService alarmMonitorService;
	
	@Autowired
	EquipmentNeRepository equipmentNeRepository;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("rawtypes")
    @Autowired
    private PagedResourcesAssembler pagedResourcesAssembler;
	
	@Autowired
	private NotImportantAlarmRepository notImportantAlarmRepository;
	
	/**
	 * 	告警监控JPA
	 * @param neName
	 * @param alarmType
	 * @param startTime
	 * @param endTime
	 * @param pageable
	 * @param assembler
	 * @return
	 * @throws ParseException 
	 */
	
	
		private Map<String,Object> currentPeriodKpiTime(){
			String sql = "select distinct period_start_time from quota_monitor ";
			List<Map<String,Object>> tempList = new ArrayList<>();
			tempList = jdbcTemplate.queryForList(sql);
			Date currentTime = (Date)tempList.get(0).get("period_start_time");
			tempList = jdbcTemplate.queryForList(sql);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTimeStr = sdf.format(currentTime);
			//String currentTimeStr = tempList.get(0).get("period_start_time").toString();
			sql = "select count(1) as count from alarm_monitor where kpi_code is not null and start_time = ?";
			tempList.clear();
			tempList = jdbcTemplate.queryForList(sql,currentTimeStr);
			Integer alarmNumber = Integer.valueOf(tempList.get(0).get("count").toString());
			Map<String,Object> map = new HashMap<>();
			map.put("period_start_time",currentTimeStr);
			map.put("count",alarmNumber);
			return map;
					
		}
		@RequestMapping(value = "alarm-monitor/search/countKpiAlarm")
		public Map<String,Object> kpiThresholdAlarmCount() throws ParseException{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map<String,Object> counterMap = currentPeriodKpiTime();
			String start_time = counterMap.get("period_start_time").toString();
			Date startTime = sdf.parse(start_time);
			Long startTimeBeforeLong = startTime.getTime()-5*60*1000;//5min interval
			Long startTimeAfterLong = startTime.getTime()+5*60*1000;
			Date startTimeBefore = new Date(startTimeBeforeLong);
			Date startTimeAfter = new Date(startTimeAfterLong);
			String startTimeBeforeStr = sdf.format(startTimeBefore);
			String startTimeAfterStr = sdf.format(startTimeAfter);
			String defaultEndTimeStr = sdf.format(new Date());
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1); //得到前一天
			String defaultStartTimeStr = sdf.format(calendar.getTime());
			counterMap.put("defaultStartTime", defaultStartTimeStr);
			counterMap.put("defaultEndTime", defaultEndTimeStr);
			counterMap.put("startTimeBefore", startTimeBeforeStr);
			counterMap.put("startTimeAfter", startTimeAfterStr);
			return counterMap;
		}
		@SuppressWarnings("unchecked")
		@RequestMapping(value = "alarm-monitor/search/searchByFilter")
	    public PagedResources<AlarmMonitor> findAlarmMonitorPageBySearchFilter(
	    		@RequestParam(value="neName",required=false) String neName,
	    		@RequestParam(value="alarmType",required=false) String alarmType,
	    		@RequestParam(value="startTime",required=false) String startTime,
	    		@RequestParam(value="endTime",required=false) String endTime,
	    		Pageable pageable,
	            PersistentEntityResourceAssembler assembler) {
	 		@SuppressWarnings("rawtypes")
	 		Map modelMap = new HashMap();
	 		if(startTime!=null && startTime != "" && endTime != null && endTime != ""){
					modelMap.put("startTime_GE", startTime);
					modelMap.put("startTime_LE", endTime);
			}
//	        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
	        // SystemRole systemRole = shiroUser.getRole();
	        /*SystemRole systemRole = new SystemRole();*/
	    	modelMap.put("unitName_LIKE", neName);
	    	modelMap.put("alarmType_EQ", alarmType);
	    	modelMap.put("alarmType_NOTEQ", "SOAPGW");
	        Page<AlarmMonitor> page = alarmMonitorService.findAlarmMonitorList(modelMap, pageable);
	        return pagedResourcesAssembler.toResource(page, assembler);
	    }
		
		@RequestMapping("addNotImportant")
		public boolean addNotImportant(@RequestParam(required=false,value="value")String value){
			List<NotImportantAlarm> list = notImportantAlarmRepository.findAll();
			NotImportantAlarm alarm = list.size() == 0 ? new NotImportantAlarm() : list.get(0);
			alarm.setAlarmNoArray(value);
			notImportantAlarmRepository.save(alarm);
			return true;
		}
		
		@RequestMapping("delNotImportant")
		public boolean delNotImportant(@RequestParam(required=false,value="value")String value){
			notImportantAlarmRepository.delete(notImportantAlarmRepository.findAll());
			return true;
		}
		
		
		public static void main(String[] args) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Long l1 = df.parse("2017-03-02 23:15:00").getTime();
				Long l2 = df.parse(df.format(new Date())).getTime();
				System.out.println( l1==l2 );
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		@SuppressWarnings("unchecked")
		@RequestMapping(value = "alarm-monitor/search/count")
	    public List<Map<String,String>> findAlarmMonitorPageBySearchFilter() throws ParseException {
	 		try {
				@SuppressWarnings("rawtypes")
				Map modelMap = new HashMap();
				Iterable<EquipmentNe> nes = equipmentNeRepository.findAll();
				
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String str = sdf.format(date);
				modelMap.put("receiveStartTime_GE", sdf1.parse(str+" 00:00:00"));
				modelMap.put("receiveStartTime_LE", sdf1.parse(str+" 23:59:00"));
				List<AlarmReceiveRecord> list = alarmMonitorService.findAlarmReceiveRecordCount(modelMap);
				Map<String,String> map = new HashMap<String,String>();
				for (EquipmentNe equipmentNe : nes) {
					if(StringUtils.isNotBlank(equipmentNe.getDhssName())){
						map.put(equipmentNe.getDhssName(), "0");
					}
				}
				List<NotImportantAlarm> alarmList = notImportantAlarmRepository.findAll();
				NotImportantAlarm alarm = alarmList.size() == 0 ? null : alarmList.get(0);
				List<String> alarmNoList = alarm == null ? new ArrayList<String>() : alarm.getAlarmNoList();
				//全部网元
				Iterable<EquipmentNe> neArray = equipmentNeRepository.findAll();
				
				for (AlarmReceiveRecord alarmReceiveRecord : list) {
					if(alarmNoList.size() != 0){
						if(alarmNoList.contains(alarmReceiveRecord.getAlarmNo())){
							continue;
						}
					} 
					String t = "unknown";
					if(StringUtils.isNotBlank(alarmReceiveRecord.getDhssName())){
						t = alarmReceiveRecord.getDhssName();
					}else{
						for (EquipmentNe ne : neArray) {
							if(StringUtils.isNotBlank(ne.getCnum())){
								if(alarmReceiveRecord.getAlarmCell().indexOf(ne.getCnum()) != -1){
									t = ne.getDhssName();
									break;
								}
							}
							
						}
					}
					int count = Integer.parseInt(map.get(t)==null?"0":map.get(t));
					map.put(t, String.valueOf(count+1));
					
					
				}
				List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
				for (String key : map.keySet()) {
					Map<String,String> m = new HashMap<String,String>();
					m.put("dhssName", key);
					m.put("value", key + " : " + map.get(key));
					listMap.add(m);
				}
				return listMap;
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return new ArrayList<Map<String,String>>();
			}
	    }
		
		 
	 	
}

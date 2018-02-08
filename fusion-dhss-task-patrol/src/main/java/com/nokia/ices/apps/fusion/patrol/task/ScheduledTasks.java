package com.nokia.ices.apps.fusion.patrol.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.jpa.SearchFilter.Operator;
import com.nokia.ices.apps.fusion.patrol.callable.SmartCheckCallable;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckJob;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckResult;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckResultTmp;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckResultTmpExecFlag;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckScheduleResult;
import com.nokia.ices.apps.fusion.patrol.pool.ThreadPoolManager;
import com.nokia.ices.apps.fusion.patrol.producer.TaskMessageProducer;
import com.nokia.ices.apps.fusion.patrol.repository.SmartCheckResultTmpRepository;
import com.nokia.ices.apps.fusion.patrol.service.SmartCheckJobService;

@Component
@EnableScheduling
public class ScheduledTasks {
	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//	private static final  Logger log = LogManager.getLogger(ScheduledTasks.class.getName());
    
	private Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	@Autowired
    private SmartCheckJobService smartCheckJobService;
	
	
	@Autowired
	private TaskMessageProducer messageProducer;

	@Autowired
	private SmartCheckResultTmpRepository smartCheckResultTmpRepository;
   

	/*@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@JmsListener(destination = "SMART-JOB-EXEC", containerFactory = "jmsContainerFactory") 
    public void reportCurrentTime(Message message) {
		log.info("DHSS|SMART|JOB|START"); 
		try {
			TextMessage messages = (TextMessage) message;
			String msgBody = message.getStringProperty("msgBody");
			Integer msgCode = message.getIntProperty("msgCode");
			log.info("消息返回信息  --- ：msgCode:{},msgBody:{}", msgCode, msgBody);
			if (null != msgBody) {
				Map<String, String> json = (Map<String, String>) new JsonMapper().fromJson(msgBody, Map.class);
				String taskName = json.get("taskName").toString();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("jobName_EQ",  taskName); 
				map.put("execFlag_EQ",  "1");
				map.put("isDisable_EQ",  "0");
				List<SmartCheckJob> jobList = smartCheckJobService.getExecJob(map);
				if (jobList.size() == 0) {
					log.info("未找到[{}]定时任务",taskName);
				} else {
					smartCheckJobService.saveScheduleResult(map, jobList);
					List<SmartCheckResultTmp> itemList = null;
					// 定义每次可同时执行指令的个数
					ThreadPoolManager manager = ThreadPoolManager.getInstance();
					itemList = smartCheckJobService.getCheckDetailTmpList(map);
					if (itemList.size() > 0) {
						log.info("开始执行定时任务...");
						try {
							for (SmartCheckResultTmp item : itemList) {
								// 单项状态在结果表里记录
								SmartCheckCallable<?> scallable = new SmartCheckCallable(item, messageProducer);
								manager.submit(scallable);
								item.setExecFlag(SmartCheckResultTmpExecFlag.EXECUTING);
								smartCheckResultTmpRepository.save(item);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			log.info("DHSS|SMART|JOB|END"); 
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error("DHSS|SMART|JOB|END"); 
		}
    }*/
	
	
	
	
	
	
	
	
	
	
	
	
	@Scheduled(cron="0 0 12 * * ?")
	public void deleTempData(){
		smartCheckJobService.clearDate();
	}
	
	

	@SuppressWarnings("rawtypes")
	@Scheduled(cron="0 0/1 * * * ?") 
    public void reportCurrentTime() {
		GregorianCalendar todayCal = new GregorianCalendar();
		// 获取当前时间，从数据库查找符合此时间条件的记录
		String thisTime = format.format(todayCal.getTime());
		String groupTime = format.format(todayCal.getTime().getTime()-1000*60*15);
		log.info("...at:{}" , thisTime);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("jobHour", thisTime);
		map.put("groupTime", groupTime);
		List<SmartCheckJob> jobList = smartCheckJobService.getScheduleResult(map);
		if (jobList.size() == 0) {
			log.info("not :{} task ",groupTime+ "   ---  "+thisTime);
		} else {
			smartCheckJobService.saveScheduleResult(map, jobList);
			List<SmartCheckResultTmp> itemList = null;
			// 定义每次可同时执行指令的个数
			ThreadPoolManager manager = ThreadPoolManager.getInstance();
			itemList = smartCheckJobService.getCheckDetailTmpList(map);
			if (itemList.size() > 0) {
				log.info("EXEC TASK...");
				try {
					for (SmartCheckResultTmp item : itemList) {
						// 单项状态在结果表里记录
						SmartCheckCallable<?> scallable = new SmartCheckCallable(item, messageProducer);
						manager.submit(scallable);
						item.setExecFlag(SmartCheckResultTmpExecFlag.EXECUTING);
						smartCheckResultTmpRepository.save(item);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
    }
    
}

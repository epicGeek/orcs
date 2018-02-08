/*package com.nokia.ices.apps.fusion.patrol.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nokia.ices.apps.fusion.mybatis.MapperLoadDao;
import com.nokia.ices.apps.fusion.patrol.repository.SmartCheckDao;
import com.nokia.ices.apps.fusion.patrol.service.SmartCheckService;



*//**
 * 智能巡检查询
 * 
 * @author ls
 * 
 *//*
@Service("smartCheckService")
public class SmartCheckServiceImpl implements SmartCheckService,InitializingBean {
	
	private final static Logger logger = LoggerFactory.getLogger(SmartCheckServiceImpl.class);

	SmartCheckDao smartCheckDao;
	
	@Autowired
    MapperLoadDao mapperLoadDao;
	
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");

	
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nsn.ices.apps.dhlr.service.intelligentInspection.impl.SmartCheckService
	 * #getSmartCheckJobResultPageList(java.util.Map)
	 
	public Map<String, Object> getSmartCheckJobResultPageList(Map<String, Object> params) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> pageList = smartCheckDao.getSmartCheckJobResultPageList(params);
		int total = smartCheckDao.getSmartCheckJobResultCount(params);
		
		resultMap.put("totalElements", total);
		resultMap.put("content", pageList);
		return resultMap;
	}

	
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nsn.ices.apps.dhlr.service.intelligentInspection.impl.SmartCheckService
	 * #getSmartCheckResultPageList(java.util.Map)
	 
	public Map<String, Object> getSmartCheckResultPageList(Map<String, Object> params) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> pageList = smartCheckDao.getSmartCheckResultPageList(params);
		int total = smartCheckDao.getSmartCheckResultCount(params);
		resultMap.put("totalElements", total);
		resultMap.put("content", pageList);
		return resultMap;
	}

	public Map<String, Object> getSmartCheckDetailResultPageList(Map<String, Object> params) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> pageList = smartCheckDao.getSmartCheckDetailResultPageList(params);
		// update by leisheng at 2015-7-17 高洁要求异常行高亮
		// 对有异常的添加属性DT_RowClass:'bg-danger'
		for (Map<String, Object> m : pageList) {
			// 1为异常
			if ((Boolean) m.get("RESULT_CODE")) {
				m.put("DT_RowClass", "bg-danger");
			}
		}
		// end update

		int total = smartCheckDao.getSmartCheckDetailResultCount(params);
		resultMap.put("totalElements", total);
		resultMap.put("content", pageList);
		return resultMap;
	}

	public List<Map<String, Object>> getDownLoadPath(Map<String, Object> params) {
		params.put("isExport", "1");
		// NE_TYPE_ID,NE_TYPE_NAME,NE_ID,NE_NAME,UNIT_ID,UNIT_NAME,UNIT_TYPE_ID,UNIT_TYPE_NAME,CHECK_ITEM_ID,CHECK_ITEM_NAME,
		// RESULT_CODE,ifNull(ERROR_MESSAGE,'')
		// ERROR_MESSAGE,ifNull(FILE_PATH,'') FILE_PATH,SCHEDULE_ID
		List<Map<String, Object>> pageList = smartCheckDao.getSmartCheckDetailResultPageList(params);

		return pageList;
	}

	public Map<String, Object> getSmartCheckJob(Map<String, Object> params) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> pageList = smartCheckDao.getSmartCheckJob(params);

		int total = smartCheckDao.getSmartCheckJobCount(params);
		resultMap.put("totalElements", total);
		resultMap.put("content", pageList);
		return resultMap;
	}

	public Integer saveJob(Map<String, Object> map) throws Exception {
		int flag = 1;
		if (map.get("jobId") == null) {
			smartCheckDao.addJob(map);
		} else {
			Map param = new HashMap();
			param.put("jobId", map.get("jobId"));
			List<Map<String, Object>> result = smartCheckDao.getSmartCheckJob(param);
			for (Map<String, Object> rm : result) {
				String jobDay = (String) map.get("jobDay");
				Integer execFlag = (Integer) rm.get("EXEC_FLAG");
				Integer jobType = Integer.valueOf((String) map.get("jobType"));
				String jobHour = (String) map.get("jobHour");
				if (execFlag == 1) {
					String[] nextDay = getNextTime(jobDay, jobHour, jobType).split(" ");
					if (jobType == 0) {
						map.put("nextDay", nextDay[0]);
						map.put("loopHour", nextDay[1]);
					} else {
						map.put("nextDay", nextDay[0]);
						map.put("loopHour", jobHour);
					}
				}
			}

			smartCheckDao.updateJob(map);
		}
		return flag;
	}

	public void removeJobByID(String jobId) {
		// 删除方案，删除方案对应的网元及任务
		smartCheckDao.removeJobByID(jobId);
		smartCheckDao.removeNeByJobID(jobId);
		smartCheckDao.removeScheduleByJobID(jobId);
	}

	public List<Map<String, Object>> getNeList(Map<String, Object> map) {
		return smartCheckDao.getNeList(map);
	}

	public void saveNeList(Map<String, Object> map) {

		int count = smartCheckDao.getCountJobNe(map);
		if (count > 0) {
			smartCheckDao.deleteNe(map);
		} else {
			smartCheckDao.saveNeList(map);
		}

	}

	public void batchUpdate(String jobId, String addOrRemove, List<Map> neList) {
		if ("add".equals(addOrRemove.toLowerCase())) {
			smartCheckDao.batchUpdate(neList);
		} else {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("jobId", jobId);
			smartCheckDao.deleteNe(m);
		}

	}

	public List<Map<String, Object>> getCheckItemList(Map<String, Object> map) {
		return smartCheckDao.getCheckItemList(map);
	}

	public void saveCheckItemList(Map<String, Object> map) {

		int count = smartCheckDao.getCountJobItem(map);
		if (count > 0) {
			smartCheckDao.deleteCheckItem(map);
		} else {
			smartCheckDao.saveCheckItemList(map);
		}

	}

	public void batchUpdateCheckItem(String jobId, String addOrRemove, List<Map> checkItemList) {
		if (checkItemList.size() > 0) {
			smartCheckDao.batchUpdateCheckItem(checkItemList);
		} else {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("jobId", jobId);
			smartCheckDao.deleteCheckItem(m);
		}

	}

	public Integer updateJobState(String jobId) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("jobId", jobId);
		m.put("isExport", "1");
		List<Map<String, Object>> result = smartCheckDao.getSmartCheckJob(m);
		for (Map<String, Object> rm : result) {
			Integer execFlag = (Integer) rm.get("EXEC_FLAG");

			String nextDay = (String) rm.get("NEXT_DAY");
			String execDay = (String) rm.get("EXEC_DAY");
			String execTime = (String) rm.get("EXEC_TIME");
			Integer jobType = (Integer) rm.get("JOB_TYPE");
			if (execFlag == 1) {
				execFlag = 2;
				nextDay = null;
			} else {
				execFlag = 1;
				// 判断是否选择了网元和任务
				List<Map<String, Object>> neItem = smartCheckDao.getCountNeAndItem(jobId);
				for (Map neMap : neItem) {
					if (neMap.get("JOB_ID") == null || neMap.get("AMOUNT1") == null || neMap.get("AMOUNT2") == null) {
						return 0;
					}
				}
				String[] nextDays = getNextTime(execDay, execTime, jobType).split(" ");
				nextDay = nextDays[0];
				m.put("nextDay", nextDay);

				if (jobType == 0) {
					m.put("loopHour", nextDays[1]);
				} else {
					m.put("loopHour", execTime);
				}
			}
			m.put("execFlag", execFlag);
			smartCheckDao.updateJob(m);
			break;
		}
		return 1;

	}

	private static String getNextTime(String execDay, String execTime, int jobType) {
		GregorianCalendar todayCal = new GregorianCalendar();

		GregorianCalendar lastCal = new GregorianCalendar();

		String lastDate = execDay + " " + execTime;
		String today = format.format(todayCal.getTime());
		String returnDay = null;
		try {
			todayCal.setTime(format.parse(today));
			lastCal.setTime(format.parse(lastDate));
			// 如果还未到开始执行的时间，则下次执行时间=开始执行时间
			if (!lastCal.before(todayCal)) {
				return lastDate;
			} else {
				switch (jobType) {
				case 0:
					// 今天加时间跟执行时间相比，晚于当前时间直接返回
					String hDay = dayFormat.format(todayCal.getTime());
					String hTime = hDay + " " + execTime;
					lastCal.setTime(format.parse(hTime));
					if (lastCal.after(todayCal)) {
						return hTime;
					}

					// 按小时则当前小时加一小时返回
					GregorianCalendar hourCal = new GregorianCalendar();
					hourCal.add(GregorianCalendar.HOUR_OF_DAY, 1);
					hourCal.set(GregorianCalendar.MINUTE, Integer.valueOf(execTime.split(":")[1]));
					returnDay = format.format(hourCal.getTime());
					break;
				case 1:
					// 今天加时间跟执行时间相比
					String tDay = dayFormat.format(todayCal.getTime());
					String tTime = tDay + " " + execTime;
					lastCal.setTime(format.parse(tTime));
					if (lastCal.after(todayCal)) {
						return tDay;
					}

					// 按天则当前日期加一天返回
					GregorianCalendar dayCal = new GregorianCalendar();
					dayCal.add(GregorianCalendar.DAY_OF_YEAR, 1);
					returnDay = dayFormat.format(dayCal.getTime());
					break;
				case 2:
					int todayWeek = todayCal.get(java.util.Calendar.DAY_OF_WEEK);
					int lastWeek = lastCal.get(java.util.Calendar.DAY_OF_WEEK);
					if (todayWeek > lastWeek) {
						// 如果执行时间所在的周天比今天所在的周天早，则执行时间为下周

						todayCal.add(GregorianCalendar.DAY_OF_YEAR, 7 - (todayWeek - lastWeek));
						returnDay = dayFormat.format(todayCal.getTime());

					} else if (todayWeek < lastWeek) {
						// 如果执行时间所在的周天比今天所在的周天晚，则推到本周第几天
						todayCal.add(GregorianCalendar.DAY_OF_YEAR, lastWeek - todayWeek);
						returnDay = dayFormat.format(todayCal.getTime());

					} else {
						// 如果是今天执行，且时间已经过去，则下次执行时间为下周
						todayCal.add(GregorianCalendar.DAY_OF_YEAR, 7);
						returnDay = dayFormat.format(todayCal.getTime());

					}
					break;
				case 3:
					// 按月则上次执行日期+（当前日期所在的月-上次执行所在的月）
					// 获取开始时间的天，加上当前的月，拼一个时间,如果这个时间比当前时间晚那就是下次运行时间，
					// 如果这个时间比当前时间早就把月加一个月拼一个时间做下次运行时间
					int todayMonth = todayCal.get(java.util.Calendar.MONTH);
					lastCal.set(GregorianCalendar.MONTH, todayMonth);
					if (lastCal.before(todayCal)) {
						lastCal.add(GregorianCalendar.MONTH, 1);
					}
					returnDay = dayFormat.format(lastCal.getTime());
					break;
				default:
					break;
				}
			}

		} catch (ParseException e) {

			e.printStackTrace();
		}
		return returnDay;
	}
	
	 @Override
	 public void afterPropertiesSet() throws Exception {
	        // TODO Auto-generated method stub
	        logger.debug("init bossBusinessDao Bean success.............");
	        smartCheckDao = mapperLoadDao.getObject(SmartCheckDao.class);
	        
	 }
}
*/
package com.nokia.ices.apps.fusion.patrol.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.persistence.criteria.Predicate.BooleanOperator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.command.domain.CommandCheckItem;
import com.nokia.ices.apps.fusion.command.repository.CommandCheckItemRepository;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentNeRepository;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.jpa.SearchFilter.Operator;
import com.nokia.ices.apps.fusion.patrol.config.SmartProjectProperties;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckJob;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckResult;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckResultTmp;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckScheduleResult;
import com.nokia.ices.apps.fusion.patrol.repository.SmartCheckJobRepository;
import com.nokia.ices.apps.fusion.patrol.repository.SmartCheckResultRepository;
import com.nokia.ices.apps.fusion.patrol.repository.SmartCheckResultTmpRepository;
import com.nokia.ices.apps.fusion.patrol.repository.SmartCheckScheduleRepository;
import com.nokia.ices.apps.fusion.patrol.repository.SmartCheckScheduleResultRepository;
import com.nokia.ices.apps.fusion.patrol.repository.SmartCheckSearchDao;
import com.nokia.ices.apps.fusion.patrol.service.SmartCheckJobService;
import com.nokia.ices.core.utils.Collections3;
import com.nokia.ices.core.utils.JsonMapper;

@Service("smartCheckJobService")
public class SmartCheckJobServiceImpl implements SmartCheckJobService {
	
	@Override
	public List<Map<String, Object>> getAllDownLoadPath(String id) {
		return smartCheckSearchDao.getSmartCheckDetailResultPageList(id);
	}

	

	@Autowired
	SmartCheckResultRepository smartCheckResultRepository;

	@Autowired
	SmartCheckScheduleResultRepository smartCheckScheduleResultRepository;

	@Autowired
	SmartCheckScheduleRepository smartCheckScheduleRepository;

	@Autowired
	SmartCheckJobRepository smartCheckJobRepository;

	@Autowired
	SmartCheckSearchDao smartCheckSearchDao;

	@Autowired
	EquipmentUnitRepository equipmentUnitRepository;

	@Autowired
	EquipmentNeRepository equipmentNeRepository;

	@Autowired
	SmartCheckResultTmpRepository smartCheckResultTmpRepository;
	
	@Autowired
	private JmsTemplate  jmsTemplate;
	

	@Autowired
	private CommandCheckItemRepository commandCheckItemRepository;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private static final Logger log = LoggerFactory.getLogger(SmartCheckJobServiceImpl.class);
	
	public static final String EXEC_NAME = "EXEC_SMART_JOB";
	@Override
	public void sendMessage(SmartCheckJob smart,int status){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("sessionId", UUID.randomUUID().toString().replace("-", ""));
		map.put("messageCode", SmartProjectProperties.getMessageCode());
		map.put("srcQueue", SmartProjectProperties.getSrcQueue());
		map.put("appQueue", SmartProjectProperties.getAppQueue());
		map.put("taskType", 4);
		map.put("taskName", SmartProjectProperties.getTaskName());
		map.put("serviceName", smart.getJobName());
		map.put("taskParam", "");
		map.put("startTime", smart.getNextDay());
		map.put("endTime", "");
		
		int type = smart.getJobType();
		Map<String, Object> contentMap = new HashMap<String, Object>();
		contentMap.put("rule", type == 1 ? "day" : (type == 2 ? "week" : "month"));
		contentMap.put("ruleMintue", "");
		contentMap.put("ruleHour", "");
		contentMap.put("ruleDay", "");
		contentMap.put("ruleWeek", "");
		contentMap.put("ruleMonth", "");
		map.put("ruleContent", contentMap);
		map.put("ruleDesc", smart.getJobDesc());
		map.put("startStatus", smart.getExecFlag() == 1 ? 2 : 3);
		map.put("startMethod", 2);
		map.put("operateStatus", status);
		map.put("jarName", SmartProjectProperties.getJarName());
		map.put("isKill", 3);
		map.put("isLog", 2);
		map.put("flag1", "");
		map.put("flag2", "");
		map.put("flag3", "");
		jmsTemplate.setDefaultDestinationName(SmartProjectProperties.getSmartJobQueue());
		jmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				TextMessage txtMessage = session.createTextMessage("");
				txtMessage.setStringProperty("msgBody", new JsonMapper().toJson(map));
				txtMessage.setIntProperty("msgCode",
						Integer.parseInt(map.get("messageCode").toString()));
				txtMessage.setJMSPriority(5);
				log.info("add job task :{}",map);
				return txtMessage;
			}
		});
	}


	/**
	 * 根据条件分页查询
	 */
	@Override
	public Page<SmartCheckScheduleResult> getSmartCheckJobResultPageList(
			Map<String, Object> searchFields, Pageable pageable) {
		String jobName = searchFields.get("jobName").toString();
		String startTime = searchFields.get("startTime").toString();
		String endTime = searchFields.get("endTime").toString();
		List<SearchFilter> searchFilter = new ArrayList<SearchFilter>();
		if (StringUtils.isNotEmpty(jobName)){
			searchFilter.add(new SearchFilter("jobName", Operator.LIKE, jobName));
		}
		try {
			if (StringUtils.isNotEmpty(startTime)) {
				searchFilter.add(new SearchFilter("startTime", Operator.GE, sdf.parse(startTime)));
			}

			if (StringUtils.isNotEmpty(endTime)) {
				searchFilter.add(new SearchFilter("startTime", Operator.LE,sdf.parse(endTime)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Specification<SmartCheckScheduleResult> specList = DynamicSpecifications.bySearchFilter(searchFilter, BooleanOperator.AND,SmartCheckScheduleResult.class);
		return smartCheckScheduleResultRepository.findAll(specList, pageable);
	}

	public List<Map<String, Object>> getDownLoadPath(Map<String, Object> params) {
		params.put("isExport", "1");
		List<Map<String, Object>> pageList = smartCheckSearchDao
				.getSmartCheckDetailResultPageList(params);
		return pageList;
	}
	
	@Override
	public List<SmartCheckResult> findSmartCheckResultByFilter(Map<String, Object> map) {
		Map<String,SearchFilter> filter = SearchFilter.parse(map);
        Specification<SmartCheckResult> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, SmartCheckResult.class);
        return smartCheckResultRepository.findAll(spec);
	}

	@Override
	public Page<SmartCheckResult> getSmartCheckDetailResultPageList(
			Map<String, Object> map, Pageable pageable) {
		List<SearchFilter> searchFilterAND = new ArrayList<SearchFilter>();
		String unitId = map.get("unitId").toString();
		String neId = map.get("neId").toString();
		String neTypeId = map.get("neTypeId").toString();
		String unitTypeId = map.get("unitTypeId").toString();
		String checkItemId = map.get("checkItemId").toString();
		String checkItemName = map.get("checkItemName").toString();
		String scheduleId = map.get("scheduleId").toString();
		String resultCode = map.get("resultCode").toString();
		// if(StringUtils.isNotEmpty(id))
		// searchFilterAND.add(new SearchFilter("id",Operator.EQ,id));
		if (StringUtils.isNotEmpty(neId))
			searchFilterAND.add(new SearchFilter("neId", Operator.EQ, neId));
		if (StringUtils.isNotEmpty(unitId))
			searchFilterAND
					.add(new SearchFilter("unitId", Operator.EQ, unitId));
		if (StringUtils.isNotEmpty(neTypeId))
			searchFilterAND.add(new SearchFilter("neType", Operator.EQ,
					neTypeId));
		if (StringUtils.isNotEmpty(unitTypeId))
			searchFilterAND.add(new SearchFilter("unitType", Operator.EQ,
					EquipmentUnitType.valueOf(unitTypeId).ordinal()));
		
		if (StringUtils.isNotEmpty(checkItemId))
			searchFilterAND.add(new SearchFilter("checkItemId", Operator.EQ,
					checkItemId));
		if (StringUtils.isNotEmpty(scheduleId))
			searchFilterAND.add(new SearchFilter("scheduleId", Operator.EQ,
					scheduleId));
		if (StringUtils.isNotEmpty(resultCode))
			searchFilterAND.add(new SearchFilter("resultCode", Operator.EQ,
					resultCode.equals("1") ? true : false));
		Specification<SmartCheckResult> speciFicationsAND = DynamicSpecifications
				.bySearchFilter(searchFilterAND, BooleanOperator.AND,
						SmartCheckResult.class);

		List<SearchFilter> searchFilterOR = new ArrayList<SearchFilter>();
		if (StringUtils.isNotEmpty(checkItemName)) {
			searchFilterOR.add(new SearchFilter("checkItemName", Operator.LIKE,
					checkItemName));
			searchFilterOR.add(new SearchFilter("unitName", Operator.LIKE,
					checkItemName));
		}

		Specification<SmartCheckResult> speciFicationsOR = DynamicSpecifications
				.bySearchFilter(searchFilterOR, BooleanOperator.OR,
						SmartCheckResult.class);
		return smartCheckResultRepository.findAll(
				Specifications.where(speciFicationsAND).and(speciFicationsOR),
				pageable);
	}




	@Override
	public int getSmartCheckJobCount(Map<String, Object> map) {
		String jobType = map.get("jobType").toString();
		String jobName = map.get("jobName").toString();
		List<SearchFilter> searchFilterAND = new ArrayList<SearchFilter>();
		if (StringUtils.isNotEmpty(jobType))
			searchFilterAND.add(new SearchFilter("jobType", Operator.EQ,
					jobType));
		Specification<SmartCheckJob> specificationAND = DynamicSpecifications
				.bySearchFilter(searchFilterAND, BooleanOperator.AND,
						SmartCheckJob.class);

		List<SearchFilter> searchFilterOR = new ArrayList<SearchFilter>();
		if (StringUtils.isNotEmpty(jobName)) {
			searchFilterOR
					.add(new SearchFilter("jobName", Operator.EQ, jobName));
			searchFilterOR
					.add(new SearchFilter("jobDesc", Operator.EQ, jobName));
		}
		Specification<SmartCheckJob> specificationOR = DynamicSpecifications
				.bySearchFilter(searchFilterOR, BooleanOperator.OR,
						SmartCheckJob.class);
		// Specification<SmartCheckJob> speciFications
		// =DynamicSpecifications.concatSpecification(BooleanOperator.AND,
		// specificationAND,specificationOR);
		return (int) smartCheckJobRepository.count(Specifications.where(
				specificationAND).and(specificationOR));
	}


	@Override
	public SmartCheckJob getCheckJobById(long id) {
		return smartCheckJobRepository.findOne(id);
	}

	@Override
	public Page<SmartCheckJob> getSmartCheckJob(String jobName, Object object,
			Pageable pageable) {
		List<SearchFilter> searchFilterOR = new ArrayList<SearchFilter>();
		if (StringUtils.isNotEmpty(jobName)) {
			searchFilterOR.add(new SearchFilter("jobName", Operator.LIKE,
					jobName));
			searchFilterOR.add(new SearchFilter("jobDesc", Operator.LIKE,
					jobName));
		}
		Specification<SmartCheckJob> specificationOR = DynamicSpecifications
				.bySearchFilter(searchFilterOR, BooleanOperator.OR,
						SmartCheckJob.class);
		return smartCheckJobRepository.findAll(specificationOR, pageable);
	}

	
	@Override
	public Map<String, Object> getSmartCheckResultPageList(Map<String, Object> map) {
		return smartCheckSearchDao.getSmartCheckResultPageList(map);
	}
	
	@Override
	public List<Map<String, Object>> getSmartCheckCountResult() {
		return smartCheckSearchDao.getSmartCheckCountResult();
	}

	@Override
	public List<SmartCheckJob> getScheduleResult(Map<String, Object> map) {
		List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>();
		searchFilterAnd.add(new SearchFilter("execFlag",Operator.EQ,"1"));
		String jobHour = map.get("jobHour") == null ? "" : map.get("jobHour").toString();
		String groupTime = map.get("groupTime") == null ? "" : map.get("groupTime").toString();
			try {
				searchFilterAnd.add(new SearchFilter("nextDay",Operator.GE,sdf.parse(groupTime+":00")));
				searchFilterAnd.add(new SearchFilter("nextDay",Operator.LT,sdf.parse(jobHour+":00")));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		Specification<SmartCheckJob> specificationAND = DynamicSpecifications.bySearchFilter(searchFilterAnd, BooleanOperator.AND,SmartCheckJob.class);
		return smartCheckJobRepository.findAll(Specifications.where(specificationAND));
	}


	@Override
	public List<SmartCheckResultTmp> getCheckDetailTmpList(Map<String, Object> map) {
		List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>();
		String ids = map.get("ids") == null ? "" : map.get("ids").toString();
		String timeOut = map.get("timeOut") == null ? "" : map.get("timeOut").toString();
		String groupTime = map.get("groupTime") == null ? "" : map.get("groupTime").toString();
		if(StringUtils.isNotEmpty(ids)){
			searchFilterAnd.add(new SearchFilter("uuId",Operator.NOTEQ,ids));
		}
		if(StringUtils.isNotEmpty(timeOut)){
			try {
				searchFilterAnd.add(new SearchFilter("startTime",Operator.GE,sdf.parse(groupTime+":00")));
				searchFilterAnd.add(new SearchFilter("startTime",Operator.LT,sdf.parse(timeOut+":00")));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Specification<SmartCheckResultTmp> specificationOR = DynamicSpecifications.bySearchFilter(searchFilterAnd, BooleanOperator.AND,SmartCheckResultTmp.class);
		return smartCheckResultTmpRepository.findAll(Specifications.where(specificationOR));
	}
	
	@Override
	public List<EquipmentNe> findAllWithDHLR() {
		return (List<EquipmentNe>) equipmentNeRepository.findAll();
	}
	
	@Override
	public List<SmartCheckResultTmp> getTimeOutCheckDetailTmpList(
			Map<String, Object> map) {
		List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>();
		String ids = map.get("ids") == null ? "" : map.get("ids").toString();
		if(StringUtils.isNotEmpty(ids)){
			searchFilterAnd.add(new SearchFilter("uuId",Operator.EQ,ids));
		}
		Specification<SmartCheckResultTmp> specificationOR = DynamicSpecifications.bySearchFilter(searchFilterAnd, BooleanOperator.OR,SmartCheckResultTmp.class);
		return smartCheckResultTmpRepository.findAll(Specifications.where(specificationOR));
	}

	@Override
	public void saveCheckResult(SmartCheckResult result) {
		smartCheckResultRepository.save(result);
	}

	@Override
	public List<SmartCheckScheduleResult> searchCheckDetailTmp(
			Map<String, Object> map) {
		String groupTime = map.get("groupTime").toString();
		String jobHour = map.get("jobHour").toString();
		List<SearchFilter> searchFilter = new ArrayList<SearchFilter>();
		try {
				searchFilter.add(new SearchFilter("startTime", Operator.GE, sdf.parse(groupTime+":00")));
				searchFilter.add(new SearchFilter("startTime", Operator.LT, sdf.parse(jobHour+":00")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Specification<SmartCheckScheduleResult> specList = DynamicSpecifications.bySearchFilter(searchFilter, BooleanOperator.AND,SmartCheckScheduleResult.class);
		return smartCheckScheduleResultRepository.findAll(specList);
	}
	
	
	@Override
	public void saveScheduleResult(Map<String, Object> m, List<SmartCheckJob> jobList) {
		
		if(Collections3.isEmpty(jobList)){
			return;
		}
		// 保存数据到smart_check_schedule_result及TMP表
		for (SmartCheckJob smartCheckJob : jobList) {
			SmartCheckScheduleResult result = new SmartCheckScheduleResult();
			result.setStartTime(smartCheckJob.getNextDay());
			result.setAmountJob(commandCheckItemRepository.findListBySmartCheckJob(smartCheckJob).size());
			result.setAmountUnit(equipmentUnitRepository.findListBySmartCheckJob(smartCheckJob).size()*commandCheckItemRepository.findListBySmartCheckJob(smartCheckJob).size());
			result.setErrorUnit(0);
			result.setExecFlag(Byte.parseByte(String.valueOf("1")));
			result.setJobDesc(smartCheckJob.getJobDesc());
			result.setJobId(smartCheckJob.getId());
			result.setSmartCheckJob(smartCheckJob);
			result.setJobName(smartCheckJob.getJobName());
			smartCheckScheduleResultRepository.save(result);
		
		}

		// 解决UUID相同的问题
		List<SmartCheckScheduleResult> smartCheckScheduleResult = searchCheckDetailTmp(m);
		if(Collections3.isEmpty(smartCheckScheduleResult)){
			return;
		}
		for (SmartCheckScheduleResult item : smartCheckScheduleResult) {
			SmartCheckJob smartCheckJob = item.getSmartCheckJob();
			List<CommandCheckItem> checkItemSet = commandCheckItemRepository.findListBySmartCheckJob(smartCheckJob);
			Set<EquipmentUnit> equipmentUnitSet = equipmentUnitRepository.findListBySmartCheckJob(smartCheckJob);
//			smartCheckJob = smartCheckJobRepository.findOne(smartCheckJob.getId());
//			SmartCheckSchedule schedule = new SmartCheckSchedule();
//			schedule.setJobId(smartCheckJob.getId());
//			schedule = smartCheckScheduleRepository.save(schedule);
			for (EquipmentUnit unit : equipmentUnitSet) {
				EquipmentNe ne  = unit.getNe();
					if(unit.getNeType().equals(ne.getNeType())){
						for (CommandCheckItem checkItem : checkItemSet) {
								SmartCheckResultTmp resultTmp = new SmartCheckResultTmp();
								resultTmp.setUuId(UUID.randomUUID().toString().replace("-", ""));
								resultTmp.setCheckItemId(checkItem.getId());
								resultTmp.setCheckItemName(checkItem.getName());
								resultTmp.setScheduleId(item.getId());
								String defaultParamValues = "";
								if(StringUtils.isNotEmpty(checkItem.getDefaultParamValues())){
									defaultParamValues = " " + checkItem.getDefaultParamValues();
								}
								resultTmp.setCommand(checkItem.getCommand() + defaultParamValues);
								resultTmp.setUserName(unit.getLoginName());
								resultTmp.setRootPwd(unit.getRootPassword());
								resultTmp.setUnitId(unit.getId());
								resultTmp.setUnitName(unit.getUnitName());
								resultTmp.setUnitType(unit.getUnitType());
								resultTmp.setNeId(ne.getId());
								resultTmp.setNeName(ne.getNeName());
								resultTmp.setNeType(ne.getNeType());
								resultTmp.setStartTime(item.getStartTime());
//								resultTmp.setResultCode(resultCode);
								smartCheckResultTmpRepository.save(resultTmp);
								
							
						}
					}
			}
		}
	
	
		

	}
	
	@Override
	 public void invokeTimeOut(int threadTimeOut) {
			GregorianCalendar todayCal = new GregorianCalendar();
			// 获取当前时间，从数据库查找符合此时间条件的记录
			String thisTime = format.format(todayCal.getTime());

			Map<String, Object> m = new HashMap<String, Object>();
			m.put("timeOut", threadTimeOut);
			// update by leisheng at 2015-8-19 由于单步操作执行太慢，改为批量数据库执行
			List<SearchFilter> searchFilter = new ArrayList<SearchFilter>();
			searchFilter.add(new SearchFilter("TIMESTAMPDIFF(MINUTE,startTime,NOW())", Operator.GE,threadTimeOut ));
			Specification<SmartCheckResultTmp> specificationOR = DynamicSpecifications.bySearchFilter(searchFilter, BooleanOperator.OR,SmartCheckResultTmp.class);
			List<SmartCheckResultTmp> data = smartCheckResultTmpRepository.findAll(Specifications.where(specificationOR));
			if (data.size() > 0) {
				m.put("resultCode", (short) 1);
				m.put("errorMessage", "巡检任务执行超过" + threadTimeOut + "分钟");
				m.put("result", "智能巡检结果");
				m.put("thisTime", thisTime);

				// 超时未返回的数据， 先删除cache超时的记录
//				for (SmartCheckResultTmp item : data) {
//					CheckResultCache.getResultCache().remove(item.getUnitId());
//				}
				// 写入smart_check_result表，状态为超时,调用告警接口，异常信息为执行超时
				for (SmartCheckResultTmp smartCheckResultTmp : data) {
					SmartCheckResult smartCheckResult = new SmartCheckResult();
					smartCheckResult.setNeId(smartCheckResultTmp.getNeId());
					smartCheckResult.setNeType(smartCheckResultTmp.getNeType());
					smartCheckResult.setNeName(smartCheckResultTmp.getNeName());
					smartCheckResult.setUnitId(smartCheckResultTmp.getUnitId());
					smartCheckResult.setUnitType(smartCheckResultTmp.getUnitType());
					smartCheckResult.setUnitName(smartCheckResultTmp.getUnitName());
					smartCheckResult.setCheckItemId(smartCheckResultTmp.getCheckItemId());
					smartCheckResult.setCheckItemName(smartCheckResultTmp.getCheckItemName());
					smartCheckResult.setScheduleId(smartCheckResultTmp.getScheduleId());
					smartCheckResult.setResultCode(true);
					smartCheckResult.setErrorMessage("巡检任务执行超过" + threadTimeOut + "分钟");
					smartCheckResult.setFilePath(smartCheckResultTmp.getFilePath());
					smartCheckResult.setStartTime(smartCheckResultTmp.getStartTime());
					saveCheckResult(smartCheckResult);
				}
				//smartCheckJobService.saveCheckDetailTmpList(m);
				// 保存到告警表
				//alarmMonitorDao.saveTimeOutMonitors(m);
				// 删除tmp表超时的记录
				smartCheckResultTmpRepository.delete(data);
				//smartCheckJobService.deleteTimeOutCheckDetailTmpList(m);

			} else {
				log.info("未找到超时未返回的记录");
			}
		}

	

	
}

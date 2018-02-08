package com.nokia.ices.apps.fusion.patrol.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.command.domain.CommandCheckItem;
import com.nokia.ices.apps.fusion.command.repository.CommandCheckItemRepository;
import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentNeType;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;
import com.nokia.ices.apps.fusion.jms.consumer.MessageConsumer;
import com.nokia.ices.apps.fusion.jms.producer.DHSSMessageProducer;
import com.nokia.ices.apps.fusion.patrol.common.JsonMapper;
import com.nokia.ices.apps.fusion.patrol.common.ZipUtil;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckJob;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckResult;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckResultTmp;
import com.nokia.ices.apps.fusion.patrol.mml.messages.Message76001;
import com.nokia.ices.apps.fusion.patrol.repository.SmartCheckResultRepository;
import com.nokia.ices.apps.fusion.patrol.service.SmartCheckTaskService;



@Service("smartCheckJobTaskService")
public class SmartCheckJobTaskServiceImpl implements SmartCheckTaskService {

	
	private static SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	@Autowired
	ProjectProperties projectProperties;
	private int threadTimeOut = 30;
	@Autowired
	DHSSMessageProducer dHSSMessageProducer;

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
    private JmsTemplate jmsTemplate;
	
	@Autowired
	private SmartCheckResultRepository smartCheckResultRepository;
	
	
	@Autowired
	CommandCheckItemRepository commandCheckItemRepository;

	private final static Logger logger = LoggerFactory
			.getLogger(MessageConsumer.class);

	

	/**
	 * 从AMQ接收到命令执行结果后执行该方法
	 * 
	 * @param session
	 *            发送AMQ消息的唯一标识
	 * @param resultCode
	 *            检查项命令执行结果码
	 * @param log
	 *            检查项命令结果日志路径
	 *            
	 *          
	 */
	
	@Override
	public void persist(String session, String resultCode, String location,Map<String, Object> smartCheckResultTmpMap,CommandCheckItem check) {
			SmartCheckResultTmp resultTmp = new SmartCheckResultTmp();
			resultTmp.setNeName(smartCheckResultTmpMap.get("ne_name") == null ? "" : smartCheckResultTmpMap.get("ne_name").toString());
			resultTmp.setNeType(EquipmentNeType.valueOf(smartCheckResultTmpMap.get("ne_type") == null ? "" : smartCheckResultTmpMap.get("ne_type").toString()));
			resultTmp.setUnitName(smartCheckResultTmpMap.get("unit_name") == null ? "" : smartCheckResultTmpMap.get("unit_name").toString());
			resultTmp.setUnitType(EquipmentUnitType.valueOf(smartCheckResultTmpMap.get("unit_type") == null ? "" : smartCheckResultTmpMap.get("unit_type").toString()));
			resultTmp.setCheckItemName(smartCheckResultTmpMap.get("check_item_name") == null ? "" : smartCheckResultTmpMap.get("check_item_name").toString());
			resultTmp.setUuId(session);
			resultTmp.setUserName(smartCheckResultTmpMap.get("user_name") == null ? "" : smartCheckResultTmpMap.get("user_name").toString());
			resultTmp.setRootPwd(smartCheckResultTmpMap.get("root_pwd") == null ? "" : smartCheckResultTmpMap.get("root_pwd").toString());
			resultTmp.setCommand(smartCheckResultTmpMap.get("command") == null ? "" : smartCheckResultTmpMap.get("command").toString());
			resultTmp.setId(smartCheckResultTmpMap.get("id") == null ? null : Long.parseLong(smartCheckResultTmpMap.get("id").toString()));
			resultTmp.setCheckItemId(smartCheckResultTmpMap.get("check_item_id") == null ?  null : Long.parseLong(smartCheckResultTmpMap.get("check_item_id").toString()));
			// 报文获取后更新数据库报文状态及日志路径，并调用集中操作接口解析
			logger.info( "Get ne log success:session={},resultCode={},location={},itemName={}", session, resultCode, location, resultTmp.getCheckItemName());
			if ("0".equals(resultCode)) {
				resultTmp.setLogState(true);
				resultTmp.setFilePath(location);
			} else {
				resultTmp.setLogState(false);
				resultTmp.setErrorMessage(location);
			}
			updateCheckItem(resultTmp);
			if (!resultTmp.isLogState()) {
				Object obj = smartCheckResultTmpMap.get("message_log");
				logger.info(resultTmp.getNeName() + " : " + obj);
				resultTmp.setResultCode(false);
				resultTmp.setErrorMessage(obj == null ? "" : obj.toString());
				saveAlarm(resultTmp);
//				invokeCheckItem(resultTmp);
			} else {
				try {
					// 压缩LUA脚本
					String zipScript = ZipUtil.compress(StringUtils.isEmpty(check.getScript()) ?  "" : check.getScript());
					logger.info("Start to invoke sendMessage76001({},1,{})", resultTmp.getUuId(), resultTmp.getFilePath());
					// 如果脚本不为空则调用76001进行解析，如果为空则结束
					if (StringUtils.isNotEmpty(check.getScript())) {
						StringBuilder paramsBuilder = new StringBuilder();
						paramsBuilder.append(resultTmp.getUserName() + ","
								+ resultTmp.getRootPwd() + ":"
								+ resultTmp.getCommand());
						logger.info( "解析定时巡检任务，在网元:{}::uuId:{}:::执行的巡检任务：{}", resultTmp.getNeName() + ":::" + resultTmp.getUnitName(),resultTmp.getUuId(), resultTmp.getCheckItemName());
						jmsTemplate.setDefaultDestinationName(ProjectProperties.getQueueDestination());
				    	jmsTemplate.send(new MessageCreator(){
							public Message createMessage(Session session) throws JMSException {
								Message76001 message = new Message76001();
								message.setDestQ(ProjectProperties.getQueueDestination());
//								message.setSessionid(resultTmp.getUuId());
								message.setSession(resultTmp.getUuId());
								message.setSrcQ(SmartCheckTaskService.TASKNAEM);
								message.setType("1");
								message.setLog_path(ProjectProperties.getCOMP_BASE_PATH()+resultTmp.getFilePath());
								TextMessage txtMessage = session.createTextMessage("");
								txtMessage.setStringProperty("msgBody", new JsonMapper().toJson(message));
								txtMessage.setIntProperty("msgCode", 76001);
								txtMessage.setJMSPriority(5);
								txtMessage.setText(zipScript);
								logger.debug("message.getSrcQ() ={},message.getDestQ() = {},txtMessag={}",message.getSrcQ() ,message.getDestQ(),txtMessage.toString());				 
								return txtMessage;
							}
						});
					} else {
						// 入库
						resultTmp.setResultCode(false);
						invokeCheckItem(resultTmp);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
	
	/**
	 * LUA解析结果返回
	 * 
	 * @param session
	 * @param resultCode
	 * @param luaResult
	 */
	@Override
	public void persistLua(String session, String resultCode, String luaResult) {
			SmartCheckResultTmp resultTmp = new SmartCheckResultTmp();
			Map<String, Object> map = getSmartCheckResultTmpByUUID(session);
			resultTmp.setNeName(map.get("ne_name") == null ? "" : map.get("ne_name").toString());
			resultTmp.setUnitName(map.get("unit_name") == null ? "" : map.get("unit_name").toString());
			resultTmp.setUnitType(EquipmentUnitType.valueOf(map.get("unit_type") == null ? "" : map.get("unit_type").toString()));
			resultTmp.setCheckItemName(map.get("check_item_name") == null ? "" : map.get("check_item_name").toString());
			resultTmp.setUuId(session);
			resultTmp.setUserName(map.get("user_name") == null ? "" : map.get("user_name").toString());
			resultTmp.setRootPwd(map.get("root_pwd") == null ? "" : map.get("root_pwd").toString());
			resultTmp.setCommand(map.get("command") == null ? "" : map.get("command").toString());
			// 解压LUA结果
			try {
				luaResult = ZipUtil.uncompress(luaResult);
			} catch (Exception e) {
				logger.debug("-------------------------");
			}
			logger.debug("Lua analysis success:session={},resultCode={},luaResult={},itemName={}",session,resultCode,luaResult,resultTmp.getCheckItemName());
			if ("0".equals(resultCode) && StringUtils.isEmpty(luaResult)) {
				resultTmp.setResultCode(true);
			} else {
				resultTmp.setResultCode(false);
				resultTmp.setErrorMessage(luaResult);
			}
//			invokeCheckItem(resultTmp);
	}
	
	@Override
	public Map<String,Object> getSmartCheckResultTmpByUUID(String session){
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from smart_check_result_tmp where uu_id = ?",session);
		return list.size() == 0 ? new HashMap<>() : list.get(0);
	}
	
	
	@Override
	public void updateNextDay(SmartCheckJob smartCheckJob){
		try {
			Date d = new Date(getNextExecuteTime(smartCheckJob.getJobType(), smartCheckJob.getNextDay().getTime()));
			String nextDay = format.format(d);
			jdbcTemplate.update("UPDATE smart_check_job SET exec_day = next_day,exec_time=next_day,next_day = ? "
							+ "WHERE id = ? ",
							new Object[] { nextDay, smartCheckJob.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
//	@Override
//	public List<Map<String, Object>> findNextDayAndTypes(String session){
//		return jdbcTemplate
//		.queryForList("SELECT * FROM smart_check_job WHERE id = (SELECT job_id FROM "
//						+ "smart_check_schedule_result where id = "
//						+ "(SELECT schedule_id FROM smart_check_result_tmp WHERE uu_id = ?) )", session);
//	}
	
	@Override
	public void updateSmartCheckResultTmp(String execFlag,String session){
		jdbcTemplate.update("update smart_check_result_tmp set exec_flag = ? where uu_id = ? ",execFlag,session);
	}
	
	@Override
	public void updateSmartCheckScheduleResults(String execFlag,String jobId,String execTime){
		jdbcTemplate.update("update smart_check_schedule_result set exec_flag = ? where job_id = ? and start_time = ? ",execFlag,jobId,execTime);
	}
	
	
	@Override
	public void updateSmartCheckResultTmp(String resultCode,String session,String errorMessage,String path){
		jdbcTemplate.update("update smart_check_result_tmp set exec_flag = ?,error_message = ?,result_code = ?,file_path = ? where uu_id = ? "
									,resultCode,errorMessage,resultCode.equals("2") ? 1 : 0,path,session);
	}
	
	@Override
	public void updateSmartCheckResultTmp(String resultCode,String session,String errorMessage){
		jdbcTemplate.update("update smart_check_result_tmp set exec_flag = ?,error_message = ?,result_code = ? where uu_id = ? ",resultCode,errorMessage,resultCode.equals("2") ? 1 : 0,session);
	}
	
	@Override
	public void addSmartCheckResult(String resultCode,Map<String,Object> map) throws ParseException{
		
		SmartCheckResult result = new SmartCheckResult();
		result.setCheckItemId(map.get("check_item_id") == null ? null : Long.parseLong(map.get("check_item_id").toString()));
		result.setCheckItemName(map.get("check_item_name") == null ? null : map.get("check_item_name").toString());
		result.setNeId(map.get("ne_id") == null ? null : Long.parseLong(map.get("ne_id").toString()));
		result.setNeName(map.get("ne_name") == null ? null : map.get("ne_name").toString());
		EquipmentNeType netype = EquipmentNeType.valueOf(map.get("ne_type") == null ? null : map.get("ne_type").toString());
		result.setNeType(netype);
		result.setNeTypeName(netype.name());
		result.setScheduleId(map.get("schedule_id") == null ? null : Long.parseLong(map.get("schedule_id").toString()));
		result.setStartTime(format.parse(map.get("start_time") == null ? null : map.get("start_time").toString()));
		result.setUnitId(map.get("unit_id") == null ? null : Long.parseLong(map.get("unit_id").toString()));
		result.setUnitName(map.get("unit_name") == null ? null : map.get("unit_name").toString());
		result.setUnitType(EquipmentUnitType.valueOf(map.get("unit_type") == null ? null : map.get("unit_type").toString()));
		result.setErrorMessage(map.get("errorMessage") == null ? null : map.get("errorMessage").toString());
		result.setFilePath(map.get("file_path") == null ? null : map.get("file_path").toString());
		result.setResultCode(resultCode.equals("2") ? true : false);
		smartCheckResultRepository.save(result);
	}
	
	@Override
	public void updateSmartCheckScheduleResultBySession(String schedule_id){
		jdbcTemplate.update("UPDATE smart_check_schedule_result SET exec_flag = ?,error_unit = "
				+ "(SELECT COUNT(DISTINCT unit_name) FROM smart_check_result_tmp WHERE schedule_id = ? AND exec_flag = ? ) "
				 +"WHERE id = ? ",new Object[] { "2",schedule_id,"3",schedule_id });
	}

	public void updateCheckItem(SmartCheckResultTmp checkItem) {
		if (StringUtils.isNotEmpty(checkItem.getFilePath()) || StringUtils.isNotEmpty(checkItem.getErrorMessage())) {
			String sql = "update smart_check_result_tmp SET ";
			List<Object> array = new ArrayList<Object>();
			boolean flag = false;
			if (StringUtils.isNotEmpty(checkItem.getFilePath())) {
				sql += "FILE_PATH =  ? ,LOG_STATE =  ? ";
				array.add(checkItem.getFilePath());
				array.add(checkItem.isLogState() ? 0 : 1 );
				flag = true;
			}
			if (StringUtils.isNotEmpty(checkItem.getErrorMessage())) {
				if(flag){ sql += ","; }
				sql += " ERROR_MESSAGE =  ? ";
				array.add(checkItem.getErrorMessage());
			}
			sql += "where id= ? ";
			array.add(String.valueOf(checkItem.getId()));
			jdbcTemplate.update(sql, array.toArray());
		}
	}

	public void invokeCheckItem(SmartCheckResultTmp checkItem) {
		String sql = "SELECT a.id,a.UU_ID AS UUID FROM smart_check_result_tmp a ";
		boolean flag = false;
		List<String> array = new ArrayList<String>();
		if (StringUtils.isNotEmpty(checkItem.getUuId())) {
			sql += " where UU_ID= ? ";
			flag = true;
			array.add(checkItem.getUuId());
		}
		if (StringUtils.isNotEmpty(String.valueOf(threadTimeOut)))  {
			sql += flag == true ? " AND TIMESTAMPDIFF(MINUTE,a.START_TIME,NOW()) > ? "
					: " where TIMESTAMPDIFF(MINUTE,a.START_TIME,NOW()) > ? ";
			array.add(String.valueOf(threadTimeOut));
		}
		List<Map<String, Object>> timeOutList = jdbcTemplate.queryForList(sql, array.toArray());
		// 如果找到了该记录就执行(过滤超时已经处理掉的记录)
		if (timeOutList.size() > 0) {
			// 异常则触发告警
			if (!checkItem.isResultCode()) {
				logger.info("{}::触发告警：{},ERROR_MESSAGE={}", checkItem.getUuId(), checkItem.isResultCode(), checkItem.getErrorMessage());
				saveAlarm(checkItem);
			}
//			saveCheckResult(checkItem);
			// 删除TMP表记录
			deleteByUUID(checkItem.getUuId());
		}

	}

	public void deleteByUUID(String uuId) {
		String sql = "delete from smart_check_result_tmp where UU_ID= ? ";
		jdbcTemplate.update(sql, uuId);
	}

	@Override
	public void saveAlarm(Map<String, Object> map) {
		SmartCheckResultTmp temp = new SmartCheckResultTmp();
		temp.setErrorMessage(map.get("errorMessage") == null ? null : map.get("errorMessage").toString());
		temp.setCheckItemName(map.get("check_item_name") == null ? null : map.get("check_item_name").toString());
		temp.setNeName(map.get("ne_name") == null ? null : map.get("ne_name").toString());
		temp.setUnitName(map.get("unit_name") == null ? null : map.get("unit_name").toString());
		temp.setNeType(EquipmentNeType.valueOf(map.get("ne_type") == null ? null : map.get("ne_type").toString()));
		temp.setFilePath(map.get("file_path") == null ? null : map.get("file_path").toString());
		temp.setUnitType(EquipmentUnitType.valueOf(map.get("unit_type") == null ? null : map.get("unit_type").toString()));
		logger.info("添加自定义告警");
		saveAlarm(temp);
		logger.info("添加自定义告警成功");
	}

	public synchronized void saveAlarm(SmartCheckResultTmp item) {
		GregorianCalendar todayCal = new GregorianCalendar();
		// 获取当前时间，从数据库查找符合此时间条件的记录
		String thisTime = format.format(todayCal.getTime());
		String sql = "insert into alarm_monitor("
				+ "alarm_content,alarm_level,alarm_scene,alarm_title,alarm_type  ,ne_name,unit_name,ne_type,start_time,file_path,unit_type) values"
				+ "( ?          , '*'       ,   ?       , ?         ,'智能巡检结果', ?     , ?       , ?     ,        ? ,     ?   ,  ? )";
		jdbcTemplate.update(sql, item.getErrorMessage(), "",
				item.getCheckItemName(), item.getNeName(), item.getUnitName(),
				item.getNeType().name(), thisTime, item.getFilePath(),item.getUnitType().name());
	}


	private static Long getNextExecuteTime(int jobType, Long startDateTime) {
		Long nextExecuteTime = 0l;
		switch (jobType) {
		case 4:
			// 15分钟
			nextExecuteTime = startDateTime + 1000 * 60 * 15;
			break;
		case 5:
			// HOUR
			nextExecuteTime = startDateTime + 1000 * 60 * 60;
			break;
		case 6:
			// 15分钟
			nextExecuteTime = startDateTime + 1000 * 60 * 5;
			break;
		case 1:
			// DAY
			nextExecuteTime = startDateTime + 1000 * 60 * 60 * 24;
			break;
		case 2:
			// WEEK
			nextExecuteTime = startDateTime + 1000 * 60 * 60 * 24 * 7;
			break;
		case 3:
			// MONTH
			int maxDate = 0;
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(new Date());
			maxDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			nextExecuteTime = startDateTime + 1000 * 60 * 60 * 24 * maxDate;
			break;
		default:
			break;
		}
		return nextExecuteTime;
	}




}

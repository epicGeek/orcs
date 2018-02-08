package com.nokia.ices.apps.fusion.jms.consumer;

import java.util.Date;
import java.util.Map;

import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.jms.CCZXJResult;
import com.nokia.ices.core.utils.JsonMapper;



@Component
public class MessageConsumer {


	@Autowired
	ProjectProperties projectProperties;

	@Autowired
    JdbcTemplate jdbcTemplate;
    
	private final static Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
			
	/*@JmsListener(destination = "DHLR-TASK", containerFactory = "jmsContainerFactory")
	public void reseiveMessageForSmartCheckJobTask(Message message){
	    logger.debug("Received 2222<" + message + ">"); 
	    
	}*/
	
	
	@JmsListener(destination = "DHLR_BACK", containerFactory = "jmsContainerFactory")
	public void reseiveMessageToBackUp(Message message){
	 try {
		String   msgBody = message.getStringProperty("msgBody");
		@SuppressWarnings("unchecked")
		Map<String, String> json = (Map<String, String>) new JsonMapper().fromJson(msgBody, Map.class);
		String resultCode = String.valueOf(json.get("resultCode"));
		String msg = String.valueOf(json.get("message"));//网元操作日志返回msg
		String[] res={resultCode.toString(),msg};
		new CCZXJResult().setRes(res);
	} catch (Exception e) {
		e.printStackTrace();
	}
		logger.debug("Received 2222<" + message + ">"); 
	}
	@JmsListener(destination = "DHLR", containerFactory = "jmsContainerFactory")
//	@JmsListener(destination = "DHLR", containerFactory = "jmsContainerFactory")
	public void receiveMessage(Message message) {
		logger.debug("Received <" + message + ">");
		logger.debug("^^^^^^^^^^^^^:" + projectProperties);
		String msgBody = null;
		Integer msgCode = new Integer(0);
		try {
			msgBody = message.getStringProperty("msgBody");
			msgCode = message.getIntProperty("msgCode");
			logger.debug("消息返回信息：{}", msgBody);
			logger.debug("消息返回Code：{}", msgCode);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != msgBody) {
			@SuppressWarnings("unchecked")
			Map<String, String> json = (Map<String, String>) new JsonMapper()
					.fromJson(msgBody, Map.class);
			String session = String.valueOf(json.get("session"));
			logger.debug("消息返回session：{}", session);
			String resultCode = String.valueOf(json.get("resultCode"));
			String eqType = String.valueOf(json.get("eqType"));
			logger.debug("消息返回resultCode：{}", resultCode);
			logger.debug("消息返回eqType：{}", eqType);
			// 根据msgCode来区分执行类型
			if (msgCode == 60014) {
				String action = String.valueOf(json.get("action"));
				logger.debug("消息返回action：{}", action);
				persistUnit(session, resultCode, action);
				logger.debug("session=" + session + "resultCode=" + resultCode);
			} else if (msgCode == 60004) {// 日志运维
				resultCode.equalsIgnoreCase("0");// 表示正常返回结果，需要保存 Log 的路径，
				// 否则返回的是错误日志，直接入库

				String msg = String.valueOf(json.get("message"));// 网元操作日志返回msg
				logger.debug("消息返回msg：{}", msg);
				String path = "";
				if ("66010".equals(resultCode)) {
					String flag = msg.split(",")[0].split(":")[1];
					int startLeng = msg.indexOf(flag) + flag.length() + 1;
					path = msg.substring(startLeng, msg.indexOf(";"));
					if ("neLog".equals(eqType)) {
						// 更新网元操作日志路径
						persistNeLog(session, path);
					} else if ("roam".equals(flag)) {

					}
				} else {
					path = msg;
				}

				if ("changePWD".equals(eqType)) {// 修改Unit密码
					logger.debug("修改Unit密码。");
					String location = (resultCode.equalsIgnoreCase("0") ? ProjectProperties
							.getCONSOLE_LOG_PATH() : "")
							+ String.valueOf(json.get("message"));
					persist(session, resultCode, location, eqType);
				} else {
					logger.debug("执行指令。");
					try {
						persist(session, resultCode, path, eqType);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				logger.debug("session=   " + session + "resultCode=   "
						+ resultCode + "path=   " + path);
			} else if (msgCode == 60006) {
				// resultCode.equalsIgnoreCase("0") 表示正常返回结果，需要保存 Log 的路径，
				// 否则返回的是错误日志，直接入库
				String location = (resultCode.equalsIgnoreCase("0") ? ProjectProperties
						.getCOMP_BASE_PATH() : "")
						+ String.valueOf(json.get("message"));
				persist(session, resultCode, location, eqType);
				logger.debug("session=" + session + "resultCode=" + resultCode
						+ "location=" + location);

			} else {
				logger.error("UNKNOWN msgCode:" + msgCode);
			}

		}
	}

	@SuppressWarnings("unchecked")
	public void persistUnit(String session, String resultCode, String action) {
		NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(
				jdbcTemplate);
		Map<String, Object> map = jdbcTemplate.queryForMap(
				"select * from equipment_unit where resource_id = ?",
				new Object[] { session });
		if (!map.isEmpty()) {

			if ("0".equals(action)) {// 增加单元
				logger.debug("增加单元action:" + action);
				logger.debug("增加单元resultCode:" + resultCode);
				if ("0".equals(resultCode)) {// 增加成功，将is_forbidden置0（启用状态）
					jdbcTemplate
							.update("update equipment_unit set is_forbidden = 0 where resource_id = ?",
									new Object[] { session });
				} else {// 增加单元失败，将记录删除。
					jdbcTemplate.update(
							"delete from equipment_unit where resource_id = ?",
							new Object[] { session });
				}
			} else if ("2".equals(action)) {// 修改单元
				logger.debug("修改单元action:" + action);
				logger.debug("修改单元resultCode:" + resultCode);
				if ("0".equals(resultCode)) {// 修改成功、更新
					Map<String, String> param_map = (Map<String, String>) new JsonMapper()
							.fromJson(map.get("temp_json").toString(),
									Map.class);
					logger.debug("修改单元temp_json:"
							+ new JsonMapper().toJson(param_map));

					namedJdbcTemplate
							.update("update equipment_unit set is_forbidden = 0,ip = :ip,"
									+ "port=:port,unit_name=:unit_name,password=:password,root_password=:root_password,username=:username"
									+ ",protocol_id=:protocol_id,unit_type_id=:unit_type_id,ne_id=:ne_id,unit_name=:unit_name where resource_id =:resource_id",
									param_map);

				} else {
					logger.debug("修改单元失败resultCode:" + resultCode);
					jdbcTemplate
							.update("update equipment_unit set is_forbidden = 0 where resource_id = ?",
									new Object[] { session });
				}
				// jdbcTemplate.update("update equipment_unit set temp_json = null where id = ",
				// session);
			} else if ("1".equals(action)) {// 删除单元
				logger.debug("删除单元action:" + action);
				logger.debug("删除单元resultCode:" + resultCode);
				if ("0".equals(resultCode)) {// 删除成功将is_forbidden = 1(禁用状态)
					jdbcTemplate
							.update("update equipment_unit set is_forbidden = 1 where resource_id = ?",
									new Object[] { session });
				} else {// 删除失败将is_forbidden =0(启用状态)
					jdbcTemplate
							.update("update equipment_unit set is_forbidden = 0 where resource_id = ?",
									new Object[] { session });
				}
			}
		}
	}

	/**
	 * 从AMQ接收到命令执行结果后执行该方法
	 * 
	 * @param unit
	 *            网元操作日志根据key返回
	 * @param resultCode
	 *            检查项命令执行结果码
	 * @param path
	 *            检查项命令结果日志路径
	 */
	public void persistNeLog(String unitName, String path) {
		// 成功进行网元操作日志update path
		if (null != unitName) {
			jdbcTemplate
					.update("update equipment_ne_operation_log set path = ? where unit_name = ? ",
							new Object[] { path, unitName });
		}
	}

	/**
	 * 从AMQ接收到命令执行结果后执行该方法
	 * 
	 * @param session
	 *            发送AMQ消息的唯一标识
	 * @param resultCode
	 *            检查项命令执行结果码
	 * @param log
	 *            检查项命令结果日志路径
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void persist(String session, String resultCode, String log,
			String eqType) {
		logger.debug("执行persist方法！");
		try {
			NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(
					jdbcTemplate);
			Map<String, Object> map = jdbcTemplate.queryForMap(
					"select * from maintain_result where id = ?",
					new Object[] { session });// 根据maintain_result的ID

			if (!map.isEmpty()) {
				logger.debug("执行persist方法");
				String unit_id = map.get("unit_resource_id").toString();// 获取单元ID
				if ("0".equalsIgnoreCase(resultCode)) {
					Map<String, Object> unit_map = jdbcTemplate
							.queryForMap(
									"select * from equipment_unit where resource_id = ?",
									new Object[] { unit_id });
					if (!unit_map.isEmpty()) {
						if ("changePWD".equals(eqType)) {
							Map<String, String> unit_param_map = (Map<String, String>) new JsonMapper()
									.fromJson(unit_map.get("temp_json")
											.toString(), Map.class);
							if (!unit_param_map.isEmpty()) {
								namedJdbcTemplate
										.update("update equipment_unit set is_forbidden = 0, password = :password,root_password=:root_password where resource_id =:resource_id",
												unit_param_map);
								// jdbcTemplate.update("update equipment_unit set temp_json = null where id = ",
								// unit_id);
							}
						}
					}
					jdbcTemplate
							.update("update maintain_result set report_path = ?,response_time = ? where id =? ",
									new Object[] { log, new Date(), session });
				} else {
					jdbcTemplate
							.update("update maintain_result error_log = ?,response_time = ? where id =? ",
									new Object[] { log, new Date(), session });
				}

				String operation_id = map.get("operation_id").toString();
				Map result_number_map = jdbcTemplate.queryForMap("SELECT COUNT(*) as result_number FROM maintain_result WHERE operation_id = ? ",new Object[] { operation_id });
				Map return_number_map = jdbcTemplate.queryForMap("SELECT COUNT(*) AS return_number FROM maintain_result WHERE operation_id = ? AND response_time IS NOT NULL",
						new Object[] { operation_id });
				Integer result_number = (Integer)result_number_map.get("result_number");
				Integer return_number = (Integer)return_number_map.get("return_number");
				logger.debug("执行persist方法result_number:" + result_number
						+ ",return_number:" + return_number);
				if (result_number == return_number) {
					jdbcTemplate
							.update("update maintain_operation set is_done = 1 where id = ?",
									new Object[] { operation_id });
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

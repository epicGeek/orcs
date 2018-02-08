package com.nokia.ices.apps.fusion.maintain.consumer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Message;
import javax.persistence.criteria.Predicate.BooleanOperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.maintain.domain.MaintainOperation;
import com.nokia.ices.apps.fusion.maintain.domain.MaintainResult;
import com.nokia.ices.apps.fusion.maintain.repository.MaintainOperationRepository;
import com.nokia.ices.apps.fusion.maintain.repository.MaintainResultRepository;
import com.nokia.ices.apps.fusion.maintain.service.MaintainService;
import com.nokia.ices.core.utils.JsonMapper;

@Component
public class SecurityConsumer {

	private final static Logger logger = LoggerFactory.getLogger(SecurityConsumer.class);

	@Autowired
	MaintainOperationRepository maintainOperationRepository;
	
	@Autowired
	MaintainResultRepository maintainResultRepository;
	
	@Autowired
	EquipmentUnitRepository equipmentUnitRepository;
	
	@Autowired
    private MaintainService maintainService;
	
	@JmsListener(destination = MaintainService.SECURITY_QNAME, containerFactory = "jmsContainerFactory")
	public void reseiveMessageForMaintain(Message message) {
		logger.debug("Received <" + message + ">");
		String msgBody = null;
		Integer msgCode = new Integer(0);
		try {
			msgBody = message.getStringProperty("msgBody");
			msgCode = message.getIntProperty("msgCode");
			logger.info("消息返回信息：{}", msgBody);
			logger.info("消息返回Code：{}", msgCode);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != msgBody) {
			@SuppressWarnings("unchecked")
			Map<String, String> json = (Map<String, String>) new JsonMapper().fromJson(msgBody, Map.class);
			String session = String.valueOf(json.get("sessionid"));
			logger.info("消息返回session：{}", session);
			String resultCode = String.valueOf(json.get("flag"));
//			String eqType = String.valueOf(json.get("eqType") == null ? "" : json.get("eqType"));
			logger.info("消息返回resultCode：{}", resultCode);
//			logger.debug("消息返回eqType：{}", eqType);
			// 根据msgCode来区分执行类型
			if (msgCode == 76000) {// 日志运维
				resultCode.equalsIgnoreCase("0");// 表示正常返回结果，需要保存 Log 的路径，
				
				String msg = resultCode.equals("0") ? String.valueOf(json.get("src")) : String.valueOf(json.get("msg"));// 网元操作日志返回msg
				logger.info("消息返回msg：{}", msg);
				try {
					persist(session, resultCode, msg/*, eqType*/);
				}catch(Exception e) {
					e.printStackTrace();
				}				
				logger.info("session=   " + session + "resultCode=   "+ resultCode + "path=   " + msg);
			}  else {
				logger.error("UNKNOWN msgCode:" + msgCode);
			}

		}
	}
	
	@SuppressWarnings("unchecked")
	public void persist(String session, String resultCode, String log/*,String eqType*/) {
		logger.info("执行persist方法！");
		try {
			MaintainResult maintainResult = maintainService.getMaintainResultByUUID(session);// 根据UUID得到maintain_result
			if (maintainResult!=null) {
				logger.info("执行persist方法");				
				if ("0".equalsIgnoreCase(resultCode)) {
					EquipmentUnit equipmentUnit= maintainResult.getUnit();//获取单元
//						if ("changePWD".equals(eqType)) {							
							Map<String, String> unit_param_map = (Map<String, String>) new JsonMapper()
									.fromJson(equipmentUnit.getTempJson(), Map.class);
							if (!unit_param_map.isEmpty()) {								
								equipmentUnit.setIsForbidden(false);
								equipmentUnit.setLoginPassword(unit_param_map.get("login_password"));
								equipmentUnit.setRootPassword(unit_param_map.get("root_password"));
								equipmentUnitRepository.save(equipmentUnit);								
							}
//						}
						maintainResult.setReportPath(log);
						maintainResult.setResponseTime(new Date());
						maintainResultRepository.save(maintainResult);
				} else {		
					maintainResult.setErrorLog(log);
					maintainResult.setResponseTime(new Date());
					maintainResultRepository.save(maintainResult);
				}

				MaintainOperation maintainOperation = maintainResult.getOperation();							
				List<MaintainResult> return_number_list = maintainResultRepository.findResultByOperationIdAndResponseTimeIsNotNull(maintainOperation.getId());
				
				Map<String,Object> paramMap  = new HashMap<>();
				paramMap.put("operation.id_EQ", maintainOperation.getId().toString());
				Map<String,SearchFilter> filter = SearchFilter.parse(paramMap);
				Specification<MaintainResult> spec = 
		                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, MaintainResult.class);
				
				
				
//				maintainOperation = maintainOperationRepository.findOne(maintainOperation.getId());
				Integer result_number = maintainResultRepository.findAll(spec).size();/*maintainOperation.getResult().size();*/
				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"+result_number);
				Integer return_number = return_number_list.size();
				logger.info("执行persist方法result_number:" + result_number+ ",return_number:" + return_number);
				if (result_number == return_number) {
					maintainOperation.setIsDone(true);
					maintainOperationRepository.save(maintainOperation);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

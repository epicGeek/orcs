package com.nokia.ices.apps.fusion.patrol.callable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentNeType;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckResultTmp;
import com.nokia.ices.apps.fusion.patrol.model.Context;
import com.nokia.ices.apps.fusion.patrol.producer.TaskMessageProducer;
import com.nokia.ices.apps.fusion.patrol.service.SmartCheckTaskService;
import com.nokia.ices.core.utils.Encodes;


public class SmartCheckCallable<V> implements Callable<V> {

	private SmartCheckResultTmp item;
	private static final Logger log = LoggerFactory.getLogger(SmartCheckCallable.class);
	public static final String commandName = "DHLR_COMMAND";
	private TaskMessageProducer messageProducer;

	public SmartCheckCallable(SmartCheckResultTmp item, TaskMessageProducer messageProducer) {
		this.item = item;
		this.messageProducer = messageProducer;
	}

	@Override
	public V call() throws Exception {

		StringBuilder paramsBuilder = new StringBuilder();
		String pwd = Encodes.encodeHex(item.getRootPwd() != null ? item.getRootPwd().getBytes() : "".getBytes());
		String [] unames = item.getUserName().split("&&");
		paramsBuilder.append(unames[0] + "," + pwd + ":" + item.getCommand());
		log.info("开始执行定时巡检任务，在网元:{}::uuId:{}:::执行的巡检任务：{}", item.getNeName() + ":::" + item.getUnitName(),
				item.getUuId(), item.getCheckItemName());
		Map<String, Object> mq_map = new HashMap<String, Object>();
		mq_map.put("app", "dhss");
		mq_map.put("cacheTime", 5);
		mq_map.put("maxConnNum", item.getUnitType().equals("SGW") || item.getUnitType().equals("SOAP_GW") ? 8  : ProjectProperties.getMaxNum());
		mq_map.put("type",item.getNeType());
		mq_map.put("srcQ", SmartCheckTaskService.TASKNAEM);
        mq_map.put("destQ", ProjectProperties.getDesQName());
        mq_map.put("sessionid", item.getUuId());
        mq_map.put("ne",  item.getUnitName());
        mq_map.put("neConnType",  "DHSS_"+item.getProtocol());
        mq_map.put("password",  Encodes.encodeHex(item.getLoginPwd() != null ? item.getLoginPwd().getBytes() : "".getBytes()));
        mq_map.put("port",  item.getPort());
        mq_map.put("priority",  5);
        mq_map.put("procotol",  item.getProtocol());
        mq_map.put("username",  unames[0]);
        mq_map.put("ip",  item.getIp());
        mq_map.put("content",new Context(item.getProtocol() + "_DHLR_COMMAND|"+unames[1]+","+Encodes.encodeHex(item.getRootPwd() != null ? item.getRootPwd().getBytes() : "".getBytes())+":"+item.getCommand(),2,1));
        mq_map.put("hostname", item.getHostname());
        mq_map.put("netFlag", item.getNetFlag());
        mq_map.put("vendor", "nokia");
        mq_map.put("flag", "");
        mq_map.put("cacheTime", 5);
        mq_map.put("retryInterval", 3);
        mq_map.put("retryTimes", 2);
        mq_map.put("needJump", 0);
        mq_map.put("jumpCount", 0);
        mq_map.put("callInterfaceName", "");
        mq_map.put("msg", "");
        mq_map.put("src", "");
        mq_map.put("exculde", 0);
        mq_map.put("taskNum", 71001);
        mq_map.put("unitType", item.getUnitType());
        
//        mq_map.put("commandName",commandName);//checkItem.getCommand());
//        mq_map.put("exeTimeoutMinutes", 30);
		
		 if (EquipmentNeType.SGW.equals(item.getNeType()) || EquipmentUnitType.SGW.equals(item.getUnitType())) {                       
             mq_map.put("msgCode", 71000);
         }else {
             mq_map.put("msgCode", 71000);
         }
		try {
			messageProducer.sendMessage(mq_map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

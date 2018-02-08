package com.nokia.ices.apps.notifier.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.nokia.ices.apps.notifier.domain.NotificationMessageRecord;
import com.nokia.ices.apps.notifier.service.NotificationService;
import com.nokia.ices.apps.notifier.CustomSettings;

@Service
public class NotificationSMSServiceImpl implements NotificationService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	@Qualifier("jdbcTemplateSMS")
	private JdbcTemplate jdbcTemplateSMS;
	private static final Logger logger = LoggerFactory.getLogger(NotificationSMSServiceImpl.class);

	@Override
	public void testConn() {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select now()");
		logger.info("The localhost time is : " + list.toString());
		List<Map<String, Object>> list2 = jdbcTemplateSMS.queryForList("select now()");
		logger.info("The sqlServer time is : " + list2.toString());
	}

	@Override
	public void sendMSG(NotificationMessageRecord message) {

		logger.info("============Start to send the message ==============");
		String phoneNubmer = message.getMobile();
		String smsContent = message.getSmscontent();
		String smsPort = CustomSettings.getSmsport();
		String insertSQL = CustomSettings.getInsertToSmsSenderTable();//"insert into CmMETONEMTINFO (mobile,smscontent,smsport) values (?,?,?)";
		String insertSqlRecord = CustomSettings.getInsertToRecordTable();//"insert into notification_message_record (mobile,smscontent,smsport,send_date) values (?,?,?,?)";
		
		
		jdbcTemplateSMS.update(insertSQL, phoneNubmer, smsContent, smsPort);
		jdbcTemplate.update(insertSqlRecord, phoneNubmer, smsContent, smsPort,new Date());// 历史记录
		
		logger.info("============Sending message ends==============");

	}

}

package com.nokia.ices.apps.fusion.task;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.nokia.ices.apps.fusion.CustomSettings;
import com.nokia.ices.apps.fusion.alarm.AlarmMonitor;
import com.nokia.ices.apps.fusion.alarm.repository.AlarmMonitorRepository;

public class MySAXHandler extends DefaultHandler {
	private Stack<String> stack = new Stack<String>();
	private Map<String,Object> xmlDataMap;
	private AlarmMonitorRepository alarmMonitorRepository;
	public MySAXHandler(JdbcTemplate jdbcTemplate,AlarmMonitorRepository alarmMonitorRepository) {
		super();
		this.alarmMonitorRepository = alarmMonitorRepository;
	}
	@Override
	public void startDocument() throws SAXException {
		System.out.println("start document -> parse begin");
		xmlDataMap = new HashMap<>();
	}

	@Override
	public void endDocument() throws SAXException {
		System.out.println("end document -> parse finished");
	}
	public Map<String,Object> getSingleXmlDataRecord(){
		return this.xmlDataMap;
	}
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {
		stack.push(qName);
		if("PMSetup".equalsIgnoreCase(qName)){
			for (int i = 0; i < attributes.getLength(); ++i) {
				String attrName = attributes.getQName(i);
				String attrValue = attributes.getValue(i);
				System.out.println("Attribute: " + attrName + "=" + attrValue);
				xmlDataMap.put(attrName, attrValue);
			}
		}
	}
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String tag = stack.peek();
		String value = new String(ch, start, length);
		if ("SubsHSS2BOSS".equals(tag)) {
			xmlDataMap.put("SubsHSS2BOSS", value);
		} else if ("RatesHSS2BOSS".equals(tag)) {
			xmlDataMap.put("RatesHSS2BOSS", value);
			xmlDataMap.put("RatesHSS2BOSS_NUM", StringUtils.replace(value, "%", ""));
		}else if ("SubsBOSS2HSS_T".equals(tag)) {
			xmlDataMap.put("SubsBOSS2HSS_T", value);
		}else if ("RatesBOSS2HSS_T".equals(tag)) {
			xmlDataMap.put("RatesBOSS2HSS_T", value);
			xmlDataMap.put("RatesBOSS2HSS_T_NUM", StringUtils.replace(value, "%", ""));
		}else if ("SubsBOSS2HSS_O".equals(tag)) {
			xmlDataMap.put("SubsBOSS2HSS_O", value);
		}else if ("RatesBOSS2HSS_O".equals(tag)) {
			xmlDataMap.put("RatesBOSS2HSS_O", value);
			xmlDataMap.put("RatesBOSS2HSS_O_NUM", StringUtils.replace(value, "%", ""));
		}else if ("ADsBOSSHSS".equals(tag)) {
			xmlDataMap.put("ADsBOSSHSS", value);
		}else if ("DN".equalsIgnoreCase(tag)){
			xmlDataMap.put("DN", value);
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		stack.pop();// 表示该元素解析完毕，需要从栈中弹出标签
		if("PMSetup".equals(qName)){
		    String DN = xmlDataMap.get("DN").toString();
		    // 正则表达式规则
		    String regEx = "HSS[0-9]+";
		    // 编译正则表达式
		    Pattern pattern = Pattern.compile(regEx);
		    // 忽略大小写的写法
		    // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		    Matcher matcher = pattern.matcher(DN);
		    // 查找字符串中是否有匹配正则表达式的字符/字符串
		    boolean rs = matcher.find();
		    if(rs){
		    	xmlDataMap.put("dhss", matcher.group());
		    }
			Integer SubsHSS2BOSS = Integer.parseInt(xmlDataMap.get("SubsHSS2BOSS").toString());
			String strCount = CustomSettings.getSubsHSS2BOSSCOUNT();
			Integer SubsHSS2BOSSCOUNT = Integer.parseInt(strCount == null || "".equals(strCount) ? "0" : strCount);
			if(SubsHSS2BOSS > SubsHSS2BOSSCOUNT){
				AlarmMonitor alarm = new AlarmMonitor();
				alarm.setAlarmContent("SOAP通知BOSS的消息提醒用户数超过" + SubsHSS2BOSSCOUNT + "个！");
				alarm.setAlarmTitle("SOAPGW告警");
				alarm.setAlarmType("SOAPGW");
				String alarmTimeStr = xmlDataMap.get("realStopTime") == null ? "" : xmlDataMap.get("realStopTime").toString();
				alarmTimeStr = alarmTimeStr.replace("+08:00:00","");
				alarmTimeStr = alarmTimeStr.replace("T", " ");
				alarm.setStartTime(alarmTimeStr);
				alarm.setBelongSite(xmlDataMap.get("dhss") == null ? "" : xmlDataMap.get("dhss").toString());
				alarm.setNeName(xmlDataMap.get("DN") == null ? "" : xmlDataMap.get("DN").toString());
				alarmMonitorRepository.save(alarm);
			}
		}

	}
}
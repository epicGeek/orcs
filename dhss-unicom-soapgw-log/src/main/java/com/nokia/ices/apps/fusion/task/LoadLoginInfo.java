package com.nokia.ices.apps.fusion.task;

import com.nokia.ices.apps.fusion.CustomSettings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class LoadLoginInfo implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(LoadLoginInfo.class);
	private static List<Map<String, String>> addressInfoList = new ArrayList<>();
	@Autowired
	CustomSettings customSettings;
	public void run(String... args) throws Exception {
		logger.info("Start to load ip info.");
		String[] ipGroup = customSettings.getSoapGwAddr().split("\\,");
		for (String nameAndIp : ipGroup) {
			Map<String,String> nameAndIpMap = new HashMap<String,String>();
			nameAndIpMap.put("addr", nameAndIp.split("-")[1]);
			nameAndIpMap.put("name", nameAndIp.split("-")[0]);
			addressInfoList.add(nameAndIpMap);
		}
		logger.info("Login info map:");
		
	}
	public static List<Map<String, String>> getAddressInfoList() {
		return addressInfoList;
	}
	public static void setAddressInfoList(List<Map<String, String>> addressInfoList) {
		LoadLoginInfo.addressInfoList = addressInfoList;
	}
	



}
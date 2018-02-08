package com.nokia.ices.apps.notifier;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.sms")
public class CustomSettings {
	private static String smsport ;
	private static String insertToRecordTable;
	private static String insertToSmsSenderTable;
	public static String getInsertToRecordTable() {
		return insertToRecordTable;
	}
	public static void setInsertToRecordTable(String insertToRecordTable) {
		CustomSettings.insertToRecordTable = insertToRecordTable;
	}
	public static String getInsertToSmsSenderTable() {
		return insertToSmsSenderTable;
	}
	public static void setInsertToSmsSenderTable(String insertToSmsSenderTable) {
		CustomSettings.insertToSmsSenderTable = insertToSmsSenderTable;
	}
	
	public static String getSmsport() {
		return smsport;
	}
	public static void setSmsport(String smsport) {
		CustomSettings.smsport = smsport;
	}
	
}

package com.nokia.ices.apps.fusion;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.report")
public class CustomSettings implements BeanClassLoaderAware {
	private static String insertQuotaHistoryTable;
	private static String insertQuotaCurrentTable;
	private static String addCustomAlarm;
	private static Integer holdKpiDataMonth;
	

	public static Integer getHoldKpiDataMonth() {
		return holdKpiDataMonth;
	}

	public static void setHoldKpiDataMonth(Integer holdKpiDataMonth) {
		CustomSettings.holdKpiDataMonth = holdKpiDataMonth;
	}

	public static String getAddCustomAlarm() {
		return addCustomAlarm;
	}

	public static void setAddCustomAlarm(String addCustomAlarm) {
		CustomSettings.addCustomAlarm = addCustomAlarm;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public static String getInsertQuotaHistoryTable() {
		return insertQuotaHistoryTable;
	}

	public static void setInsertQuotaHistoryTable(String insertQuotaHistoryTable) {
		CustomSettings.insertQuotaHistoryTable = insertQuotaHistoryTable;
	}

	public static String getInsertQuotaCurrentTable() {
		return insertQuotaCurrentTable;
	}

	public static void setInsertQuotaCurrentTable(String insertQuotaCurrentTable) {
		CustomSettings.insertQuotaCurrentTable = insertQuotaCurrentTable;
	}

	public static String getInsertQuotaTable() {
		return insertQuotaHistoryTable;
	}

	public static void setInsertQuotaTable(String insertQuotaTable) {
		CustomSettings.insertQuotaHistoryTable = insertQuotaTable;
	}

	private ClassLoader classLoader;

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;

	}

}

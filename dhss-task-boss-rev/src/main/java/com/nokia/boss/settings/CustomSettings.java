package com.nokia.boss.settings;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "dhss.boss")
public class CustomSettings implements BeanClassLoaderAware {

	private ClassLoader classLoader;
	private static String rsyncCmd;
	private static Integer saveDays;
	private static String loadFileDir;
	private static String soapGwInfo;
	private static String bossVersion;
	private static String rsyncDataDir;
	private static String cacheDataDir;
	private static String ruleFileAbsPath;
	public ClassLoader getClassLoader() {
		return classLoader;
	}
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	public static String getRsyncCmd() {
		return rsyncCmd;
	}
	public static void setRsyncCmd(String rsyncCmd) {
		CustomSettings.rsyncCmd = rsyncCmd;
	}
	public static Integer getSaveDays() {
		return saveDays;
	}
	public static void setSaveDays(Integer saveDays) {
		CustomSettings.saveDays = saveDays;
	}
	public static String getLoadFileDir() {
		return loadFileDir;
	}
	public static void setLoadFileDir(String loadFileDir) {
		CustomSettings.loadFileDir = loadFileDir;
	}
	public static String getSoapGwInfo() {
		return soapGwInfo;
	}
	public static void setSoapGwInfo(String soapGwInfo) {
		CustomSettings.soapGwInfo = soapGwInfo;
	}
	public static String getBossVersion() {
		return bossVersion;
	}
	public static void setBossVersion(String bossVersion) {
		CustomSettings.bossVersion = bossVersion;
	}
	public static String getRsyncDataDir() {
		return rsyncDataDir;
	}
	public static void setRsyncDataDir(String rsyncDataDir) {
		CustomSettings.rsyncDataDir = rsyncDataDir;
	}
	public static String getCacheDataDir() {
		return cacheDataDir;
	}
	public static void setCacheDataDir(String cacheDataDir) {
		CustomSettings.cacheDataDir = cacheDataDir;
	}
	public static String getRuleFileAbsPath() {
		return ruleFileAbsPath;
	}
	public static void setRuleFileAbsPath(String ruleFileAbsPath) {
		CustomSettings.ruleFileAbsPath = ruleFileAbsPath;
	}
	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
}

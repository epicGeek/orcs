package com.nokia.ices.apps.fusion.patrol.config;


import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "spring.dhss.smart")
public class SmartProjectProperties implements BeanClassLoaderAware {

	private static String smartJobQueue;
	private static String messageCode;
	private static String srcQueue;
	private static String appQueue;
	private static String taskName;
	private static String jarName;
    public static final String smarTaskName = "SMART-JOB-UPDATE";



	public static String getMessageCode() {
		return messageCode;
	}


	public static void setMessageCode(String messageCode) {
		SmartProjectProperties.messageCode = messageCode;
	}


	public static String getSrcQueue() {
		return srcQueue;
	}


	public static void setSrcQueue(String srcQueue) {
		SmartProjectProperties.srcQueue = srcQueue;
	}


	public static String getAppQueue() {
		return appQueue;
	}


	public static void setAppQueue(String appQueue) {
		SmartProjectProperties.appQueue = appQueue;
	}


	public static String getTaskName() {
		return taskName;
	}


	public static void setTaskName(String taskName) {
		SmartProjectProperties.taskName = taskName;
	}


	public static String getJarName() {
		return jarName;
	}


	public static void setJarName(String jarName) {
		SmartProjectProperties.jarName = jarName;
	}


	private ClassLoader classLoader; 
	
	
	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}


	public ClassLoader getClassLoader() {
		return classLoader;
	}


	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}


	public static String getSmartJobQueue() {
		return smartJobQueue;
	}


	public static void setSmartJobQueue(String smartJobQueue) {
		SmartProjectProperties.smartJobQueue = smartJobQueue;
	}



}

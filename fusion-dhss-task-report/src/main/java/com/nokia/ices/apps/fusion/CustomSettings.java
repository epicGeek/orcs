package com.nokia.ices.apps.fusion;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.report")
public class CustomSettings implements BeanClassLoaderAware {

	private static String templatePath;
	private static String outPutFilePath;
	private static Long id;
	
	public static Long getId() {
		return id;
	}

	public static void setId(Long id) {
		CustomSettings.id = id;
	}

	public static String getTemplatePath() {
		return templatePath;
	}

	public static void setTemplatePath(String templatePath) {
		CustomSettings.templatePath = templatePath;
	}

	public static String getOutPutFilePath() {
		return outPutFilePath;
	}

	public static void setOutPutFilePath(String outPutFilePath) {
		CustomSettings.outPutFilePath = outPutFilePath;
	}

	private ClassLoader classLoader;

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;

	}

}

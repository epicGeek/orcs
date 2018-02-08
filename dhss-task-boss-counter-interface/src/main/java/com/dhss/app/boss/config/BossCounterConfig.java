package com.dhss.app.boss.config;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.boss.counter")
public class BossCounterConfig implements BeanClassLoaderAware{
	private Integer intervalMin;
	private String bossVersion;
	private String jsonFileOutPutDir;
	private String rsyncDataDir;
	private String counterWorkSpaceDir;
	
	public String getCounterWorkSpaceDir() {
		return counterWorkSpaceDir;
	}

	public void setCounterWorkSpaceDir(String counterWorkSpaceDir) {
		this.counterWorkSpaceDir = counterWorkSpaceDir;
	}

	public String getRsyncDataDir() {
		return rsyncDataDir;
	}

	public void setRsyncDataDir(String rsyncDataDir) {
		this.rsyncDataDir = rsyncDataDir;
	}

	public String getJsonFileOutPutDir() {
		return jsonFileOutPutDir;
	}

	public void setJsonFileOutPutDir(String jsonFileOutPutDir) {
		this.jsonFileOutPutDir = jsonFileOutPutDir;
	}

	public String getBossVersion() {
		return bossVersion;
	}

	public void setBossVersion(String bossVersion) {
		this.bossVersion = bossVersion;
	}

	public Integer getIntervalMin() {
		return intervalMin;
	}

	public void setIntervalMin(Integer intervalMin) {
		this.intervalMin = intervalMin;
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

}

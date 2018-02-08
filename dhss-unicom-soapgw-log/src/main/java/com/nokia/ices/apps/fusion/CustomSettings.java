package com.nokia.ices.apps.fusion;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "dhss.unicom")
public class CustomSettings implements BeanClassLoaderAware {
	private String soapGwAddr;
	private String startCron = "0 0 10 * * ?"; //默认早上10点同步
	private String userName;
	private String password;
	private String remoteDir;
	private String localDir;
	private Boolean dryRunMode = false;
	
	public Boolean getDryRunMode() {
		return dryRunMode;
	}

	public void setDryRunMode(Boolean dryRunMode) {
		this.dryRunMode = dryRunMode;
	}

	public String getRemoteDir() {
		return remoteDir;
	}

	public void setRemoteDir(String remoteDir) {
		this.remoteDir = remoteDir;
	}

	public String getLocalDir() {
		return localDir;
	}

	public void setLocalDir(String localDir) {
		this.localDir = localDir;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStartCron() {
		return startCron;
	}

	public void setStartCron(String startCron) {
		this.startCron = startCron;
	}

	public String getSoapGwAddr() {
		return soapGwAddr;
	}

	public void setSoapGwAddr(String soapGwAddr) {
		this.soapGwAddr = soapGwAddr;
	}

	public void setBeanClassLoader(ClassLoader classLoader) {
	}
}
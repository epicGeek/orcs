package com.nokia.pgw.settings;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dhss.pgw-log")
public class CustomSetting implements BeanClassLoaderAware {

	private String pgwBasicInfo;
	private String rsyncCmdPattern;
	private boolean isDryRunMode;
	private boolean isAccurateSyncMode;
	private String accurateMatchRuleFileDir;
	private String pgwLogDeployDir;
	private String remotePgwLogBaseDir;
	private Integer saveDays;
	private String mainProgramCron;
	
	public String getMainProgramCron() {
		return mainProgramCron;
	}

	public void setMainProgramCron(String mainProgramCron) {
		this.mainProgramCron = mainProgramCron;
	}

	public Integer getSaveDays() {
		return saveDays;
	}

	public void setSaveDays(Integer saveDays) {
		this.saveDays = saveDays;
	}

	public String getPgwBasicInfo() {
		return pgwBasicInfo;
	}

	public void setPgwBasicInfo(String pgwBasicInfo) {
		this.pgwBasicInfo = pgwBasicInfo;
	}

	public String getRsyncCmdPattern() {
		return rsyncCmdPattern;
	}

	public void setRsyncCmdPattern(String rsyncCmdPattern) {
		this.rsyncCmdPattern = rsyncCmdPattern;
	}

	public boolean isDryRunMode() {
		return isDryRunMode;
	}

	public void setDryRunMode(boolean isDryRunMode) {
		this.isDryRunMode = isDryRunMode;
	}

	public boolean isAccurateSyncMode() {
		return isAccurateSyncMode;
	}

	public void setAccurateSyncMode(boolean isAccurateSyncMode) {
		this.isAccurateSyncMode = isAccurateSyncMode;
	}

	public String getAccurateMatchRuleFileDir() {
		return accurateMatchRuleFileDir;
	}

	public void setAccurateMatchRuleFileDir(String accurateMatchRuleFileDir) {
		this.accurateMatchRuleFileDir = accurateMatchRuleFileDir;
	}

	public String getPgwLogDeployDir() {
		return pgwLogDeployDir;
	}

	public void setPgwLogDeployDir(String pgwLogDeployDir) {
		this.pgwLogDeployDir = pgwLogDeployDir;
	}

	public String getRemotePgwLogBaseDir() {
		return remotePgwLogBaseDir;
	}

	public void setRemotePgwLogBaseDir(String remotePgwLogBaseDir) {
		this.remotePgwLogBaseDir = remotePgwLogBaseDir;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	private ClassLoader classLoader;

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

}

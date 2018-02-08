package com.nokia.ices.apps.fusion.config;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "spring.dhss")
public class ProjectProperties implements BeanClassLoaderAware {

    private static String COMP_BASE_PATH;
    private static String logBasePath;
    private static String projectName;
    private static String desQName;
    private static String cmdType;
    
    private static String emsSendMoblieList;
    
    public static String getEmsSendMoblieList() {
		return emsSendMoblieList;
	}
	public static void setEmsSendMoblieList(String emsSendMoblieList) {
		ProjectProperties.emsSendMoblieList = emsSendMoblieList;
	}

	private static String volteTablePath;
    
    public static String getVolteTablePath() {
		return volteTablePath;
	}
	public static void setVolteTablePath(String volteTablePath) {
		ProjectProperties.volteTablePath = volteTablePath;
	}

	private static String publicLicensePath;
    private static String privateLicensePath;
    
	public static String getPublicLicensePath() {
		return publicLicensePath;
	}
	public static void setPublicLicensePath(String publicLicensePath) {
		ProjectProperties.publicLicensePath = publicLicensePath;
	}
	public static String getPrivateLicensePath() {
		return privateLicensePath;
	}
	public static void setPrivateLicensePath(String privateLicensePath) {
		ProjectProperties.privateLicensePath = privateLicensePath;
	}

	private static String soap_port;
    private static String soap_ip;
    private static String subtool_export;
    private static String ifDynamicGetIp;
    private static String parentNeName;
    private static String oneKeyCpLocation;
    public static String getOneKeyCpLocation() {
		return oneKeyCpLocation;
	}
	public static void setOneKeyCpLocation(String oneKeyCpLocation) {
		ProjectProperties.oneKeyCpLocation = oneKeyCpLocation;
	}

	private static String otherhost;
    private static String localhost;
    
    private static String oneKeyLocation;
    private static String oneKeyUser;
    private static String downloadFolder;
    
    private static String onekeypath;
   
	public static String getOnekeypath() {
		return onekeypath;
	}
	public static void setOnekeypath(String onekeypath) {
		ProjectProperties.onekeypath = onekeypath;
	}
	public static String getDownloadFolder() {
		return downloadFolder;
	}
	public static void setDownloadFolder(String downloadFolder) {
		ProjectProperties.downloadFolder = downloadFolder;
	}
	public static String getOneKeyLocation() {
		return oneKeyLocation;
	}
	public static void setOneKeyLocation(String oneKeyLocation) {
		ProjectProperties.oneKeyLocation = oneKeyLocation;
	}
	public static String getOneKeyUser() {
		return oneKeyUser;
	}
	public static void setOneKeyUser(String oneKeyUser) {
		ProjectProperties.oneKeyUser = oneKeyUser;
	}
	public static String getOneKeyPwd() {
		return oneKeyPwd;
	}
	public static void setOneKeyPwd(String oneKeyPwd) {
		ProjectProperties.oneKeyPwd = oneKeyPwd;
	}

	private static String oneKeyPwd;
	
	private static String maxNum;
    
    public static String getMaxNum() {
		return maxNum;
	}
	public static void setMaxNum(String maxNum) {
		ProjectProperties.maxNum = maxNum;
	}

	public static final String ccSrcQName="DHLR_BACK";
    
    public static final String TASK_QName="DHLR_TASK";
    
    
    private static String userRuleParse;
    private static String number;
    private static String bossDataPath;
    private static String queueDestination;
    private static String emsName;
    private static String emsTaskTime;
    
    private static String sendErrorCode;
    
    public static List<String> getSendErrorCode() {
    	List<String> resultCodeList = new ArrayList<>();
    	String [] arr = sendErrorCode.split(",");
    	for (String string : arr) {
    		resultCodeList.add(string.trim());
		}
		return resultCodeList;
	}
	public static void setSendErrorCode(String sendErrorCode) {
		ProjectProperties.sendErrorCode = sendErrorCode;
	}
	public static String getEmsTaskTime() {
		return emsTaskTime;
	}
	public static void setEmsTaskTime(String emsTaskTime) {
		ProjectProperties.emsTaskTime = emsTaskTime;
	}

	private static String mmlIp;
    private static String mmlPort;
    private static String mmlUserName;
    private static String mmlUserPwd;
    private static String mmlProtocol;
    
	public static String getCmdType() {
		return cmdType;
	}
	public static void setCmdType(String cmdType) {
		ProjectProperties.cmdType = cmdType;
	}
	public static String getMmlIp() {
		return mmlIp;
	}
	public static void setMmlIp(String mmlIp) {
		ProjectProperties.mmlIp = mmlIp;
	}
	public static String getMmlPort() {
		return mmlPort;
	}
	public static void setMmlPort(String mmlPort) {
		ProjectProperties.mmlPort = mmlPort;
	}
	public static String getMmlUserName() {
		return mmlUserName;
	}
	public static void setMmlUserName(String mmlUserName) {
		ProjectProperties.mmlUserName = mmlUserName;
	}
	public static String getMmlUserPwd() {
		return mmlUserPwd;
	}
	public static void setMmlUserPwd(String mmlUserPwd) {
		ProjectProperties.mmlUserPwd = mmlUserPwd;
	}
	public static String getMmlProtocol() {
		return mmlProtocol;
	}
	public static void setMmlProtocol(String mmlProtocol) {
		ProjectProperties.mmlProtocol = mmlProtocol;
	}
	public static String getEmsName() {
		return emsName;
	}
	public static void setEmsName(String emsName) {
		ProjectProperties.emsName = emsName;
	}
	public static String getOtherhost() {
		return otherhost;
	}
	public static void setOtherhost(String otherhost) {
		ProjectProperties.otherhost = otherhost;
	}
	public static String getLocalhost() {
		return localhost;
	}
	public static void setLocalhost(String localhost) {
		ProjectProperties.localhost = localhost;
	}		

    public static String getQueueDestination() {
		return queueDestination;
	}
	public static void setQueueDestination(String queueDestination) {
		ProjectProperties.queueDestination = queueDestination;
	}
	public static String getBossDataPath() {
        return bossDataPath;
    }
    public static void setBossDataPath(String bossDataPath) {
        ProjectProperties.bossDataPath = bossDataPath;
    }
    public static String getNumber() {
		return number;
	}
	public static void setNumber(String number) {
		ProjectProperties.number = number;
	}

	public static String getUserRuleParse() {
		return userRuleParse;
	}
	public static void setUserRuleParse(String userRuleParse) {
		ProjectProperties.userRuleParse = userRuleParse;
	}

	private static String CONSOLE_LOG_PATH;;

    private ClassLoader classLoader;

    public static String getIfDynamicGetIp() {
		return ifDynamicGetIp;
	}



	public static void setIfDynamicGetIp(String ifDynamicGetIp) {
		ProjectProperties.ifDynamicGetIp = ifDynamicGetIp;
	}

	public static String getParentNeName() {
		return parentNeName;
	}
	public static void setParentNeName(String parentNeName) {
		ProjectProperties.parentNeName = parentNeName;
	}
	public static String getSoap_port() {
		return soap_port;
	}



	public static void setSoap_port(String soap_port) {
		ProjectProperties.soap_port = soap_port;
	}



	public static String getSoap_ip() {
		return soap_ip;
	}



	public static void setSoap_ip(String soap_ip) {
		ProjectProperties.soap_ip = soap_ip;
	}

	public static String getSubtool_export() {
		return subtool_export;
	}



	public static void setSubtool_export(String subtool_export) {
		ProjectProperties.subtool_export = subtool_export;
	}



	public static String getCOMP_BASE_PATH() {
        return COMP_BASE_PATH;
    }

	public static void setCOMP_BASE_PATH(String cOMP_BASE_PATH) {
        COMP_BASE_PATH = cOMP_BASE_PATH;
    }


    public static String getLogBasePath() {
        return logBasePath;
    }
    public static void setLogBasePath(String logBasePath) {
        ProjectProperties.logBasePath = logBasePath;
    }
    public static String getProjectName() {
        return projectName;
    }

    public static void setProjectName(String projectName) {
        ProjectProperties.projectName = projectName;
    }

    public static String getDesQName() {
        return desQName;
    }

    public static void setDesQName(String desQName) {
        ProjectProperties.desQName = desQName;
    }


    public static String getCONSOLE_LOG_PATH() {
        return CONSOLE_LOG_PATH;
    }


    public static void setCONSOLE_LOG_PATH(String cONSOLE_LOG_PATH) {
        CONSOLE_LOG_PATH = cONSOLE_LOG_PATH;
    }


    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }


    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;

    }


}

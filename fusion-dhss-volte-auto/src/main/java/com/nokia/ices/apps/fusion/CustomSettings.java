package com.nokia.ices.apps.fusion;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.volte")
public class CustomSettings implements BeanClassLoaderAware {
	
	private static String dataFileDir;
	private static String loadSql;
	private static String insertCounterDataSql ;
	private static String loadSqlHss2Boss;
	private static String SubsHSS2BOSSCOUNT;
	
	
	public static String getSubsHSS2BOSSCOUNT() {
		return SubsHSS2BOSSCOUNT;
	}


	public static void setSubsHSS2BOSSCOUNT(String subsHSS2BOSSCOUNT) {
		SubsHSS2BOSSCOUNT = subsHSS2BOSSCOUNT;
	}


	public static String getLoadSqlHss2Boss() {
		return loadSqlHss2Boss;
	}


	public static void setLoadSqlHss2Boss(String loadSqlHss2Boss) {
		CustomSettings.loadSqlHss2Boss = loadSqlHss2Boss;
	}


	public static String getInsertCounterDataSql() {
		return insertCounterDataSql;
	}


	public static void setInsertCounterDataSql(String insertCounterDataSql) {
		CustomSettings.insertCounterDataSql = insertCounterDataSql;
	}


	public static String getLoadSql() {
		return loadSql;
	}


	public static void setLoadSql(String loadSql) {
		CustomSettings.loadSql = loadSql;
	}


	public static String getDataFileDir() {
		return dataFileDir;
	}


	public static void setDataFileDir(String dataFileDir) {
		CustomSettings.dataFileDir = dataFileDir;
	}


	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
	}
	
}

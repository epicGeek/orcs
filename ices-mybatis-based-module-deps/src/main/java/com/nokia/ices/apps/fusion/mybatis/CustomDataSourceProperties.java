package com.nokia.ices.apps.fusion.mybatis;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration(value="dbConfig")
@ConfigurationProperties(locations = "classpath:mybatis.properties",prefix ="spring.mybatis.datasource")
public class CustomDataSourceProperties implements BeanClassLoaderAware{
	
	private String url;
	private String username;
	private String password;
	private String iceUrl;
	private String iceUsername;
	private String icePassword;
	private String driverClassName;
	private Integer initialSize;
	private Integer maxActive;
	private Integer maxWait;
	private Integer minIdle;
	private Long validationInterval;
	private String validationQuery;
	private String testOnBorrow;
	private String testWhileIdle;
	private String testOnReturn;
	private Integer timeBetweenEvictionRunsMillis;
	private Integer minEvictableIdleTimeMillis;
	private ClassLoader classLoader;
	
	
	public String getIceUrl() {
		return iceUrl;
	}
	public void setIceUrl(String iceUrl) {
		this.iceUrl = iceUrl;
	}
	public String getIceUsername() {
		return iceUsername;
	}
	public void setIceUsername(String iceUsername) {
		this.iceUsername = iceUsername;
	}
	public String getIcePassword() {
		return icePassword;
	}
	public void setIcePassword(String icePassword) {
		this.icePassword = icePassword;
	}
	public Integer getMaxWait() {
		return maxWait;
	}
	public void setMaxWait(Integer maxWait) {
		this.maxWait = maxWait;
	}
	public Integer getMinIdle() {
		return minIdle;
	}
	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}
	public Integer getMaxActive() {
		return maxActive;
	}
	public void setMaxActive(Integer maxActive) {
		this.maxActive = maxActive;
	}
	public Integer getInitialSize() {
		return initialSize;
	}
	public void setInitialSize(Integer initialSize) {
		this.initialSize = initialSize;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the driverClassName
	 */
	public String getDriverClassName() {
		return driverClassName;
	}
	/**
	 * @param driverClassName the driverClassName to set
	 */
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	/**
	 * @return the classLoader
	 */
	public ClassLoader getClassLoader() {
		return classLoader;
	}
	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	public Long getValidationInterval() {
        return validationInterval;
    }
    public void setValidationInterval(Long validationInterval) {
        this.validationInterval = validationInterval;
    }
    public Integer getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }
    public void setTimeBetweenEvictionRunsMillis(Integer timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }
    public Integer getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }
    public void setMinEvictableIdleTimeMillis(Integer minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }
    public String getValidationQuery() {
        return validationQuery;
    }
    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }
    public String getTestOnBorrow() {
        return testOnBorrow;
    }
    public void setTestOnBorrow(String testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }
    public String getTestWhileIdle() {
        return testWhileIdle;
    }
    public void setTestWhileIdle(String testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }
	public String getTestOnReturn() {
		return testOnReturn;
	}
	public void setTestOnReturn(String testOnReturn) {
		this.testOnReturn = testOnReturn;
	}
    
}
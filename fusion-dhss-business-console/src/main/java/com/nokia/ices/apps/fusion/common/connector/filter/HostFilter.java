package com.nokia.ices.apps.fusion.common.connector.filter;

/**
 * 主机过滤器
 * 允许或拒绝连接主机
 * 
 * @author kongdy
 *
 */
public interface HostFilter {

	public boolean isAccessible(String host);

}

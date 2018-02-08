package com.nokia.ices.apps.fusion.common.connector.filter;

/**
 * 默认的主机过滤器，默认主机可被连接
 * 
 * @author kongdy
 *
 */
public class SimpleHostFilter implements HostFilter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nsn.ices.apps.dhlr.common.connector.HostFilter#isAccessible(java.
	 * lang.String)
	 */
	@Override
	public boolean isAccessible(String host) {
		return true;
	}

}

package com.nokia.ices.apps.fusion.common.connector.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认命令过滤器，默认允许执行所有命令
 * 
 * @author kongdy
 *
 */
public class SimpleCommandFilter implements CommandFilter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nsn.ices.apps.dhlr.common.connector.filter.CommandFilter#getWhiteList()
	 */
	@Override
	public List<String> getWhiteList() {
		return new ArrayList<String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nsn.ices.apps.dhlr.common.connector.filter.CommandFilter#getBlackList()
	 */
	@Override
	public List<String> getBlackList() {
		return new ArrayList<String>();
	}

}

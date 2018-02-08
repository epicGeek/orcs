package com.nokia.ices.apps.fusion.common.connector.filter;

import java.util.List;

/**
 * 命令过滤器，允许或拒绝向主机发送命令
 * 
 * @author kongdy
 *
 */
public interface CommandFilter {

	/**
	 * 设置命令白名单，白名单中的命令将被允许在主机上执行
	 * 
	 * 若命令同时存在白名单和黑名单中，则以黑名单优先，命令将被禁止执行
	 * 若白名单为空，则所有命令均被允许执行
	 * 
	 * @return 允许执行的命令列表
	 */
	public List<String> getWhiteList();

	/**
	 * 设置命令黑名单，黑名单中的命令将被禁止在主机上执行
	 * 
	 * 若命令同时存在白名单和黑名单中，则以黑名单优先，命令将被禁止执行
	 * 
	 * @return 禁止执行的命令列表
	 */
	public List<String> getBlackList();
}

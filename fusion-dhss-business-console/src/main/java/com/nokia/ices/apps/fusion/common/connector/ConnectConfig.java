package com.nokia.ices.apps.fusion.common.connector;

import java.util.Map;

import com.nokia.ices.apps.fusion.common.connector.filter.CommandFilter;
import com.nokia.ices.apps.fusion.common.connector.filter.HostFilter;

/**
 * 登陆配置信息
 * @author kongdy
 *
 */
public class ConnectConfig {
	/**
	 * 用户名
	 */
	public String username;

	/**
	 * 密码
	 */
	public String password;

	/**
	 * 连接扩展信息
	 */
	public Map<String, String> ext;

	/**
	 * 连接空闲超时时间（秒）
	 */
	public long idleTimeoutInSec;

	/**
	 * 主机过滤器
	 */
	public HostFilter hostFilter;

	/**
	 * 命令过滤器
	 */
	public CommandFilter commandFilter;

	/**
	 * 命令提示符
	 * 例如：bash命令提示符为$或#
	 */
	public String prompt;

    public String termType;

	public static final String SSH2_ROOT_PROMPT = "> ";

	public static final String SSH2_USER_PROMPT = "$ ";

	public static final String TELNET_ROOT_PROMPT = "> ";

	public static final String TELNET_USER_PROMPT = "$ ";

	public static final String TELNET_LOGIN_PROMPT = "login:";

	public static final String TELNET_PASSWORD_PROMPT = "Password:";
}

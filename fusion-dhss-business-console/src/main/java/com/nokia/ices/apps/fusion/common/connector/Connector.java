package com.nokia.ices.apps.fusion.common.connector;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.nokia.ices.apps.fusion.common.connector.filter.CommandFilter;
import com.nokia.ices.apps.fusion.common.connector.filter.HostFilter;
import com.nokia.ices.apps.fusion.common.connector.filter.SimpleCommandFilter;
import com.nokia.ices.apps.fusion.common.connector.filter.SimpleHostFilter;

/**
 * 多协议连接器
 * 支持相应协议的连接器继承该类，适用于长连接
 * 
 * @author kongdy
 *
 */
public abstract class Connector {

	/**
	 * 主机过滤器
	 */
	private HostFilter hostFilter = new SimpleHostFilter();

	/**
	 * 命令过滤器
	 */
	private CommandFilter commandFilter = new SimpleCommandFilter();

	/**
	 * 连接空闲超时时间
	 * 若在超时时间内无命令发送，连接器自动断开，默认是5分钟
	 */
	private long idleTimeout = 1000 * 60 * 5; // 5 mins

	/**
	 * 命令提示符
	 */
	private String prompt;

	public Connector setHostFilter(HostFilter hostFilter) {
		this.hostFilter = hostFilter;
		return this;
	}

	public Connector setCommandFilter(CommandFilter commandFilter) {
		this.commandFilter = commandFilter;
		return this;
	}

	public HostFilter getHostFilter() {
		return hostFilter;
	}

	public CommandFilter getCommandFilter() {
		return commandFilter;
	}

	public long getIdleTimeout() {
		return idleTimeout;
	}

	public void setIdleTimeout(long seconds) {
		this.idleTimeout = 1000 * seconds;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	/**
	 * 登陆
	 * @param hostname 主机名称
	 * @param port 主机端口
	 * @param username 用户
	 * @param password 密码
	 * @param ext 扩展信息
	 * @return 登陆提示信息（连接器成功连接主机后需要提醒用户的信息）
	 * @throws Exception 登陆错误信息
	 */
	public abstract String login(String hostname, int port, String username, String password, Map<String, String> ext)
			throws Exception;

	/**
	 * 发送命令
	 * @param command 命令
	 * @throws Exception
	 */
	public abstract void write(Object command) throws Exception;

	/**
	 * 获取结果
	 * 
	 * @return 结果输入流对象
	 */
	public abstract InputStream readOut();

	/**
	 * 获取错误
	 * 
	 * @return 错误输入流对象
	 */
	public abstract InputStream readErr();

	/**
	 * 退出
	 * @return 退出提示信息（连接器成功与主机断开连接后需要提醒用户的信息）
	 * @throws Exception 退出错误信息
	 */
	public abstract String logout() throws IOException;

    public abstract void write(byte[] commandBytes, boolean flush) throws IOException;
}

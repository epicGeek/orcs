package com.nokia.ices.apps.fusion.common.connector;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 单元上执行命令shell对象，同步发送命令并获取回显
 * 基于实现机制，该对象区别于虚拟终端对象Terminal，若需发送命令并同时获取回显，使用该对象 
 * 
 * @author kongdy
 * 
 */
public class Shell {

	/**
	 * 连接器列表
	 * 每个host一个Connector，后续可扩展为多Connector
	 */
	private final Map<String, Connector> connectorList = new HashMap<String, Connector>();

	/**
	 * 结果缓存列表
	 */
	private final Map<String, StringBuffer> outputBuffer = new HashMap<String, StringBuffer>();

	private static Shell instance = new Shell();

	private Shell() {
	}

	public static Shell getInstance() {
		return instance;
	}

	public void destroyConnector(String host) {
		Connector connector = connectorList.get(host);
		if (null != connector) {
			try {
				connector.logout();
			} catch (Exception e) {
				// do nothing
			}
		}

		if (connectorList.containsKey(host)) {
			connectorList.remove(host);
		}

		if (outputBuffer.containsKey(host)) {
			outputBuffer.remove(host);
		}
	}

	private void login(Protocol protocol, String hostname, int port, ConnectConfig config) throws Exception {
		Connector connector = ConnectorFactory.createConnector(protocol);
		if (0 != config.idleTimeoutInSec) {
			connector.setIdleTimeout(config.idleTimeoutInSec);
		}
		if (null != config.hostFilter) {
			connector.setHostFilter(config.hostFilter);
		}
		if (null != config.commandFilter) {
			connector.setCommandFilter(config.commandFilter);
		}

		if (StringUtils.isEmpty(config.prompt)) {
			if (Protocol.SSH2 == protocol) {
				if (config.username.equals("root")) {
					connector.setPrompt(ConnectConfig.SSH2_ROOT_PROMPT);
				} else {
					connector.setPrompt(ConnectConfig.SSH2_USER_PROMPT);
				}
			} else if (Protocol.TELNET == protocol) {
				if (config.username.equals("root")) {
					connector.setPrompt(ConnectConfig.TELNET_ROOT_PROMPT);
				} else {
					connector.setPrompt(ConnectConfig.TELNET_USER_PROMPT);
				}
			} else {
			}
		}

		// TODO 进行主机过滤

		connector.login(hostname, port, config.username, config.password, config.ext);
		connectorList.put(hostname, connector);
		outputBuffer.put(hostname, new StringBuffer());
		new ReadOutThread(connector.readOut(), outputBuffer.get(hostname)).start();
		new ReadOutThread(connector.readErr(), outputBuffer.get(hostname)).start();

		if (Protocol.SSH2 == protocol) {
			// SSH2为安全连接，仅判断是否显示命令提示符即可
			this.execute(hostname, "", 60);
		} else if (Protocol.TELNET == protocol) {
			// Telnet为不安全连接，需要输入用户名和密码
			this.execute(hostname, ConnectConfig.TELNET_LOGIN_PROMPT, "", 60);
			this.execute(hostname, ConnectConfig.TELNET_PASSWORD_PROMPT, config.username, 60);
			this.execute(hostname, config.password, 60);
		} else {

		}
	}

	private void write(String host, Object command) throws Exception {
		if (connectorList.containsKey(host)) {

			// TODO 过滤命令

			connectorList.get(host).write(command + "\n");
		} else {
			throw new Exception("The connector has been destroyed.");
		}
	}

	public String execute(String host, String prompt, Object command, int timeoutInSec) throws Exception {
		// 发送命令
		if (StringUtils.isNotEmpty(String.valueOf(command))) {
			this.write(host, command);
		}

		// 若输出结果包含命令提示符，则认为命令结果输出完整
		// 500毫秒轮询一次结果是否回显结束，直到超时
		for (int i = 0; i < timeoutInSec * 2; ++i) {
			String output = this.outputBuffer.get(host).toString();
			if (output.contains(prompt)) {
				this.outputBuffer.get(host).setLength(0);
				return output;
			}

			Thread.sleep(500);
		}

		return "";
	}

	public String execute(String host, Object command, int timeoutInSec) throws Exception {
		String prompt = this.connectorList.get(host).getPrompt();
		return execute(host, prompt, command, timeoutInSec);
	}

	public String execute(Protocol protocol, String hostname, int port, ConnectConfig config, Object command,
			int timeoutInSec) throws Exception {
		if (this.connectorList.containsKey(hostname)) {
			return this.execute(hostname, command, timeoutInSec);
		} else {
			this.login(protocol, hostname, port, config);
			return this.execute(hostname, command, timeoutInSec);
		}
	}
}

package com.nokia.ices.apps.fusion.common.connector;

import com.nokia.ices.apps.fusion.common.connector.ssh2.SSH2Connector;

/**
 * 连接器创建工厂
 * 
 * @author kongdy
 *
 */
public class ConnectorFactory {

	/**
	 * 根据协议创建连接器
	 * 
	 * @param protocol
	 *            协议类型
	 * @return 连接器
	 */
	public static Connector createConnector(Protocol protocol) {
		switch (protocol) {
		case SSH2: {
			return new SSH2Connector();
		}
		default: {
			return new DefaultConnector();
		}
		}
	}

}

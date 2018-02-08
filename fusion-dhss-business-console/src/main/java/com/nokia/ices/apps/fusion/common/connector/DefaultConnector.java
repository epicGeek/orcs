package com.nokia.ices.apps.fusion.common.connector;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 默认连接器
 * 
 * @author kongdy
 *
 */
public class DefaultConnector extends Connector {

	@Override
	public String login(String hostname, int port, String username, String password, Map<String, String> ext)
			throws Exception {
		return "";
	}

	@Override
	public void write(Object command) throws IOException {
		// do nothing
	}

	@Override
	public InputStream readOut() {
		return new ByteArrayInputStream(
				"This is the default connector, you should implement your own connector.".getBytes());
	}

	public InputStream readErr() {
		return null;
	}

	@Override
	public String logout() throws IOException {
		return "";
	}

    @Override
    public void write(byte[] commandBytes, boolean flush) throws IOException {
        // TODO Auto-generated method stub
        
    }

}

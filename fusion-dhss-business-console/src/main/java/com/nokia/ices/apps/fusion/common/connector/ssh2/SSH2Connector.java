package com.nokia.ices.apps.fusion.common.connector.ssh2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.nokia.ices.apps.fusion.common.connector.Connector;
import com.nokia.ices.apps.fusion.common.connector.ProtocolExt;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.KnownHosts;
import ch.ethz.ssh2.Session;

/**
 * SSH2协议连接器
 * 
 * @author kongdy
 *
 */
public class SSH2Connector extends Connector {

	static final String KNOWN_HOST_PATH = "";
	static final String ID_DSA_PATH = "";
	static final String ID_RSA_PATH = "";

	private KnownHosts database = new KnownHosts();

	private Connection conn;

	public Session session;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nsn.ices.apps.dhlr.common.connector.Connector#login(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String login(String hostname, int port, String username, String password, Map<String, String> ext)
			throws Exception {
		String lastError = "";

		conn = new Connection(hostname, port);

		/*
		 * 
		 * CONNECT AND VERIFY SERVER HOST KEY (with callback)
		 */

		String[] hostkeyAlgos = database.getPreferredServerHostkeyAlgorithmOrder(hostname);

		if (hostkeyAlgos != null) {
			conn.setServerHostKeyAlgorithms(hostkeyAlgos);
		}

		AdvancedVerifier verifier = new AdvancedVerifier(database, KNOWN_HOST_PATH);
		try {
			conn.connect(verifier);
		} catch (IOException e) {
			throw e;
		}

		lastError = verifier.getTipMessage();

		/*
		 * 
		 * AUTHENTICATION PHASE
		 */

		boolean enableKeyboardInteractive = true;
		boolean enableDSA = true;
		boolean enableRSA = true;

		while (true) {
			if ((enableDSA || enableRSA) && conn.isAuthMethodAvailable(username, "publickey")) {
				if (enableDSA) {
					File key = new File(ID_DSA_PATH);

					if (key.exists()) {
						String dsaPrivateKeyPwd = ext.get(ProtocolExt.SSH2_DSA_PRIVATE_KEY);

						boolean res = conn.authenticateWithPublicKey(username, key, dsaPrivateKeyPwd);

						if (res == true)
							break;

						lastError = "DSA authentication failed.";
					}
					enableDSA = false; // do not try again
				}

				if (enableRSA) {
					File key = new File(ID_RSA_PATH);

					if (key.exists()) {
						String rsaPrivateKeyPwd = ext.get(ProtocolExt.SSH2_RSA_PRIVATE_KEY);

						boolean res = conn.authenticateWithPublicKey(username, key, rsaPrivateKeyPwd);

						if (res == true)
							break;

						lastError = "RSA authentication failed.";
					}
					enableRSA = false; // do not try again
				}

				continue;
			}


			if (conn.isAuthMethodAvailable(username, "password")) {
				boolean res = conn.authenticateWithPassword(username, password);

				if (res == true)
					break;
					
				lastError = "Password authentication failed."; // try again, if possible
				
				break;
			}
			
			
			if (enableKeyboardInteractive && conn.isAuthMethodAvailable(username, "keyboard-interactive")) {
				InteractiveLogic il = new InteractiveLogic(null);

				boolean res = conn.authenticateWithKeyboardInteractive(username, il);

				if (res == true)
					break;

				if (il.getPromptCount() == 0) {
					// aha. the server announced that it supports
					// "keyboard-interactive", but when
					// we asked for it, it just denied the request without
					// sending us any prompt.
					// That happens with some server versions/configurations.
					// We just disable the "keyboard-interactive" method and
					// notify the user.

					lastError = "Keyboard-interactive does not work.";

					enableKeyboardInteractive = false; // do not try this again
				} else {
					lastError = "Keyboard-interactive auth failed."; // try
																		// again,
																		// if
																		// possible
				}

				continue;
			}

			throw new IOException("No supported authentication methods available.");
		}

		/*
		 * 
		 * AUTHENTICATION OK. DO SOMETHING.
		 */

		session = conn.openSession();
		
		//默认窗口大小
        int x_width = 90;
        int y_width = 30;
		
        //默认窗口类型
        String term = "dumb";
		if(ext!=null&&ext.containsKey("x_width"))
		    x_width = Integer.parseInt(ext.get("x_width"));

        if(ext!=null&&ext.containsKey("y_width"))
            y_width = Integer.parseInt(ext.get("y_width"));

        if(ext!=null&&ext.containsKey("term"))
            term = ext.get("term");
        System.out.println(ext.toString());
        session.requestPTY(term, x_width, y_width, 0, 0, null);
		session.startShell();

		return lastError;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nsn.ices.apps.dhlr.common.connector.Connector#write(java.lang.String)
	 */
	@Override
	public void write(Object command) throws IOException{
		session.getStdin().write(String.valueOf(command).getBytes());
		session.getStdin().flush();
	}

    @Override
    public void write(byte[] commandBytes,boolean flush) throws IOException {

        session.getStdin().write(commandBytes);
        if(flush){
            try {
                session.getStdin().flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nsn.ices.apps.dhlr.common.connector.Connector#read()
	 */
	@Override
	public InputStream readOut() {
		return session.getStdout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nsn.ices.apps.dhlr.common.connector.Connector#readErr()
	 */
	@Override
	public InputStream readErr() {
		return session.getStderr();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nsn.ices.apps.dhlr.common.connector.Connector#logout()
	 */
	@Override
	public String logout() {
		conn.close();
		return "";
	}

}

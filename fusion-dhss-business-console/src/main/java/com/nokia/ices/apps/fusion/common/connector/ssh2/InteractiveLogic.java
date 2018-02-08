package com.nokia.ices.apps.fusion.common.connector.ssh2;

import java.io.IOException;

import ch.ethz.ssh2.InteractiveCallback;

/**
 * The logic that one has to implement if "keyboard-interactive" autentication shall be
 * supported.
 * 
 * @author kongdy
 *
 */
public class InteractiveLogic implements InteractiveCallback {

	int promptCount = 0;

	private String[] answers;

	public InteractiveLogic(String[] answers) {
		this.answers = answers;
		this.promptCount = null == answers ? 0 : answers.length;
	}

	/*
	 * the callback may be invoked several times, depending on how many
	 * questions-sets the server sends
	 */

	public String[] replyToChallenge(String name, String instruction, int numPrompts, String[] prompt, boolean[] echo)
			throws IOException {

		if (answers == null)
			throw new IOException("Login aborted by user");

		return answers;
	}

	/*
	 * We maintain a prompt counter - this enables the detection of situations
	 * where the ssh server is signaling "authentication failed" even though it
	 * did not send a single prompt.
	 */

	public int getPromptCount() {
		return promptCount;
	}

}

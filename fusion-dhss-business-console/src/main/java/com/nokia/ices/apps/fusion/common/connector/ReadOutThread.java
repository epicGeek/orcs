package com.nokia.ices.apps.fusion.common.connector;

import java.io.InputStream;

/**
 * BIO输入流读线程
 * @author kongdy
 *
 */
public class ReadOutThread extends Thread {
	private InputStream in;

	private StringBuffer buffer;

	private static final String ANSI_COLOR = "\\x1B\\[([0-9]{1,2}(;[0-9]{1,2})?)?[m|K]";

	public ReadOutThread(InputStream in, StringBuffer buffer) {
		this.in = in;
		this.buffer = buffer;
	}

	public void run() {
		byte[] buff = new byte[8192];

		try {
			if (null != in) {
				while (true) {
					int len = in.read(buff);
					if (len == -1)
						return;
					buffer.append(new String(buff, 0, len, "UTF-8"));
				}
			}
		} catch (Exception e) {
		}
	}
}

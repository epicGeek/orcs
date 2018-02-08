package com.nokia.ices.apps.fusion.common.connector;

import java.io.InputStream;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokia.ices.core.utils.FileOperateUtil;

/**
 * BIO输入流读线程
 * @author kongdy
 *
 */
public class ReadThread extends Thread {
	private String file;
	
	private InputStream in;

	private Queue<String> buffer;

	private static final String ANSI_COLOR = "\\x1B\\[([0-9]{1,2}(;[0-9]{1,2})?)?[m|K]";
	
	private static final Logger logger = LoggerFactory.getLogger(ReadThread.class);

	public ReadThread(String file, InputStream in, Queue<String> buffer) {
		this.file = file;
		this.in = in;
		this.buffer = buffer;
	}

	public void run() {
		byte[] buff = new byte[8192];
		String appendContent ="";
		try {
			if (null != in) {
				while (true) {
					int len = in.read(buff);
					if (len == -1){
		                logger.debug("exit Read Thread");
		                return;
					}
					appendContent = new String(buff, 0, len, "UTF-8").replaceAll(ANSI_COLOR, "");
					buffer.add(appendContent);
					//追加结果到缓存文件  add by yudq 2015-04-14.
					FileOperateUtil.saveAs(appendContent, file);
				}
				
			}
		} catch (Exception e) {
		}
	}
}

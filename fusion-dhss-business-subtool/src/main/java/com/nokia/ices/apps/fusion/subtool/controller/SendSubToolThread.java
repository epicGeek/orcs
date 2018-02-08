package com.nokia.ices.apps.fusion.subtool.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.subtool.domain.CheckSubtoolResult;
import com.nokia.ices.apps.fusion.subtool.repository.SubtoolRepository;
import com.nokia.ices.core.utils.DateUtil;
import com.nokia.ices.core.utils.ModuleDownLoadNameDefinition;

public class SendSubToolThread implements Runnable {

	private final static Logger logger = LoggerFactory.getLogger(SendSubToolThread.class);

	// 执行命令
	private String command;

	private String checkName;

	private List<String> listIp;

	private String loginName;

	private String number;

	private SubtoolRepository subtoolRepository;

	private final static String SUCCESS = "0";

	private final static String FAILURE = "1";

	/**
	 * 初始化参数值
	 * 
	 * @param command
	 * @param checkName
	 */
	public SendSubToolThread(String command, String checkName, SubtoolRepository subtoolRepository, List<String> listIp,
			String loginName, String number) {
		this.command = command;
		this.checkName = checkName;
		this.subtoolRepository = subtoolRepository;
		this.listIp = listIp;
		this.loginName = loginName;
		this.number = number;
	}

	@SuppressWarnings("unused")
	@Override
	public void run() {
		BufferedReader buf = null;
		boolean flag = false;// 多个PGW如果有一个不成功则不成功
		// 产生UUID保证文件名不重复，导致数据错写
		String session = UUID.randomUUID().toString().replaceAll("[-]", "");
		StringBuffer sbs = new StringBuffer();
		int ipSize = listIp.size();
		String result = "0";// 默认成功
		/**
		 * 执行返回内容追加
		 */
		String isStr = ">>";
		String subPath = loginName + "_" + DateUtil.getCurrentDate6() + "_" + session + ".txt";
		String exportCommand = ProjectProperties.getSubtool_export();
		String subtoolPath = ProjectProperties.getLogBasePath() + File.separator
				+ ModuleDownLoadNameDefinition.DOWNLOAD_SUBTOOL + File.separator;
		File operationDir = new File(subtoolPath);
		if (!operationDir.exists() && !operationDir.isDirectory()) {
			operationDir.mkdir();
		}
		String filePath = subtoolPath + subPath;
		// 执行命令处理
		result = execCmd(isStr, filePath);
		logger.debug("execute to end result ：" + result);

		// export 命令执行成功 开始执行 subtool命令，并返回结果入库
		writerFile(subPath, result);
	}

	/**
	 * 命令执行
	 * @param isStr
	 * @param filePath
	 * @return
	 */
	private String execCmd(String isStr, String filePath) {

		StringBuffer sbs = new StringBuffer();
		Process process = null;
		String[] cmd = null;

		/**
		 * 循环执行subtool指令获取执行结果
		 */

		for (String pgwIp : listIp) {
			if (pgwIp.contains(":")) {
				/**
				 * 针对ldapip上的指令做处理
				 */
				String export = "export PGWAdd=" + pgwIp.split(":")[0] + " LDAPAdd=" + pgwIp.split(":")[1];
				cmd = new String[] { "/bin/sh", "-c", export + ";" + command + " " + isStr + " " + filePath };
				logger.debug("command = " + export + ";" + command + " " + isStr + " " + filePath);
			} else {
				String export = "export PGWAdd=" + pgwIp;
				cmd = new String[] { "/bin/sh", "-c", export + ";" + command + " " + isStr + " " + filePath };
				logger.debug("command = " + export + ";" + command + " " + isStr + " " + filePath);
			}
			try {
				process = Runtime.getRuntime().exec(cmd);
				String resuslt = FileReader(filePath);
				int exitValue = isCommandSuccsee(process);
				if (0 != exitValue) {
					logger.debug("The command  Exec failure code is :" + exitValue);
				}
				logger.debug("fileReader is context " + resuslt);
				if ("0".equalsIgnoreCase(resuslt)) {
					// 执行成功立即返回
					sbs.append(resuslt);
				} else {
					// 失败信息记录
					sbs.append(resuslt);
				}
			} catch (IOException e) {
				logger.debug("The command  Exec failure IOException......" + e.toString());
			}
		}

		return sbs.toString();
	}

	private String FileReader(String filePath) throws IOException {

		String result = "";
		BufferedReader buf = null;

		File file = new File(filePath);
		if (file.exists()) {
			buf = new BufferedReader(new FileReader(file));
			String linstr = "";
			int count = 0;
			while ((linstr = buf.readLine()) != null) {
				count++;
				if (linstr.contains("successful")) {
					logger.debug("The success of " + linstr);
					result = "0";// 表示成功
					break;
				} else {
					// 返回失败原因
					if (count > 1) {
						result += "\r\n" + linstr;
					} else {
						result += linstr;
					}
				}
			}
		} else {
			logger.debug("The file does not exist");
		}

		// file.delete();//删除该文件
		return result;
	}

	/**
	 * 判断命名是否正常返回
	 * 
	 * @param process
	 * @return
	 */
	private int isCommandSuccsee(Process process) {
		int exitValue = 0;
		try {
			exitValue = process.waitFor();
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
		return exitValue;
	}

	private void writerFile(String subPath, String result) {
		// 入库操作
		CheckSubtoolResult sub = new CheckSubtoolResult();
		if ("0".equalsIgnoreCase(result)) {
			sub.setExeResults(SUCCESS);
		} else {
			sub.setExeResults(FAILURE);
			sub.setErrorMessage(result);
		}
		sub.setFilePath(subPath);
		sub.setCheckName(checkName);
		sub.setCreateTime(new Date());
		sub.setCreateName(loginName);
		sub.setUserNumber(number);
		subtoolRepository.save(sub);// 持久化数据

	}

}

package com.nokia.ices.apps.fusion.score.common.utils;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinuxCommand {
	public static final Logger log = LoggerFactory.getLogger(LinuxCommand.class);

	public static void truncateDirData(String loadFilePath) {
		// 程序启动时删除一次目录内容，防止因意外导致上周期文件删除失败
		log.info("Start to delete Kpi tmp file..."); 
		LinuxCommand.exec("rm -rf " + loadFilePath +File.separator+ "*"); 
		log.info("delete Kpi tmp file finished.");
	} 
	public static void exec(String cmd) {
		Process process = null;
		Runtime runTime = null;
		try {
			runTime = Runtime.getRuntime();
			String[] cmdA = { "/bin/sh", "-c", cmd };
			log.info("Execute Linux command:" + cmd);
			process = runTime.exec(cmdA);
			StreamGobbler outGobbler = new StreamGobbler(process.getInputStream(), "STDOUT");
			outGobbler.start();
			if (process.waitFor() == 0) {
				log.info(cmd + " finished.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			process.destroy();
			runTime.freeMemory();
			runTime.gc();
			process = null;
			runTime = null;
		}
	}

	public static String merge( String path, String fileName) {
		try {
			// 合并导出的文件
			log.info("Start to merge liburay:" + path + "'s files...");
			// cat 20151101/* >20151101.txt
			LinuxCommand.exec("cat " + path + File.separator + "* > " + fileName);
			log.info("cat " + path + File.separator + "* > " + fileName);
			return path + File.separator + fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

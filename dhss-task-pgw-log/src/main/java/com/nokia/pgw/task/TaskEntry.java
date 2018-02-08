package com.nokia.pgw.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nokia.pgw.service.PgwAnalysisService;
import com.nokia.pgw.settings.CustomSetting;
import com.nokia.pgw.util.PGWAnalyseUtil;

@Component
@EnableScheduling

public class TaskEntry {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskEntry.class);

	@Autowired
	private CustomSetting customSetting;
	@Autowired
	private PgwAnalysisService pgwAnalysisService;
	// @Scheduled(cron = "${dhss.pgw-log.main-program-cron}")
	@Scheduled(initialDelay = 1000, fixedDelay = 1000 * 60 * 60) // dry-run test
	private void entry() throws IOException, InterruptedException {
		if (customSetting.isDryRunMode() == false) {
			try {
				pgwLogAnalyzerEntry();
			} catch (Exception e) {
				e.getMessage();
				LOGGER.info("Synchronize or analyse failed.");
			}
			pgwAnalysisService.handlePartition();
			pgwAnalysisService.clearTempFile();
		} else {
			rsyncCommandDryRunModeTester();
		}

	}

	private void pgwLogAnalyzerEntry() {
		LOGGER.info("*******************");
		LOGGER.info("** DHSS-PGW-LOG ***");
		LOGGER.info("**PRODUCTION MODE**");
		LOGGER.info("*******************");
		List<File> synchronizedPgwLogFileList = new ArrayList<>();
		synchronizedPgwLogFileList = getPgwGzFile();//get all GZ file.
		if(synchronizedPgwLogFileList.size()>0){
			String uncompressFileDir = PrepareAction.getUncompressedFileDir();
			for (File gzFile : synchronizedPgwLogFileList) {//uncompress all GZ file.
				PGWAnalyseUtil.uncompressGzFile(gzFile,uncompressFileDir);
			}
			LOGGER.info("Start to analysis files.");
			pgwAnalysisService.analysisTargetFile();
			LOGGER.info("All files have been analysed.");
			LOGGER.info("Start to load data to DB.");
			pgwAnalysisService.loadDataToDB();
			LOGGER.info("All data are loaded to DB.");
		}else{
			LOGGER.info("Rsync result:No new PGW.");
		}

	}

	private List<File> getPgwGzFile() {
		List<String> allRsyncCommandList = new ArrayList<>();
		allRsyncCommandList = pgwAnalysisService.getAllRsyncCommand();
		List<File> synchronizedPgwLogFile = new ArrayList<>();
		for (String rsyncCommand : allRsyncCommandList) {
			List<String> synchronizedPgwLogRelativePathList = pgwAnalysisService.getRsyncInfo(rsyncCommand);
			String localSomePgwRsyncDataBaseDir = rsyncCommand.split(" ")[rsyncCommand.split(" ").length-1];
			for (String synchronizedPgwLogPath : synchronizedPgwLogRelativePathList) {
				String fileAbsPath = localSomePgwRsyncDataBaseDir+File.separator+synchronizedPgwLogPath;
				File gzFile = new File(fileAbsPath);
				synchronizedPgwLogFile.add(gzFile);
			}
		}
		return synchronizedPgwLogFile;
	}

	private void rsyncCommandDryRunModeTester() {
		LOGGER.info("*******************");
		LOGGER.info("** DHSS-PGW-LOG ***");
		LOGGER.info("** DRY-RUN  MODE **");
		LOGGER.info("*******************");
		List<String> rsyncCommnadList = new ArrayList<>();
		rsyncCommnadList = pgwAnalysisService.getAllRsyncCommand();
		for (String rsyncCommnad : rsyncCommnadList) {
			pgwAnalysisService.getRsyncInfo(rsyncCommnad);
		}
	}

}
package com.nokia.ices.apps.fusion.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nokia.ices.apps.fusion.CustomSettings;

@Component
@EnableScheduling
public class SoapGWLogSynchronize {

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	CustomSettings customSettings;
	private static String INSERT_SQL = "insert into monitor_table (create_date,file_path,name)values(?,?,?)";
	private static final Logger logger = LoggerFactory.getLogger(SoapGWLogSynchronize.class);
	private static SimpleDateFormat standardFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat fileNameFormat = new SimpleDateFormat("yyyyMMdd");
	private static String RSYNC_CMD = "sshpass -p #password# rsync #DRY-RUN# --include=\"#TargetFile#\" --exclude=\"record*/\" -ave \"ssh\" #userName#@#soapGwAddr#:#remoteDir# #localDir#";
	// sshpass -p nsn1234! rsync --dry-run --include="*.log" --exclude="record*/" -ave "ssh" oamsys@172.16.73.51:/var/log/check-system/ /etc
	//@Scheduled(fixedDelay = 100000000)
	 @Scheduled(cron = "${dhss.unicom.start-cron}")
	private void startToSynchronize() throws IOException, InterruptedException {
		logger.info("Start to synchronize SoapGW log");
		List<Map<String, String>> ipInfoList = LoadLoginInfo.getAddressInfoList();
		String password = customSettings.getPassword();
		String userName = customSettings.getUserName();
		String remoteDir = customSettings.getRemoteDir();
		if(!remoteDir.endsWith("/")){
			remoteDir = remoteDir + "/";
		}
		String localDir = customSettings.getLocalDir() + "/#soapgw-name#";
		String includeFileName = fileNameFormat.format(new Date()) + ".log";
		List<String> commandList = new ArrayList<>();
		Boolean dryRunMode = customSettings.getDryRunMode();
		for (Map<String, String> map : ipInfoList) {
			String rsyncCmd = RSYNC_CMD;
			rsyncCmd = rsyncCmd.replace("#password#", password);
			rsyncCmd = rsyncCmd.replace("#userName#", userName);
			rsyncCmd = rsyncCmd.replace("#TargetFile#", includeFileName);
			rsyncCmd = rsyncCmd.replace("#remoteDir#", remoteDir);
			rsyncCmd = rsyncCmd.replace("#localDir#", localDir);
			rsyncCmd = rsyncCmd.replace("#soapGwAddr#", map.get("addr"));
			rsyncCmd = rsyncCmd.replace("#soapgw-name#", map.get("name"));
			if(!dryRunMode){
				rsyncCmd = rsyncCmd.replace("#DRY-RUN#", "");
			}else{
				rsyncCmd = rsyncCmd.replace("#DRY-RUN#", "--dry-run");
			}
			logger.info("Generate a CMD:" + rsyncCmd);
			commandList.add(rsyncCmd);
		}
		
		if (!dryRunMode) {
			executeCmd(commandList);
		} else {
			dryRunCmd(commandList);
		}
	}

	private void dryRunCmd(List<String> commandList) throws InterruptedException, IOException {
		logger.info("** DRY-RUN **");
		for (String rsyncCmd : commandList) {
			logger.info("Execute dry run cmd:"+rsyncCmd);
			Process rysncProcess = Runtime.getRuntime().exec(rsyncCmd);
			rysncProcess.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(rysncProcess.getInputStream()));
			String currentLine = null;
			while ((currentLine = br.readLine()) != null) {
				logger.info(currentLine);
			}
		}
	}

	private void executeCmd(List<String> commandList) throws IOException, InterruptedException {
		logger.info("** PRODUCTION **");
		String todayFileName = fileNameFormat.format(new Date())+".log";
		Calendar todayC = Calendar.getInstance();
		todayC.add(Calendar.DATE, -1);
		Date yesterday = todayC.getTime();
		String yesterdayFileName = fileNameFormat.format(yesterday)+".log";
		logger.info("Today file Name:"+todayFileName);
		for (String rsyncCmd : commandList) {
			logger.info("Execute RSYNC cmd:"+rsyncCmd);
			String localLogPath = rsyncCmd.split(" ")[rsyncCmd.split(" ").length - 1]; // 取得本地路径
			Process rysncProcess = Runtime.getRuntime().exec(rsyncCmd);
			rysncProcess.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(rysncProcess.getInputStream()));
			String currentLine = null;
			while ((currentLine = br.readLine()) != null){
				String fileNameInCallBack = currentLine.split("/")[currentLine.split("/").length-1];
				//logger.info("file name in the callback:"+fileNameInCallBack);
				if (todayFileName.equals(fileNameInCallBack)) {
					String create_date = standardFormat.format(new Date());
					String file_path = localLogPath +"/"+ todayFileName;
					String file_name = localLogPath.split("/")[localLogPath.split("/").length - 1] + "_" + todayFileName; // 文件名=
					logger.info("abs path:" + file_path);
					logger.info("file_name:" + file_name);
					this.jdbcTemplate.update(INSERT_SQL, new Object[] { create_date, file_path, file_name });
				}
				if (yesterdayFileName.equals(fileNameInCallBack)) {
					String create_date = standardFormat.format(new Date());
					String file_path = localLogPath +"/"+ yesterdayFileName;
					String file_name = localLogPath.split("/")[localLogPath.split("/").length - 1] + "_" + yesterdayFileName; // 文件名=
					logger.info("abs path:" + file_path);
					logger.info("file_name:" + file_name);
					this.jdbcTemplate.update(INSERT_SQL, new Object[] { create_date, file_path, file_name });
				}
			}

		}
		
		logger.info("** SOAP-GW Log synchronizing completed! **");
	}

}
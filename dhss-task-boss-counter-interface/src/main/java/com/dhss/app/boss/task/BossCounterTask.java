package com.dhss.app.boss.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dhss.app.boss.config.BossCounterConfig;
import com.dhss.app.boss.domain.BossCounter;
import com.dhss.app.boss.domain.BossCounterMarker;
import com.dhss.app.boss.service.BossCounterService;

@Component
@EnableScheduling
public class BossCounterTask {
	private static Logger LOGGER = LoggerFactory.getLogger(BossCounterTask.class);

	@Autowired
	BossCounterService bossCounterService;
	@Autowired
	BossCounterConfig bossCounterConfig;

	//@Scheduled(cron = "0 0/5 * * * ?")
	//@Scheduled(cron = "0 0/5 * * * ?")
	@Scheduled(initialDelay = 1000, fixedDelay = 1000 * 60 * 60) // dry-run test
	public void outputDataGenerator() throws IOException{
		int interval = bossCounterConfig.getIntervalMin();
		DateTime startTime = new DateTime().minusMinutes(30);
		//1.复制BOSS后台定时任务rsync-data目录下面的所有soap文件
		String rsyncDataDir = bossCounterConfig.getRsyncDataDir();
		String counterWorkSpace = bossCounterConfig.getCounterWorkSpaceDir();
		bossCounterService.copyRsyncDataToWorkspace(rsyncDataDir,counterWorkSpace);
		//2.获取每台SOAP的解析顺序。
		Map<String,List<File>> soapOrderListMap = bossCounterService.getAnalysisOrder();
		//3.获得解析对象
		//
		String startTimeS = "2017-08-31 00:05:00";
		String endTimeS = "2017-08-31 00:20:00";
		
		DateTime startTimeSD = DateTime.parse(startTimeS,BossCounterService.DATETIME_FORMAT);
		DateTime endTimeSD = DateTime.parse(endTimeS,BossCounterService.DATETIME_FORMAT);
		//
		Map<String,File> analysisTargetMap = bossCounterService.generateAnalysisTarget(soapOrderListMap,startTimeSD,endTimeSD);
		//4.解析对象
		Map<String,Integer> dataMap = bossCounterService.analysisData(analysisTargetMap);
		//5.输出数据
		bossCounterService.outPutJsonFile(dataMap,startTimeSD,endTimeSD);
		//6.删除工作空间
		//FileUtils.deleteDirectory(new File(bossCounterConfig.getCounterWorkSpaceDir()));
	}
	
	
}

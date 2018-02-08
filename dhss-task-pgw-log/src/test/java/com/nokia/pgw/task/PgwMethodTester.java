//package com.nokia.pgw.task;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.io.FileUtils;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.util.Assert;
//
//import com.nokia.pgw.service.PgwAnalysisService;
//import com.nokia.pgw.util.PGWAnalyseUtil;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class PgwMethodTester {
//	private static final Logger LOGGER = LoggerFactory.getLogger(PgwMethodTester.class);
//	@Autowired
//	PgwAnalysisService pgwAnalysisService;
//	@Test
//	public void uncompressGzTester() {
//		File file = new File("E:/bossdata/boss/");
//		try {
//			FileUtils.deleteDirectory(file);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		PGWAnalyseUtil.uncompressGzFile("E:/bossdata/boss.tar.gz", "E:/bossdata/boss/");
//		Assert.isTrue(file.exists(), "file not Exists ");
//	}
//
//	@Test
//	public void getAllRsyncCommandTester() {
//		List<String> rsyncCommandList = new ArrayList<>(); 
//		rsyncCommandList = pgwAnalysisService.getAllRsyncCommand();
//		for (String rsyncCommand : rsyncCommandList) {
//			LOGGER.info(rsyncCommand);
//		}
//	}
//}

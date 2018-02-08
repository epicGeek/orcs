package com.dhss.app.boss.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.dhss.app.boss.domain.BossCounter;
import com.dhss.app.boss.domain.BossCounterMarker;

public interface BossCounterService {
	public static DateTimeFormatter DATETIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	public void outPutJsonFile(Map<String, Integer> dataMap, DateTime startTime, DateTime endTimeSD) throws IOException ;
	public Map<String, BossCounterMarker> checkMarker(Map<String,List<File>> soapOrderListMap);
	public void copyRsyncDataToWorkspace(String rsyncDataDir, String counterWorkSpace) throws IOException;
	public Map<String, List<File>> getAnalysisOrder();
	public Map<String, File> generateAnalysisTarget(Map<String, List<File>> soapOrderListMap,DateTime startTime, DateTime endTimeSD) throws IOException;
	public Map<String, Integer> analysisData(Map<String, File> analysisTargetMap);
}

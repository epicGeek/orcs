package com.nokia.pgw.service;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author Pei Nan
 *	
 */
public interface PgwAnalysisService {
	public List<Map<String,Object>> deviceBasicInfo();
	public List<String> getAllRsyncCommand();
	public List<String> getRsyncInfo(String rsyncCmd);
	public void loadDataToDB();
	public void analysisTargetFile();
	public void handlePartition();
	public void clearTempFile();
}

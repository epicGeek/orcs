package com.nokia.ices.apps.fusion.score.common.callable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokia.ices.apps.fusion.score.common.utils.ExportCsv;
import com.nokia.ices.apps.fusion.score.service.impl.AreaServiceImpl;

public class Exportcallable implements Callable<String> {
 
	private CountDownLatch countDownLatch;
	private int page;
	private String loadPath;
	private String[] showColumnArray;
	private String[] keyArray;
	private List<Map<String, Object>> result;
	private final static Logger logger = LoggerFactory.getLogger(Exportcallable.class);

	public Exportcallable(List<Map<String, Object>> list, CountDownLatch countDownLatch, int page, String loadPath, String[] showColumnArray, String[] keyArray) {
		this.result = list;
		this.countDownLatch = countDownLatch;
		this.page = page;
		this.loadPath = loadPath;
		this.showColumnArray = showColumnArray;
		this.keyArray = keyArray;
	}

 
	@Override
	public String call() throws Exception { 

		String fileName = loadPath +File.separator+ page + ".csv";
		ExportCsv ec = new ExportCsv();
		File csvFile = null;
		BufferedWriter csvFileOutputStream = null;
		try {
			if (result.size() > 0) {
				csvFile = new File(fileName);
				csvFile.createNewFile();
				csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 2048);
				ec.createCSVFile(result, showColumnArray, keyArray, csvFileOutputStream, null);
				csvFileOutputStream.close();
			}
			return fileName;
		} catch (Exception e) {

		} finally {
			countDownLatch.countDown();
			try {
				if (csvFileOutputStream != null) {
					csvFileOutputStream.close();
				}
			} catch (IOException e) {
				logger.error("An error occurred while invoke method:exportBreakReason,error message:{}", e.getMessage());
				e.printStackTrace();
			}
			csvFileOutputStream = null;
		}
		return fileName;

	}

}

package com.nokia.boss;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.nokia.boss.task.LoadStaticData;
import com.nokia.boss.util.DateUtils;

public class Test {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
	private static SimpleDateFormat sdfHH = new SimpleDateFormat("HH");
	public static void main(String[] args) throws IOException {
		String[] d = {"1@1@1","2!2!2","3@3@3"};
		for(String s:d){
			try {
				s = s.split("@")[1];
				System.out.println(s);
			} catch (Exception e) {
				e.printStackTrace();
		}
	}
	}
	public static void getDefaultSelectOptionsTimePeriod() {
		Calendar nowC = Calendar.getInstance();
		int nowMinute = nowC.get(Calendar.MINUTE);
		if(nowMinute<15){
			nowC.set(Calendar.MINUTE, 0);
		}else if(nowMinute>=15&&nowMinute<30){
			nowC.set(Calendar.MINUTE, 15);
		}else if(nowMinute>=30&&nowMinute<45){
			nowC.set(Calendar.MINUTE, 30);
		}else if(nowMinute>45){
			nowC.set(Calendar.MINUTE, 45);
		}
		Date startTime = nowC.getTime();
		nowC.add(Calendar.MINUTE, 15);
		Date endTime = nowC.getTime();
		System.out.println(sdf.format(startTime)+"-"+sdf.format(endTime));
	}
	/**
	 * 每次同步前会检查规范文件，是否是同步今日的数据。
	 * @param bossVersion
	 * @throws IOException
	 */
	private static void checkPatternFile(String bossVersion) throws IOException {
		File patternFile = new File("E:/pattern.txt");
		//File patternFile = new File(CustomSettings.getRuleFileAbsPath());
		FileReader fr = new FileReader(patternFile);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		StringBuilder patternText = new StringBuilder();
		while ((line = br.readLine())!=null) {
			patternText.append(line+"\n");
		}
		br.close();
		fr.close();
		String patternStr = patternText.toString().trim();
		String todayStr = DateUtils.getTargetTime().get("TODAY");
		String todayPattern = "";
		if(bossVersion.equalsIgnoreCase("chinamobile")){
			todayPattern = LoadStaticData.getRulePatternCM().replace("yyyy-MM-dd", todayStr);
		}else if(bossVersion.equalsIgnoreCase("unicom")){
			todayPattern = LoadStaticData.getRULE_PATTERN_CUC().replace("yyyy-MM-dd", todayStr);
		}
		if(!patternStr.equals(todayPattern)){
			FileWriter fw = new FileWriter(patternFile);
			fw.write(todayPattern);
			fw.close();
			FileReader fr_ = new FileReader(patternFile);
			BufferedReader br_ = new BufferedReader(fr_);
			String line_ = null;
			System.out.println("Pattern rule file has been changed as:");
			while ((line_ = br_.readLine())!=null) {
				System.out.println(line_);
			}
			br_.close();
		}else{
			System.out.println("Pattern rule is correct.");
		}
	}
}

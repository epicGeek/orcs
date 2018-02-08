package com.nokia.boss;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class MockDataGenerator {
	private static String BOSS_DATA_DIR = "E:/bossdata/chinamobile/2017-02-17/";
	private static String specifiedDateStr = "2017-02-24";
	public static void main(String[] args) throws IOException {
		List<File> soapFiles = new ArrayList<>();
		List<File> errFiles = new ArrayList<>();
		
		Map<String,List<File>> m = dataFileCategory();
		soapFiles = m.get("soap");
		errFiles = m.get("err");
		mockSoapDataGenerator(soapFiles);
		mockErrDataGenerator(errFiles);
	}
	private static void mockErrDataGenerator(List<File> errFiles) throws IOException {
		for (File errFile : errFiles) {
			File transErrFile = gzToFile(errFile);
		}
		
	}
	private static void mockSoapDataGenerator(List<File> soapFiles) throws IOException {
		for (File soapFile : soapFiles) {
			File transSoapFile = gzToFile(soapFile);
			FileReader fr = new FileReader(transSoapFile);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			StringBuilder sb = new StringBuilder();
			while((line = br.readLine())!=null){
				line = line.replace(line.substring(0, 9), specifiedDateStr)+"\n";
				sb.append(line);
			}
			br.close();
		}
	}
	public static Map<String,List<File>> dataFileCategory() {
		Map<String,List<File>> m = new HashMap<>();
		List<File> soapFiles = new ArrayList<>();
		List<File> errFiles = new ArrayList<>();
		File dataFileDir = new File(BOSS_DATA_DIR);
		File[] dataFiles = dataFileDir.listFiles();
		soapFiles = new ArrayList<>();
		errFiles = new ArrayList<>();
		for (File dataFile : dataFiles) {
			if(dataFile.getName().contains("SOAP")){
				soapFiles.add(dataFile);
			}else{
				errFiles.add(dataFile);
			}
		}
		m.put("soap", soapFiles);
		m.put("err", errFiles);
		return m;
	}
	public static File gzToFile(File gzFile) throws IOException{
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new GZIPInputStream(new FileInputStream(gzFile.getAbsolutePath())), "utf-8"));
		String line = null;
		StringBuilder gzText = new StringBuilder();
		while ((line = br.readLine()) != null) {
			gzText.append(line+"\n");
		}
		br.close();
		String tempTransformFileAbsPath = "E:/bossdata/chinamobile/mockdata/"+gzFile.getName().replace(".gz", ".trans");
		File f = new File(tempTransformFileAbsPath);
		FileWriter fw = new FileWriter(f);
		fw.write(gzText.toString());
		fw.close();
		return f;
	}
}

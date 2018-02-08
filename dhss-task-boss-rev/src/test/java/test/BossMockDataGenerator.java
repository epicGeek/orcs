package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class BossMockDataGenerator {
	private static String MOCK_DATE_STR = "2017-03-01";
	private static String DATA_SOURCE_DIR = "E:/bossdata/chinamobile/2017-02-17/";
	public static void main(String[] args) throws IOException {
		Map<String,List<File>> dataSourceMap = new HashMap<>();
		dataSourceMap = dataPicker(DATA_SOURCE_DIR);
		List<File> soapFiles = dataSourceMap.get("soap");
		List<File> errFiles = dataSourceMap.get("err");
		mockSoapData(soapFiles);
		mockErrData(errFiles);
	}

	private static void mockErrData(List<File> errFiles) throws IOException {
		for (File errFile : errFiles) {
			File transformedFile = gzToFile(errFile,"err");
			compressFile(transformedFile.getAbsolutePath());
			transformedFile.delete();
		}
		
	}

	private static void mockSoapData(List<File> soapFiles) throws IOException {
		for (File soapFile : soapFiles) {
			File transformedFile = gzToFile(soapFile,"soap");
			compressFile(transformedFile.getAbsolutePath());
			transformedFile.delete();
		}
		
	}

	private static File gzToFile(File gzFile,String fileType) throws IOException{
		String cacheDir = "E:/bossdata/cache/";
		String transformedFileName = gzFile.getName().replace(".gz", "");
		if(transformedFileName.contains("SOAP")){
			if(transformedFileName.contains("CUC")){
				transformedFileName = transformedFileName.replace(transformedFileName.substring(31, 41), MOCK_DATE_STR);
			}else{
				transformedFileName = transformedFileName.replace(transformedFileName.substring(27, 37), MOCK_DATE_STR);
			}
		}else if(transformedFileName.contains("ERR")){
			if(transformedFileName.contains("CUC")){
				transformedFileName = transformedFileName.replace(transformedFileName.substring(22,32), MOCK_DATE_STR);
			}else{
				transformedFileName = transformedFileName.replace(transformedFileName.substring(18,28), MOCK_DATE_STR);
			}
			
		}
		File transformedFile = new File(cacheDir+transformedFileName);
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new GZIPInputStream(new FileInputStream(gzFile.getAbsolutePath())), "utf-8"));
		String line = null;
		FileWriter fw = new FileWriter(transformedFile,true);
		while ((line = br.readLine()) != null) {
			if(fileType.equals("soap")){
				line = line.replace(line.substring(0, 10), MOCK_DATE_STR);
				fw.write(line+"\n");
			}else if(fileType.equals("err")){
				
				if(line.contains("task id")){
					line = line.replace(line.substring(0, 10), MOCK_DATE_STR);
					fw.write(line+"\n");
				}else{
					fw.write(line+"\n");
				}
			}
		}
		fw.close();
		br.close();
		return transformedFile;
	}
	private static Map<String, List<File>> dataPicker(String dataSourceDir) {
		File originalDataDir = new File(dataSourceDir);
		File[] fileLists = originalDataDir.listFiles();
		List<File> soapFiles = new ArrayList<>();
		List<File> errFiles = new ArrayList<>();
		for (File originalFile : fileLists) {
			if(originalFile.getName().contains("SOAP")&&originalFile.getName().endsWith("gz")){
				soapFiles.add(originalFile);
			}else if(originalFile.getName().contains("CASE")&&originalFile.getName().endsWith("gz")){
				errFiles.add(originalFile);
			}
		}
		Map<String, List<File>> m = new HashMap<>();
		m.put("soap", soapFiles);
		m.put("err", errFiles);
		return m;
	}
	public static void compressFile(String inFileName) {  
        String outFileName = inFileName + ".gz";  
        FileInputStream in = null;  
        try {  
            in = new FileInputStream(new File(inFileName));  
        }catch (FileNotFoundException e) {  
            System.out.println("Could not find the inFile..."+inFileName);             
        }  
          
        GZIPOutputStream out = null;  
        try {  
            out = new GZIPOutputStream(new FileOutputStream(outFileName));  
        }catch (IOException e) {  
            System.out.println("Could not find the outFile..."+outFileName);  
              
        }  
        byte[] buf = new byte[10240];  
        int len = 0;  
        try {                                 
            while (((in.available()>10240)&& (in.read(buf)) > 0)) {    
                out.write(buf);               
            }          
            len = in.available();              
            in.read(buf, 0, len);              
            out.write(buf, 0, len);                       
            in.close();  
            System.out.println("Completing the GZIP file..."+outFileName);  
            out.flush();  
            out.close();  
        }catch (IOException e) {  
              
        }  
    } 
}

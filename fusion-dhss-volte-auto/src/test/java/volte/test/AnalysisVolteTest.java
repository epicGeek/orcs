package volte.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class AnalysisVolteTest {

	private static final Logger logger = LoggerFactory.getLogger(AnalysisVolteTest.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	@Autowired
	JdbcTemplate jdbcTemplate;

	//@Test
	public void analysisVolte() throws IOException, ParseException {
		//单纯的入库
		String messagePath = "C://Users//Administrator.b-PC//Desktop//volte-txt-analysise//boss2hss-soap50-2016090909.txt";
		// 解析文件路径来判断数据流向
		String flowDirection = messagePath.substring(messagePath.lastIndexOf("/")).substring(1).split("-")[0];
		String neName = messagePath.substring(messagePath.lastIndexOf("/")).substring(1).split("-")[1];
		File messageFile = new File(messagePath);
		String encoder = "UTF-8";
		if (messageFile.isFile() && messageFile.exists()) {
			// 读取文件
			String sql = "INSERT INTO volte_message (imsi,msisdn,status,start_time,cost_time,flow_direction,ne_name) "
					+ "values (?,?,?,?,?,?,?)";
			InputStreamReader read = new InputStreamReader(new FileInputStream(messageFile), encoder);// 考虑到编码格式
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				if (lineTxt.startsWith("460")) {
					String[] analysisElement = lineTxt.split("\\|");
					Date date = sdf.parse(analysisElement[3]);
					Object[] insertElement = { analysisElement[0], analysisElement[1], analysisElement[2], date,
							analysisElement[4], flowDirection, neName };
					logger.info("This data-> imsi:" + analysisElement[0] + " msisdn:" + analysisElement[1] + " status:"
							+ analysisElement[2] + " startTime:" + analysisElement[3] + " costTime:"
							+ analysisElement[4] + " flowDirection:" + flowDirection + " neName:" + neName);
					jdbcTemplate.update(sql, insertElement);
				}
			}
			bufferedReader.close();
		}
	}
	
	//@Test
	public void AnalysisVolteWithLoad() throws IOException{
		//TO-DO IO LOAD
		//1.解析文件名。判断是否写完.命名规则：数据流向-网元名-完成时间-写入状态  example: hss2boss-soap50-201609092213-complete.txt(表示已完成)
		String messagePath = "C://Users//Administrator.b-PC//Desktop//volte-txt-analysise//hss2boss-soapgw50-20160815120000000-complete.txt";
		//流程： 解析已完成的数据->IO增加其余字段的值->SQL LOAD
		String inputTxtName = messagePath.substring(messagePath.lastIndexOf("/")).substring(1);
		String[] messageTxtNameElement = messagePath.substring(messagePath.lastIndexOf("/")).substring(1).split("-");
		String flowDirection = messageTxtNameElement[0];
		String neName = messageTxtNameElement[1];
		boolean isComplete = false;
		String isCompleteFlag = messageTxtNameElement[messageTxtNameElement.length-1];
		if(isCompleteFlag.startsWith("complete")){
			isComplete=true;
		}
		File messageTxt = new File(messagePath);
		if(!messageTxt.exists()||!messageTxt.canRead()){
			return;
		}
		String encoder = "UTF-8";
		InputStreamReader read = new InputStreamReader(new FileInputStream(messageTxt), encoder);// 考虑到编码格式
		BufferedReader bufferedReader = new BufferedReader(read);
		String lineTxt = null;
		List<String> dataPieceList = new ArrayList<String>();
		while ((lineTxt = bufferedReader.readLine()) != null) {
			if (lineTxt.startsWith("460")){
				dataPieceList.add(lineTxt); //加入缓存
			}
		}
		String targetDir = "C:/Users/Administrator.b-PC/Desktop/volte-txt-analysise/writeIn/";
//		String writeInTxtName = inputTxtName.replaceAll(".txt", "-writeIn-"+sdf.format(new Date())+".txt");
		String writeInTxtName = "fku";
		logger.info(writeInTxtName);
		logger.info(targetDir+writeInTxtName);
		FileWriter fw = new FileWriter(targetDir+writeInTxtName);
		BufferedWriter output = new BufferedWriter(fw);
		logger.info("start to write in");
		for(int i = 1 ; i<dataPieceList.size();i++){
			output.write(dataPieceList.get(1)+"\r\n");
			if(i>=dataPieceList.size()/2){
				output.flush();
			}
		}
		output.close();
		logger.info("write completed.");
	}
	
	@Test
	public void loadDataInstantly(){
		//能直接LOAD的文件
		String filePath = "C:/Users/Administrator.b-PC/Desktop/volte-txt-analysise/hss2boss-soapgw50-20160815120000000-complete.txt";
		String signalRN = "\\r\\n";
		logger.info(signalRN);
		//String filePathReplaced = filePath.replaceAll("/", "\\");
		//logger.info(filePathReplaced);
		//   java linux mysql 一般用/
		//    WINDOWS路径一般用\ 就你微软操蛋
		File loadFile = new File(filePath);
		if(!loadFile.exists()){
			logger.info("not exist");
			return;
		}
		if(!loadFile.canRead()){
			logger.info("this file:"+filePath+" "+"can not be read.");
		}
		String sql = "LOAD DATA LOCAL INFILE '"+filePath+"' INTO TABLE volte_message FIELDS TERMINATED BY '|' LINES TERMINATED BY '"+signalRN+"' (imsi,msisdn,action_status,start_time,cost_time) ";//LOAD DATA
		try {
			logger.info(sql);
			jdbcTemplate.execute(sql);
		} catch (Exception e) {
			
		}
		
	}
	
	
	
	
	
	
	//@Test
	public void analysisXml(){
		String messagePath = "C://Users//Administrator.b-PC//Desktop//volte-txt-analysise//boss2hss-soap50-2016090909.txt";
	}
}
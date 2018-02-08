package com.nokia.ices.apps.dhss.alarm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.nokia.ices.apps.fusion.App;
import com.nokia.ices.apps.fusion.alarm.IcesAlarmRule;
import com.nokia.ices.apps.fusion.alarm.repository.IcesAlarmRuleRepository;

import junit.framework.TestCase;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=App.class)
public class ImportAlarmDesc extends TestCase{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private IcesAlarmRuleRepository icesAlarmRuleRepository;
	
	@Test
	public void testJdbcTemplate(){
		jdbcTemplate.queryForList("select 1 ");
	}
	
	@Test
	public void test() throws IOException{
		String basePath = "/Users/quyidong/Downloads/新建文件夹/";
		File file = new File(basePath);
		File result = new File("/Users/quyidong/Downloads/新建文件夹/output.sql");
		FileWriter fw = new FileWriter(result);
		String[] fileNames = file.list();
		
		int total = 0;
		int insert = 0;
		int errorCount = 0;
		
		for (String string : fileNames) {
			if(!string.endsWith(".xls")){
				continue;
			}
			String filePath = (basePath + string);
			File fileExcel = new File(filePath);
			FileInputStream fis = new FileInputStream(fileExcel);
			System.out.println(fileExcel);
			HSSFWorkbook template = new HSSFWorkbook(fis);
			Sheet sheet = template.getSheetAt(0);
			int maxRow = sheet.getLastRowNum();
			System.out.println(maxRow+":"+filePath);
			for (Row row : sheet) {
				if(row.getRowNum()==0) continue;
				
				total ++;
				int cellCount = row.getLastCellNum();
				String sqlInsert = ("insert into ices_alarm_rule "
						+ "(alarm_no,release_version,alarm_text,alarm_meaning,alarm_desc) values ("
						+ "'"+row.getCell(0)+"','"+row.getCell(2)+"','"+row.getCell(3)+"','"+row.getCell(6)+"','"+row.getCell(9)+"');\n");
				System.out.println(sqlInsert);
//				fw.write(sqlInsert);
				IcesAlarmRule iar = new IcesAlarmRule();
				try {
					iar.setAlarmNo(Integer.parseInt(row.getCell(0).toString()));
				} catch (Exception e) {
					// TODO: handle exception
				}
				iar.setReleaseVersion(row.getCell(2).toString());
				iar.setAlarmText(row.getCell(3).toString());
				iar.setAlarmType(row.getCell(4).toString());
				iar.setProbableCause(row.getCell(5).toString());
				iar.setAlarmMeaning(row.getCell(6).toString());
				iar.setAlarmDesc(row.getCell(9).toString());
				iar.setFromRow("from:"+row.getRowNum());
				iar.setIsActive(true);
				iar.setFromFile(filePath);
				try {
//					icesAlarmRuleRepository.save(iar);
					insert ++;
				} catch (Exception e) {
					errorCount ++;
					fw.write(e.getMessage() + "============\n"+sqlInsert);
				}
				System.out.println("\ninsert:"+insert+"\nerror:"+errorCount);
			}
			template.close();
			fis.close();
		}
		System.out.println("total:"+total +"\ninsert:"+insert+"\nerror:"+errorCount);
		fw.flush();
		fw.close();
//		;// 读取模板
	}
	
}

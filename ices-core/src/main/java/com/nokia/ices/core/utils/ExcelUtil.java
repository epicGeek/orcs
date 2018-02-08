package com.nokia.ices.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class ExcelUtil {
	
	
	 public static void main(String[] args) {  
		 
	  
	 }
	 
	@SuppressWarnings("resource")
	public static List<String> readXls(InputStream is) throws IOException{
	          HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
	          List<String> list = new ArrayList<String>();
	        // 循环工作表Sheet
	        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
	             HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
	            if (hssfSheet == null) {
	                  continue;
	            }
	             // 循环行Row
	              for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
	                  HSSFRow hssfRow = hssfSheet.getRow(rowNum);
	                if (hssfRow != null) {
	                     HSSFCell no = hssfRow.getCell(0);
	                     String value = getValue(no);
	                     if(" ".equals(value)){
	                    	 continue;
	                     }
	                     list.add(value);
	                  }
	              }
	          }
	          return list;
     }
	 
	 /**
	  * 
	  * @param is
	  * @param length cell 列 获取值
	  * @param checkName 检查项目
	  * @return
	  * @throws IOException  
	  */
	public static  void readXls(Map<String,List< String []>> numberMap,InputStream is,int length,String checkName) throws IOException{
	          HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
	          List< String []> imsilist = new ArrayList< String []>();
	         
	         boolean flag = true;
	        // 循环工作表Sheet
	        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
	             HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
	            if (hssfSheet == null) {
	                  continue;
	            }
	             // 循环行Row
	              for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
	                  HSSFRow hssfRow = hssfSheet.getRow(rowNum);
	                if (hssfRow != null) {
	                	String [] number = new String[length];
	                	for(int i=0;i<length;i++){
	                		HSSFCell cellvalue = hssfRow.getCell(i);
	                		if(cellvalue==null){
	                			flag = false;
	                			break;
	                		}
	                		String textValue = getValue(cellvalue);
	                		if(null!=textValue && !" ".equals(textValue)){
	                			if(textValue.length()==11){
	                				textValue = "86"+textValue;
	                			}
	                			number[i] =textValue; 
	                		}
	                	}
	                	if(flag){
	                		imsilist.add(number);
	                	}
	                  }
	              }
	              
	          }
        	//存放到集合等待获取集合
        	numberMap.put(checkName, imsilist);
        	hssfWorkbook.close();
     }
	 
	 
	 private static String getValue(HSSFCell hssfCell) {
		 
		 if(null!=hssfCell){
			 if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
				 // 返回布尔类型的值
				 return String.valueOf(hssfCell.getBooleanCellValue());
			 } else if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				 // 返回数值类型的值
				 
				 hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				 String value = String.valueOf(hssfCell.getStringCellValue());
				 return value;
				// return String.valueOf(hssfCell.getNumericCellValue());
			 } else {
				 // 返回字符串类型的值
				 return String.valueOf(hssfCell.getStringCellValue());
			 }
		 }
		 
		 return null;
    }
	 
	 /**
	  * 创建excel文件
	  * @param title sheet 名称
	  * @param headers title标题
	  * @param defaultValue 默认值
	  * @param out 输入
	  * @param pattern
	  * @throws Exception
	  */
	 @SuppressWarnings("deprecation")
	public static void createExcel(String title, String[] headers,
	            String [] defaultValue, OutputStream out) throws Exception {
	     HSSFWorkbook workbook = null;
          try{
			 
			 // 声明一个工作薄
			 workbook = new HSSFWorkbook();
			 // 生成一个表格
			 HSSFSheet sheet = workbook.createSheet(title);
			 // 设置表格默认列宽度为15个字节
			 sheet.setDefaultColumnWidth((short) 20);
			 // 生成一个样式
			 HSSFCellStyle style = workbook.createCellStyle();
			 // 设置这些样式
			 style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			 style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			 style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			 style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			 style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			 style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			 style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			 // 生成一个字体
			 HSSFFont font = workbook.createFont();
			 font.setColor(HSSFColor.VIOLET.index);
			 font.setFontHeightInPoints((short) 12);
			 font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			 // 把字体应用到当前的样式
			 style.setFont(font);
			 // 产生表格标题行
			 HSSFRow row = sheet.createRow(0);
			 for (short i = 0; i < headers.length; i++) {
				 HSSFCell cell = row.createCell(i);
				 cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				 cell.setCellStyle(style);
				 HSSFRichTextString text = new HSSFRichTextString(headers[i]);
				 cell.setCellValue(text);
			 }
			 
			 //创建一行写入模板参数数据
			 HSSFRow rowText = sheet.createRow(1);
			 HSSFCellStyle styleText = workbook.createCellStyle();
			 HSSFDataFormat format = workbook.createDataFormat();  
			 styleText.setDataFormat(format.getFormat("@"));  
			 String value  = "";
			 if(defaultValue.length>0 && StringUtils.isNotEmpty(defaultValue[0])){
				 for(int i=0;i<defaultValue.length;i++){
					 HSSFCell cell = rowText.createCell(i);
					 cell.setCellStyle(styleText);
					 value = defaultValue[i];
					 if(null!=value && !"null".equals(value)&& !"undefined".equals(value)){
						 cell.setCellValue(defaultValue[i]);
					 }
				 }
			 }else{
				 for (short i = 0; i < headers.length; i++) {
					 HSSFCell cell = rowText.createCell(i);
					 cell.setCellStyle(styleText);
					 cell.setCellValue("");
				 }
			 }
			 workbook.write(out);
		 }catch(Exception e){
			 e.printStackTrace();
		 }finally{
			 out.close();
			 workbook.close();
		 }
		 
	 }

}
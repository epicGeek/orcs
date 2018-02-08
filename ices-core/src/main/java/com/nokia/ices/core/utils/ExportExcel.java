package com.nokia.ices.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExportExcel<T> {
	
	/**
	 * 
	 * @param title sheetNmae
	 * @param headers  表头
	 * @param dataset 数据
	 * @param out 输出流
	 * @param pattern 时间格式转换
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked"})
	public void exportExcel(String title, String[] headers,
            Collection<T> dataset, OutputStream out, String pattern) throws Exception {
        // 声明一个工作薄
		XSSFWorkbook workbook = new XSSFWorkbook();
        // 生成一个表格
		XSSFSheet sheet = workbook.createSheet(title);
        //HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 15);
        // 生成一个样式
        XSSFCellStyle style = workbook.createCellStyle();
		//  HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        // 设置对齐方式  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);// 水平居中  
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中  
        
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        XSSFFont font = workbook.createFont();
       // HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        XSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        XSSFFont font2 = workbook.createFont();
        font2.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);
        
        // 产生表格标题行
        XSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.length; i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            XSSFRichTextString text = null;
            if(headers[i].indexOf(":")!=-1){
            	 text = new XSSFRichTextString(headers[i].split(":")[0]);
            }else{
            	text = new XSSFRichTextString(headers[i]);
            }
            cell.setCellValue(text);
        }
        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = (T) it.next();
            for (short i = 0; i < headers.length; i++) {
            	XSSFCell cell = row.createCell(i);
            	cell.setCellStyle(style2);
            	String textValue = null;
            	if(t instanceof String []){
            		String [] values = (String [])t;
            		textValue = values[i];
            	}else{
            		HashMap<String, Object> map = (HashMap<String, Object>)t;
            		String key = headers[i].split(":")[1];
            		if(null!=map.get(key)){
            			textValue = map.get(key).toString();
            		}
            	}
            	if (textValue != null) {
            		XSSFRichTextString richString = new XSSFRichTextString(textValue);
            		cell.setCellValue(richString);
            	}
            }

        }
		workbook.write(out);
		workbook.close();
    }
	
	
	 /**
	  * 
	  * @param is
	  * @param length cell 列 获取值
	  * @param checkName 检查项目
	  * @return
	  * @throws IOException  
	  */
	public static  List< String []> readXls(InputStream is,int length) throws IOException{
	          XSSFWorkbook hssfWorkbook = new XSSFWorkbook(is);
	          List< String []> dataList = new ArrayList< String []>();
	         
	         boolean flag = true;
	        // 循环工作表Sheet
	        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
	             XSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
	            if (hssfSheet == null) {
	                  continue;
	            }
	             // 循环行Row
	              for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
	                  XSSFRow hssfRow = hssfSheet.getRow(rowNum);
	                if (hssfRow != null) {
	                	String [] number = new String[length];
	                	for(int i=0;i<length;i++){
	                		XSSFCell cellvalue = hssfRow.getCell(i);
	                		if(cellvalue==null){
	                			flag = false;
	                			break;
	                		}
	                		String textValue = getValue(cellvalue);
	                		if(null!=textValue && !" ".equals(textValue)){
	                			number[i] =textValue; 
	                		}
	                	}
	                	if(flag){
	                		dataList.add(number);
	                	}
	                  }
	              }
	              
	          }
	        hssfWorkbook.close();
       	return dataList;
    }
	 
	 
	 @SuppressWarnings("static-access")
	private static String getValue(XSSFCell hssfCell) {
		 
		 if(null!=hssfCell){
			 if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
				 // 返回布尔类型的值
				 return String.valueOf(hssfCell.getBooleanCellValue());
			 } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
				 // 返回数值类型的值
				 
				 hssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
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
}

package com.nokia.ices.apps.fusion.score.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
	 * @param flag 对象值是否要转换，0：不转换，1：转换
	 * @param Map<String,String[]> maps 属性映射值
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes"})
	public void exportExcel(String title, String[] headers,
            Collection<T> dataset, OutputStream out, String pattern,String flag,Map<String,String[]> maps) throws Exception {
		
	    //String class_path = this.getClass().getResource("").getPath(); 
		//Workbook workbook = WorkbookFactory.create(is); //这种方式 Excel 2003/2007/2010 都是可以处理的  
		//PropertyConfigurator.configure(class_path+"log4j.properties");//获取 log4j 配置文件 
		SXSSFWorkbook workbook = new SXSSFWorkbook(1000);//内存中保留 100 条数据，以免内存溢出，其余写入 硬盘
		
        // 声明一个工作薄
		//XSSFWorkbook workbook = new XSSFWorkbook();  //2007
		setExcelStyle(workbook);//执行样式初始化
        // 生成一个表格
		//XSSFSheet sheet = workbook.createSheet(title);
		SXSSFSheet sheet =  (SXSSFSheet) workbook.createSheet(title);
        //HSSFSheet sheet = workbook.createSheet(title);   //2003
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 15);
        // 生成一个样式
        // HSSFCellStyle style = workbook.createCellStyle();
        // XSSFCellStyle style = workbook.createCellStyle();
        CellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        // XSSFFont font = workbook.createFont();
        Font font = workbook.createFont();
        // HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        // XSSFCellStyle style2 = workbook.createCellStyle();
        CellStyle style2 = workbook.createCellStyle();
        //HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        //XSSFFont font2 = workbook.createFont();
        Font font2 = workbook.createFont();
        font2.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);
        
        // 产生表格标题行
        //XSSFRow row = sheet.createRow(0);
        Row row = sheet.createRow(0);
        for (short i = 0; i < headers.length; i++) {
            //XSSFCell cell = row.createCell(i);
        	Cell cell = row.createCell(i);
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
            String simpleName =  t.getClass().getSimpleName();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();
            for (short i = 0; i < headers.length; i++) {
            	//XSSFCell cell = row.createCell(i);
            	Cell cell = row.createCell(i);
            	cell.setCellStyle(style2);
            	String textValue = null;
            	if("HashMap".equals(simpleName)){
            		HashMap<String, Object> map = (HashMap<String, Object>)t;
            		String key = headers[i].split(":")[1];
            		textValue = map.get(key)==null?"":map.get(key).toString();
            	}else{//Model 对象
        			Field field = fields[i];
            		String fieldName = field.getName();
            		String getMethodName = "get"
            				+ fieldName.substring(0, 1).toUpperCase()
            				+ fieldName.substring(1);
            		Class tCls = t.getClass();
            		Method getMethod = tCls.getMethod(getMethodName,
            				new Class[] {});
            		Object value = getMethod.invoke(t, new Object[] {});
            		if (value instanceof Date) {
            			Date date = (Date) value;
            			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            			textValue = sdf.format(date);
            		}else{
            			// 其它数据类型都当作字符串简单处理
            			if(null!=value){
            				textValue = value.toString();
            			}
            		}
            	}
            	//表示需要值类型转换 如：数据库存储的是0：页面显示：无 ，导出转换成 无
        		if(null!=maps){
        			Iterator<Entry<String, String[]>> its = maps.entrySet().iterator();
        			while(its.hasNext()){
        				Entry<String, String[]>  entry = its.next();
        				String [] values =  entry.getValue();
        				String key = entry.getKey();
        				if(key.equals( headers[i].split(":")[1])){
        					textValue = values[Integer.parseInt(textValue)];
        				}
        			}
        		}
                if (textValue != null) {
                    XSSFRichTextString richString = new XSSFRichTextString(textValue);
                  /*  HSSFFont font3 = workbook.createFont();
                    font3.setColor(HSSFColor.BLUE.index);
                    richString.applyFont(font3);*/
                    cell.setCellValue(richString);
                }
            }

        }
		workbook.write(out);
		workbook.close();
    }
	
	 private void setExcelStyle(SXSSFWorkbook workbook) {
		
	}

	/**
	  * @param is
	  * @param length cell 列 获取值
	  * @param checkName 检查项目
	  * @return
	  * @throws IOException  
	  */
	@SuppressWarnings("resource")
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

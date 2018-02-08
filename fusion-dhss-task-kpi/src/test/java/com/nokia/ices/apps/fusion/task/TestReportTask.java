package com.nokia.ices.apps.fusion.task;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class TestReportTask {

    @Test
    public void test() {
        XSSFWorkbook template = null;
        try {
            template = new XSSFWorkbook("d:\\ImportExcel.xlsx");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }// 读取模板
        
        SXSSFWorkbook outPutFile = new SXSSFWorkbook(template);// 设置输出文件位置
        Map<String,List<ListOrderedMap>> kpiValueMap = new HashMap<String,List<ListOrderedMap>>();
        
        List<ListOrderedMap> list = new ArrayList<ListOrderedMap>();
        for (int i = 0; i < 10; i++) {
            ListOrderedMap map = new ListOrderedMap();
            map.put("id", i*10+Math.random()*5);
            map.put("name", "name"+i);
            map.put("value", i*i);
            map.put("period_start_time", new Date());
            list.add(map);
        }
        
        kpiValueMap.put("鉴权", list);
        kpiValueMap.put("短信", list);
        kpiValueMap.put("呼叫", list);
        outPutFile.setCompressTempFiles(true);// 压缩缓存
        for (Sheet sheet : outPutFile) {
            String kpiName = sheet.getSheetName();
            List<ListOrderedMap> kpiValueList = kpiValueMap.get(kpiName);
            int startRow = 2;
            for (ListOrderedMap map : kpiValueList) {
                Row row = sheet.createRow(startRow);
                List<String> colList = map.keyList();
                int startColumn = 0;
                for (String colName : colList) {
                    Cell cell = row.createCell(startColumn);
                    Object cellValue = map.get(colName);
                    fillCellWithValue(cell, cellValue);
                    startColumn++;
                }
                startRow++;
            }
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("d:\\ExportExcel.xlsx");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            outPutFile.write(out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // dispose of temporary files backing this workbook on disk
        outPutFile.dispose();
    
        
    }

    private  void fillCellWithValue(Cell cell, Object cellValue) {
        if (cellValue instanceof Date)
            cell.setCellValue((Date) cellValue);
        else if (cellValue instanceof Number)
            cell.setCellValue( ((Number) cellValue).doubleValue());
        else if (cellValue instanceof String)
            cell.setCellValue((String) cellValue);
        else
            cell.setCellValue(cellValue.toString());
    }
}

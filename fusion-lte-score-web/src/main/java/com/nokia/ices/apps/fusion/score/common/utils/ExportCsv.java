package com.nokia.ices.apps.fusion.score.common.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokia.ices.apps.fusion.score.domain.IndexScoringRule;

public class ExportCsv {
	
	public static void main(String[] args) {
		String ss = "2016-05";
		System.out.println(ss.lastIndexOf("-"));
	}
	
	private final static Logger logger = LoggerFactory.getLogger(ExportCsv.class);
	
	public void createScoreCSVFile(List<Map<String,Object>> exportData,
			Object[] showColumnArray, Object[] keyArray,
			BufferedWriter csvFileOutputStream,
			List<IndexScoringRule> indexList) throws IOException {
		if (showColumnArray.length != keyArray.length) {
			logger.error(
					"Export error: show field length is not "
							+ " consistent with the length of the table.should be {?} but {?}",
					showColumnArray.length, keyArray.length);
			throw new RuntimeException(
					"Export error: show field length is not "
							+ " consistent with the length of the table.");
		} else {

			// 写入文件头部
			for (int i = 0; i < showColumnArray.length; i++) {
				csvFileOutputStream.write("\"" + showColumnArray[i].toString()+ "\"");
				// 最后一个不写逗号
				if (i != showColumnArray.length - 1) {
					csvFileOutputStream.write(",");
				}
			}
			csvFileOutputStream.newLine();
			// 写入文件内容
			for (Map<String,Object> m : exportData) {
				for (int i = 0; i < keyArray.length; i++) {
					
					String key = keyArray[i]!=null?keyArray[i].toString():"";
					
					String s   = "";
					if("kpiAll".equalsIgnoreCase(key)){
						String total_score = m.get("total_score").toString();
							if(!"100".equals(total_score)){
								//故障原因参数转换使用
								for (IndexScoringRule isr : indexList) {
									String  key_score = isr.getKpiId().toLowerCase() + "_score";
									String score =  m.get(key_score)!=null?m.get(key_score).toString():"";
									if(!"0".equals(score)){
										s+=","+isr.getCnName();
									}
								}
								s  = s.substring(1, s.length());
							}
					}else{
						//根据属性字段获取对应值
					     s  = m.get(key)!=null?m.get(key).toString():"";
					}
					csvFileOutputStream.write("\"" + s + "\"");
					// 最后一个不写逗号
					if (i != keyArray.length - 1) {
						csvFileOutputStream.write(",");
					}
					if (i % 5000 == 0) {
						csvFileOutputStream.flush();
					}
				}
				csvFileOutputStream.newLine();
			}
			csvFileOutputStream.flush();

		}
	}
	
	/**
	 * 
	 * @param exportData
	 *            数据
	 * @param showColumnArray
	 *            表头
	 * @param keyArray
	 *            表头对应的表字段
	 * @param csvFileOutputStream
	 *            数据存放的文件流
	 * @param mapList
	 *            值转换，例如：数据库字段flag有两种值0和1，导出时希望写为正常、异常， 则传值时处理为：Map<String,
	 *            String> flagMap = new HashMap(); flagMap.put("0", "正常");
	 *            flagMap.put("1", "异常"); mapList.put("flag", flagMap);
	 *            如果有多个字段都需要转义，则可以存多个，mapList中的key需要与keyArray中对应
	 * 
	 * @return
	 * @throws IOException
	 */
	public void createCSVFile(List<Map<String,Object>> exportData,
			Object[] showColumnArray, Object[] keyArray,
			BufferedWriter csvFileOutputStream,
			Map<String, Map<String, String>> mapList) throws IOException {
		if (showColumnArray.length != keyArray.length) {
			logger.error(
					"Export error: show field length is not "
							+ " consistent with the length of the table.should be {?} but {?}",
					showColumnArray.length, keyArray.length);
			throw new RuntimeException(
					"Export error: show field length is not "
							+ " consistent with the length of the table.");
		} else {

			// 写入文件头部
			for (int i = 0; i < showColumnArray.length; i++) {
				csvFileOutputStream.write("\"" + showColumnArray[i].toString()
						+ "\"");
				// 最后一个不写逗号
				if (i != showColumnArray.length - 1) {
					csvFileOutputStream.write(",");
				}
			}
			csvFileOutputStream.newLine();
			// 写入文件内容
			for (Map m : exportData) {
				for (int i = 0; i < keyArray.length; i++) {
					String s = "";
					if (null != mapList) {
						if (null != mapList.get(keyArray[i])) {
							Map temp = mapList.get(keyArray[i]);
							if (temp.containsKey((String) m.get(keyArray[i]))) {
								s = (String) temp.get((String) m
										.get(keyArray[i]));
							}
							else {
								s = m.get(keyArray[i])!=null?m.get(keyArray[i]).toString():"";
							}

						} else {
							String key = keyArray[i]!=null?keyArray[i].toString():"";
							String value = m.get(key)!=null?m.get(key).toString():"";
							if("period_date".equals(key) && value.lastIndexOf("-")<5){
								s= value.split("-")[0]+"年"+value.split("-")[1]+"月";
							}else{
								s = value;
							}
						}

					} else {
						s = m.get(keyArray[i])!=null?m.get(keyArray[i]).toString():"";

					}

					csvFileOutputStream.write("\"" + s + "\"");
					// 最后一个不写逗号
					if (i != keyArray.length - 1) {
						csvFileOutputStream.write(",");
					}
					if (i % 5000 == 0) {
						csvFileOutputStream.flush();
					}
				}
				csvFileOutputStream.newLine();
			}
			csvFileOutputStream.flush();

		}
	}
}

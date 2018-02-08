package com.nokia.ices.apps.fusion.log.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nokia.ices.apps.fusion.log.common.utils.ExportExcel;
import com.nokia.ices.apps.fusion.log.common.utils.FileOperateUtil;
import com.nokia.ices.apps.fusion.log.domain.HistoryData;
import com.nokia.ices.apps.fusion.log.service.SystemLogService;


@RequestMapping("/systemLog")
@RestController
public class SystemLogController {

	public static final Logger logger = LoggerFactory.getLogger(SystemLogController.class);
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	@Autowired
	SystemLogService systemLogService;
	
    /**
     * 历史数据
     * @param iDisplayStart
     * @param iDisplayLength
     * @param userName
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "query/historyData")
	public Map<String,Object> historyData(@RequestParam("start") Integer iDisplayStart, 
			@RequestParam("length") Integer iDisplayLength,@RequestParam("userName") String userName,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
    	
    	List<String> sortSet = new ArrayList<String>();
    	Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("userName", userName);
		searchParams.put("startDate", startDate);
		searchParams.put("endDate", endDate);
		sortSet.add("logDate,desc");
		iDisplayStart= iDisplayStart/iDisplayLength+1;  //分页
		Map<String, Object> resultData = new HashMap<String, Object>();
		try{
			Page<HistoryData> page =systemLogService.findhistoryDataPageBySearch(searchParams, null, iDisplayStart, iDisplayLength, sortSet);
			resultData.put("data", page.getContent());
			resultData.put("iTotalRecords", page.getTotalElements());
			resultData.put("iTotalDisplayRecords", page.getTotalElements());
		}catch(Exception e){
    		logger.debug(" query historyData is error:"+e.getMessage());
    	}
		return resultData;
	} 
    
    @RequestMapping(value = "query/condition")
	public List<HistoryData> getHistoryDayData(@RequestParam("dateTime") String dateTime,
			@RequestParam(value="startDate",required=false) String startDate,
			@RequestParam(value="endDate",required=false) String endDate,@RequestParam("userName") String userName) {
    	List<HistoryData> listAll =null;
    	Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("logDate", dateTime);
		searchParams.put("startDate", startDate);
		searchParams.put("endDate", endDate);
		searchParams.put("userName", userName);
		try{
			listAll= systemLogService.findHistoryDataListByCreator(searchParams);
		}catch(Exception e){
    		logger.debug(" query logData is error:"+e.getMessage());
    	}
		return listAll;
	} 
    
    /**
     * 保存
     * @param historyData
     * @return
     */
    @RequestMapping(value = "saveOrEditLog")
    public String saveLog(@RequestBody HistoryData []  historyData){
    	
    	String result = "false";
    	try{
    		for(HistoryData his : historyData){
    			String id = his.getId()!=null?his.getId() .toString():"";
    			his.setStatus(true);
    			if(StringUtils.isNotEmpty(id)){
    				his.setId(Long.parseLong(id));
    				systemLogService.updateHistoryDataName(his);
    			}else{
    				systemLogService.addHistoryData(his);
    			}
    		}
    		result = "true";
    	}catch(Exception e){
    		logger.debug("save logData is error:"+e.getMessage());
    	}
    	return result;
    }
    
    /**
     * 删除
     * @param ids
     * @return
     */
    @RequestMapping(value = "deleteLog/{ids}")
    public Long deleteLog(@PathVariable String ids){
    	
    	try{
    		String [] arrIds  = ids.split(",");
    		for(String id: arrIds){
    			systemLogService.deleteByIdLogData(Long.parseLong(id));
    		}
    		return 0L;
    	}catch(Exception e){
    		logger.debug("delete logData is error:"+e.getMessage());
    	}
    	return 1L;
    }
    
    // 导出
 	@RequestMapping(value = "/exportFile")
 	public void exportFile(@RequestParam("userName") String userName,
			@RequestParam(value="startDate") String startDate,@RequestParam(value="endDate") String endDate,
			HttpServletRequest request, HttpServletResponse response) {
 		
    	List<HistoryData> listAll =null;
    	Map<String, Object> searchParams = new HashMap<String, Object>();
    	
		searchParams.put("userName", userName);
		searchParams.put("startDate", startDate);
		searchParams.put("endDate", endDate);
		
		ExportExcel<HistoryData> ex = new ExportExcel<HistoryData>();
		String[] headers = { "姓名:userName", "时间:logDate", "产品:product","模块:modular","项目:projectName",
				"阶段:stage","工作包类型:workPackageType","工作包:workPackage", "工时数:time","备注:jobOperator"};
		String historyPath = FileOperateUtil.getPropertieValue("system.log.excel.path");
		String fileName = "历史记录-" + sdf.format(new Date()) + ".xlsx";
		OutputStream out = null;
		File fielDir = null;
		// 保存到本地xls
		try {
			listAll= systemLogService.exportData(searchParams);
			File operationDir = new File(historyPath);
			fielDir = new File(historyPath + fileName);
			if (!operationDir.exists() && !operationDir.isDirectory()) {
				operationDir.mkdir();
			}
			if (!fielDir.exists()) {
				fielDir.createNewFile();
			}
			out = new FileOutputStream(fielDir);
			ex.exportExcel("历史记录导出", headers, listAll, out, "yyyy-MM-dd", "0", null);

			// 导出excel
			FileOperateUtil.download(request, response, historyPath + fileName, "application/octet-stream", fileName);

		} catch (Exception e) {
			logger.debug(" query logData is error:"+e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (null != out) {
				try {
					out.close();
					fielDir.delete();// 删除文件
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
				out = null;
			}
		}
	} 
 	
}

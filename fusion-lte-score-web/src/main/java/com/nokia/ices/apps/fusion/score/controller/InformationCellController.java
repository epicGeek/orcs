package com.nokia.ices.apps.fusion.score.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.score.common.utils.ExportColumns;
import com.nokia.ices.apps.fusion.score.common.utils.ExportCsv;
import com.nokia.ices.apps.fusion.score.common.utils.FileOperateUtil;
import com.nokia.ices.apps.fusion.score.common.utils.ZipCompressUtil;
import com.nokia.ices.apps.fusion.score.service.AreaService;
import com.nokia.ices.apps.fusion.score.service.BtsInformationCellService;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;

//基站基础信息

@RequestMapping("/cell")
@RestController
public class InformationCellController {

	public static final Logger logger = LoggerFactory.getLogger(InformationCellController.class);
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
	
	@Autowired
	AreaService areaService;
	
	@Autowired
	BtsInformationCellService btsService;
	
    @RequestMapping(value = "/search")
    public Map<String,Object> getInformationList(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize,
											    @RequestParam("neCode") String neCode,
											    @RequestParam("areaCode")String areaCode,
											    @RequestParam("cityCode")String cityCode) {
    	// 获取有权限的地区
  		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
  		areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
        Map<String,Object> searchParams = new HashMap<String,Object>();
        searchParams.put("page", (page-1)*pageSize);
        searchParams.put("pageSize", pageSize);
        searchParams.put("neCode", neCode);
        searchParams.put("areaCode", areaCode);
        searchParams.put("cityCode", cityCode);
        
       return  btsService.findBtsAll(searchParams);
        
    }
    
 // 基站基础信息导出
 	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "/exportFile")
 	public void saveSession(HttpServletRequest request, HttpServletResponse response) throws ParseException {
 		response.setHeader("Pragma", "No-cache");
 		response.setHeader("Cache-Control", "no-cache");
 		response.setDateHeader("Expires", 0);
 		Map<String, String[]> parameterMap = request.getParameterMap();

 		String cityCode = null == parameterMap.get("cityCode") ? "" : parameterMap.get("cityCode")[0];
 		String areaCode = null == parameterMap.get("areaCode") ? "" : parameterMap.get("areaCode")[0];
 		String neCode = null == parameterMap.get("neCode") ? "" : parameterMap.get("neCode")[0];
 		
 		Map<String, Object> searchParams = new HashMap<String, Object>();
 		searchParams.put("cityCode", cityCode);// 地市名称
 		searchParams.put("areaCode", areaCode); // 区县
 		searchParams.put("neCode", neCode); // 基站ID
 		
 		
 		String[]  keyArray = ExportColumns.ICES_CEll;
 		String[]  showColumnArray = ExportColumns.ICES_CEll_HEADER;
 		
 		ExportCsv ec = new ExportCsv();
 		File csvFile = null;
 		BufferedWriter csvFileOutputStream = null;
 		String cellPath = FileOperateUtil.getPropertieValue("iceslte.kpi.file.path");
 		String fileName = "基础基站信息"+sdf.format(new Date());
 		String path = cellPath + fileName + ".csv";
 		String zipPath = cellPath + fileName + ".zip";
 		File zipFile = null;
 		String zipUrl = zipPath;
 		try {
 			File operationDir = new File(cellPath);
 			csvFile = new File(path);
 			zipFile = new File(zipPath);
 			if (!operationDir.exists() && !operationDir.isDirectory()) {
 				operationDir.mkdir();
 			}

 			if (!zipFile.exists()) {
 				Map<String, Object> map = btsService.findBtsAll(searchParams);	
 				List<Map<String, Object>> cellList = (List<Map<String, Object>>) map.get("content");
 				// 生成文件
 				csvFile.createNewFile();
 				csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 2048);
 				ec.createScoreCSVFile(cellList, showColumnArray, keyArray, csvFileOutputStream,null);
 				// 压缩
 				zipUrl = ZipCompressUtil.compress(new String[] { path }, cellPath, fileName + ".zip");
 				csvFileOutputStream.close();
 				csvFile.delete();
 			}

 			// 下载
 			FileOperateUtil.download(response, zipUrl, "" + zipFile.length(), fileName + ".zip");
 		} catch (Exception e) {
 			e.printStackTrace();
 			logger.error("An error occurred while export method:exportBreakReason,error message:{}", e.getMessage());
 		} finally {
 			zipFile.delete();
 			try {
 				if (csvFileOutputStream != null) {
 					csvFileOutputStream.close();
 				}
 			} catch (IOException e) {
 				logger.error("An error occurred while invoke method:exportBreakReason,error message:{}", e.getMessage());
 				e.printStackTrace();
 			}
 			csvFileOutputStream = null;

 		}
 	}

}

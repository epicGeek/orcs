package com.nokia.ices.apps.fusion.userdata.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.userdata.domain.UserBacthLog;
import com.nokia.ices.apps.fusion.userdata.domain.UserDataLog;
import com.nokia.ices.apps.fusion.userdata.service.BacthUserDataService;
import com.nokia.ices.apps.fusion.userdata.service.UserDataService;
import com.nokia.ices.core.utils.ExcelUtil;
import com.nokia.ices.core.utils.ModuleDownLoadNameDefinition;


@RequestMapping("/userData")
@RestController
public class UserDataController {

    private final static Logger logger = LoggerFactory.getLogger(UserDataController.class);

    @Autowired
    private UserDataService userDataService;
    
    @Autowired
    BacthUserDataService bacthUserDataService;
    
    
    /**
	 * 批量用户查询处理
	 * @param value
	 * @return
	 */
	@RequestMapping(value = "/getBatchUserData/{value}/all", method = RequestMethod.GET)
	public long getBatchUserData(@PathVariable String value){
		
		Long isSuccess  = null;
		try {
			
			String[] numbers = value.split(",");
			isSuccess = bacthUserDataService.addBacthUserData(numbers);
    	} catch (Exception e) {
    		logger.error(e.getMessage());
    	}
		return isSuccess;
	}
	
	/**
	 * excel文件上传
	 * @param file 文件
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/uploadFile")
	public String uploadFile(@RequestParam("fileName") MultipartFile file){
		Long isSuccess  = null;
		try{
			   
			if(!file.isEmpty()){
				List<String> lists = ExcelUtil.readXls(file.getInputStream());
				//保存数据
				String [] numbers =  lists.toArray(new String[lists.size()]);
				
				isSuccess = bacthUserDataService.addBacthUserData(numbers);
			}
		}catch (Exception e) {
    		logger.error(e.getMessage());
		}
		
		return isSuccess+"";
		
	}
	
	
	@RequestMapping(value = "/getUnit", method = RequestMethod.GET)
	public EquipmentUnit getUnit(@RequestParam("unitName") String unitName){
		try {
			return userDataService.findEquipmentUnitByUnitName(unitName);
    	} catch (Exception e) {
    		logger.error(e.getMessage());
    	}
		return null;
	}
	
	
	@RequestMapping(value = "/downUserDataTemplate")
	public void downUserData(HttpServletRequest request,
			HttpServletResponse response){
		try{
			    Resource  resource = new ClassPathResource("/number/template/bacthTemplate.xls");
			    String filePath = resource.getURI().getPath();
				download(request, response, filePath, "application/octet-stream","bacthTemplate.xls");
		}catch (Exception e) {
    		logger.error(e.getMessage());
		}
		
	}
	
    
    @RequestMapping(value = "/searchLog", method = RequestMethod.GET)
    public Page<UserDataLog> search(@RequestParam("searchField") String searchField,
			@RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime,Pageable pageable) {
    	Page<UserDataLog> page = null;
    	Map<String, Object> searchParams = new HashMap<String, Object>();
        try {
    		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    		searchParams.put("searchField", searchField);
    		searchParams.put("startTime", startTime);
    		searchParams.put("endTime", endTime);
    		page = userDataService.findUserDataPageBySearch(searchParams, shiroUser,pageable);
        } catch (Exception e) {
            logger.error(e.toString());
        }
		return page;
    }
    
    @RequestMapping(value = "/bacthLog", method = RequestMethod.GET)
    public Page<UserBacthLog> bacthLog(@RequestParam("searchField") String searchField,Pageable pageable) {
    	Page<UserBacthLog> page = null;
    	Map<String, Object> searchParams = new HashMap<String, Object>();
        try {
    		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    		searchParams.put("searchField", searchField);
    		page = bacthUserDataService.findUserBacthLogPageBySearch(searchParams, shiroUser, pageable);
        } catch (Exception e) {
        	  logger.error(e.getMessage());
              throw new RuntimeException(e);
        }
		return page;
    }
    

    @RequestMapping(value = "/getUserData/{type}/{imsiMsisdn}/all/{isAdvance}", method = RequestMethod.GET)
    public Map<String, Object> getUserData(@PathVariable String type,
						    		@PathVariable String imsiMsisdn,
						    		@PathVariable Boolean isAdvance) {

        Map<String, Object> soapJson = null;
        try {
             
        	if(imsiMsisdn.length()>13){
        		type = "2";
            }else{
            	type = "1";
            	//验证是否以“86”开头
    			if(!imsiMsisdn.matches("^86.*")){
    				imsiMsisdn = "86"+imsiMsisdn;
    			}
            }
            soapJson = userDataService.saveUserData(type, imsiMsisdn,isAdvance);
            // System.out.println("json="+new JsonMapper().toJson(soapJson));
            if (null == soapJson) {
                return new HashMap<String, Object>();
            }
            return soapJson;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    @RequestMapping(value = "/downxml")
    public void downloadLog(@RequestParam("filePath") String filePath, HttpServletRequest request,
            HttpServletResponse response) {

        String fileLogPath = null;
        try {
        	
        	if(filePath.contains("bacthUser")){
        		fileLogPath = ProjectProperties.getLogBasePath()+ File.separator+ModuleDownLoadNameDefinition.DOWNLOAD_BACTHUSER+File.separator;
        	}else{
        		fileLogPath = ProjectProperties.getLogBasePath()+ File.separator+ModuleDownLoadNameDefinition.DOWNLOAD_SOAPXML+File.separator;
        	}
            if (filePath != null && !"".equals(filePath)) {
                download(request, response, fileLogPath + filePath, "application/octet-stream",filePath);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }
    
    /**
     * 下载日志
     * @param request
     * @param response
     * @param downLoadPath
     * @param contentType
     * @param realName
     * @throws Exception
     */
    public static void download(HttpServletRequest request,HttpServletResponse response, String downLoadPath,
			String contentType, String realName) throws Exception {
		request.setCharacterEncoding("UTF-8");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		File file = new File(downLoadPath);
		response.setContentType(contentType);
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String(realName.getBytes("utf-8"), "ISO8859-1"));
		response.setHeader("Content-Length", String.valueOf(file.length()));
		bis = new BufferedInputStream(new FileInputStream(file));
		bos = new BufferedOutputStream(response.getOutputStream());
		byte[] buff = new byte[2048];
		int bytesRead;
		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
		bis.close();
		bos.close();
	}

}

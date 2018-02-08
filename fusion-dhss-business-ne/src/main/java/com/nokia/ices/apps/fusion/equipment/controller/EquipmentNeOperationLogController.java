package com.nokia.ices.apps.fusion.equipment.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNeOperationLog;
import com.nokia.ices.apps.fusion.equipment.service.EquipmentNeOperationService;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.core.utils.DateUtil;
import com.nokia.ices.core.utils.ModuleDownLoadNameDefinition;

@RequestMapping("/ne_log")
@RestController
public class EquipmentNeOperationLogController {

//    private final static Logger logger = LoggerFactory.getLogger(EquipmentNeOperationLogController.class); 
    
     @Autowired
     EquipmentNeOperationService neOperationService;
    
    @RequestMapping(value = "/search")
    public Page<EquipmentNeOperationLog> getNeLogList(
            @RequestParam("searchField") String searchField,
            @RequestParam("neStratTime") String neStratTime,
            @RequestParam("neEndTime") String neEndTime,
            @RequestParam("neType") String neType,
            @RequestParam("unitType") String unitType,Pageable pageable) {
        
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
//        List<String> sortSet = new ArrayList<String>();
        Date [] dates = DateUtil.getStartAndTimeDate(neStratTime, neEndTime);
        Map<String,Object> searchParams = new HashMap<String,Object>();
        searchParams.put("searchField", searchField);
        searchParams.put("neStratTime",dates[0]);
        searchParams.put("neEndTime", dates[1]);
        searchParams.put("neType", neType);
        searchParams.put("unitType", unitType);
        
        Page<EquipmentNeOperationLog> page = neOperationService.findNeLogPageBySearch(searchParams,shiroUser,pageable);
//        logger.debug(new JsonMapper().toJson(page));
        return page;
    }
    
    @RequestMapping(value = "/downLog")
    public void saveSession(@RequestParam("unitNamelog") String unitNamelog, HttpServletRequest request,  
               HttpServletResponse response){
        
        String neLogPath = null;
          try{
        	  neLogPath  = ProjectProperties.getLogBasePath() + File.separator +ModuleDownLoadNameDefinition.DOWNLOAD_NELOG+File.separator;
              if(unitNamelog!=null && !"".equals(unitNamelog)){
                  String fileName =unitNamelog.substring(unitNamelog.lastIndexOf("/")+1,unitNamelog.length());
                  download(request, response, neLogPath+fileName, "application/octet-stream",fileName);
              }
              
          }catch (Exception e) {
              e.printStackTrace();
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

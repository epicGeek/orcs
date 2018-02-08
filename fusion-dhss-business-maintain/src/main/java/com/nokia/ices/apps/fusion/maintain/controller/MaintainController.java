package com.nokia.ices.apps.fusion.maintain.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.command.domain.CommandCheckItem;
import com.nokia.ices.apps.fusion.command.repository.CommandCheckItemRepository;
import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.jms.producer.DHSSMessageProducer;
import com.nokia.ices.apps.fusion.maintain.consumer.SecurityConsumer;
import com.nokia.ices.apps.fusion.maintain.domain.MaintainOperation;
import com.nokia.ices.apps.fusion.maintain.domain.MaintainResult;
import com.nokia.ices.apps.fusion.maintain.repository.MaintainOperationRepository;
import com.nokia.ices.apps.fusion.maintain.service.MaintainService;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.system.repository.types.SystemOperationLogOpType;
import com.nokia.ices.apps.fusion.system.service.SystemService;
import com.nokia.ices.core.utils.JsonMapper;


@RepositoryRestController
@RestController
public class MaintainController {
    public static final Logger logger = LoggerFactory.getLogger(MaintainController.class);

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    @SuppressWarnings("rawtypes")
    @Autowired
    PagedResourcesAssembler pagedResourcesAssembler;

    @Autowired
    private MaintainService maintainService;
    
    @Autowired
    private SystemService systemService;
    
    @Autowired
    SecurityConsumer SecurityConsumer;


    @Autowired
    private MaintainOperationRepository maintainOperationRepository;

    @Autowired
    private CommandCheckItemRepository commandCheckItemRepository;
    
    @Autowired
    DHSSMessageProducer messageProducer;
    
	@RequestMapping(value = "/maintain-operation/sendPassword",method = RequestMethod.POST,  produces=MediaType.APPLICATION_JSON_VALUE)
    public String sendUnitPassword(@RequestBody ModelMap paramMap){
    	ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		String userName = shiroUser.getUserName();
		logger.debug(new JsonMapper().toJson(paramMap));
		maintainService.sendChangePasswordCommandViaJcomp(paramMap, userName);
//		SecurityConsumer.persist("646b16988efb4bc7a08b52ff92a9db46", "0", "1222222222222");
    	return "";
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/maintain-result/pageinfo")
    public PagedResources<MaintainOperation> getMaintainOperationList(
            @RequestParam(value = "unitType", required = false) String unitType,
            @RequestParam(value = "neType", required = false) String neType,
            @RequestParam(value = "category", required = false) String categoryName,
            @RequestParam(value = "checkName", required = false) String checkName,Pageable pageable,
            PersistentEntityResourceAssembler assembler) {

        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        logger.debug("&&&&&&&&&&&&&:" + shiroUser.getUserId());
        Page<MaintainOperation> page =
                maintainService.findMaintainOperationPageBySearch(checkName,shiroUser.getUserName(), categoryName, pageable);
        /*
         * logger.debug(new JsonMapper().toJson(list)); Page<MaintainResult> page =
         * maintainService.findMaintainResultPageBySearch(list, pageable); logger.debug(new
         * JsonMapper().toJson(page));
         */
        return pagedResourcesAssembler.toResource(page, assembler);
    }

    @SuppressWarnings({})
    @RequestMapping(value = "/maintain-operation/sendCommand", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String sendCommand(@RequestBody ModelMap paramMap) {
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String userName = shiroUser.getUserName();
        logger.debug(new JsonMapper().toJson(paramMap));
        maintainService.sendCommandViaJcomp(paramMap, userName);
        return "";
    }
    
    
    @RequestMapping(value = "/maintainOperateion/showloadLog")
    public String showloadLog(@RequestParam("id") Long operationId, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	String logText = "";
        MaintainOperation maintainOperation = maintainOperationRepository.findOne(operationId);
        String compBasePath = ProjectProperties.getCOMP_BASE_PATH();
        
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        systemService.saveSystemOperationLog(shiroUser, "日常运维", "查看 "+maintainOperation.getCheckName() +" 执行结果", SystemOperationLogOpType.View);
        logger.debug("compBasePath:"+compBasePath);
        File operationDir = new File(compBasePath);
        if (!operationDir.exists() || !operationDir.isDirectory()) {
            operationDir.mkdirs();
        }
                for (MaintainResult result : maintainOperation.getResult()) {
                	
                	CommandCheckItem item = new CommandCheckItem();
                	logger.info("RESULT::::::"+new JsonMapper().toJson(result));
                	if(result.getCommandCheckItem() != null){
                		 item = commandCheckItemRepository.findOne(result.getCommandCheckItem().getId());
                	}else{
                		item.setName("修改网元密码");
                	}
                	logger.info("ITEM::::::"+new JsonMapper().toJson(item));
                	logText += createLogTitle(result,item);
                	logText += "&nbsp;</br>";
                	logText += "&nbsp;</br>";
                    if (result.getReportPath() != null && !"".equals(result.getReportPath())) {
                        String resultLog = compBasePath + result.getReportPath();
                        logger.debug("resultLog:"+resultLog);
                        BufferedReader reader = null;
                        File files = new File(resultLog);
                        try {
                            reader = new BufferedReader(new FileReader(files));
                            String len = "";
                            while ((len = reader.readLine()) != null) {
                            	logText +=  len + "&nbsp;</br>";
                            }

                            logText += "&nbsp;</br>";
                            logText += "----------------------------------------------------------------------";
                            logText += "&nbsp;</br>";
                        }catch(Exception e){
                        	logText += "&nbsp;</br>";
                        }finally {
                            if (null != reader) {
                                reader.close();
                            }
                        }
                    } else {
                    	logText += result.getErrorLog();
                    	logText += "&nbsp;</br>";
                    	logText += "----------------------------------------------------------------------";
                        logText += "&nbsp;</br>";
                    }

                }
                return logText;
    }

    @RequestMapping(value = "/maintainOperateion/downloadLog")
    public void downloadLog(@RequestParam("id") Long operationId, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
//        String userName = shiroUser.getUserName();
        MaintainOperation maintainOperation = maintainOperationRepository.findOne(operationId);
        String compBasePath = ProjectProperties.getCOMP_BASE_PATH();
        //String logBasePath = ProjectProperties.getLOG_BASE_PATH();
        logger.debug("compBasePath:"+compBasePath);
        String operationLogName = shiroUser.getUserName() + "-" + sdf.format(maintainOperation.getRequestTime()) + "-"
                + maintainOperation.getCheckName() + ".txt";
        logger.debug("operationLogName:"+operationLogName);
        String operationLogPath = compBasePath + File.separator + operationLogName;
        systemService.saveSystemOperationLog(shiroUser, "日常运维", "下载"+maintainOperation.getCheckName() +" 执行结果", SystemOperationLogOpType.View);
        logger.debug("operationLogPath:"+operationLogPath);
        File operationDir = new File(compBasePath);
        if (!operationDir.exists() || !operationDir.isDirectory()) {
            operationDir.mkdirs();
        }
        // 若汇总日志文件 已存在，则不再进行汇总
        File operationLogFile = new File(operationLogPath);
        if (operationLogFile.exists() && operationLogFile.isFile()) {
            // do nothing
        } else {
            OutputStream writer = null;
            try {
                writer = new FileOutputStream(operationLogFile);
                for (MaintainResult result : maintainOperation.getResult()) {
                	
                	CommandCheckItem item = new CommandCheckItem();
                	if(result.getCommandCheckItem() != null){
                		 item = commandCheckItemRepository.findOne(result.getCommandCheckItem().getId());
                	}else{
                		item.setName("修改网元密码");
                	}
                	
                	
                    writer.write(createLogTitle(result,item).getBytes());
                    writer.write("\r\n".getBytes());
                    writer.write("\r\n".getBytes());

                    // 若检查结果成功，则读取检查日志
                    // 若检查结果失败，则直接读取错误信息

                    if (result.getReportPath() != null && !"".equals(result.getReportPath())) {

                    	
                        String resultLog = compBasePath + result.getReportPath();
                        logger.debug("resultLog:"+resultLog);
                        InputStream reader = null;
                        try {
                            reader = new FileInputStream(resultLog);
                            byte[] b = new byte[1024];
                            int len = 0;
                            while ((len = reader.read(b)) != -1) {
                                writer.write(b, 0, len);
                            }

                            writer.write("\r\n".getBytes());
                            writer.write("----------------------------------------------------------------------"
                                    .getBytes());
                            writer.write("\r\n".getBytes());
                        } finally {
                            if (null != reader) {
                                reader.close();
                            }
                        }
                    } else {
                        writer.write(result.getErrorLog().getBytes());
                        writer.write("\r\n".getBytes());
                        writer.write(
                                "----------------------------------------------------------------------".getBytes());
                        writer.write("\r\n".getBytes());
                    }

                    writer.flush();
                }
            } finally {
                if (null != writer) {
                    writer.close();
                }
            }
        }
        // 下载日志
        request.setCharacterEncoding("UTF-8");
        InputStream is = null;
        OutputStream os = null;

        try {
            long fileLength = operationLogFile.length();

            response.setContentType("application/octet-stream");

            // 如果客户端为IE
            // System.out.println(request.getHeader("User-Agent"));
            if (request.getHeader("User-Agent").indexOf("Trident") != -1) {
                operationLogName = java.net.URLEncoder.encode(operationLogName, "UTF-8");
            } else {
                operationLogName = new String(operationLogName.getBytes("UTF-8"), "iso-8859-1");
            }

            response.setHeader("Content-disposition", "attachment; filename=" + operationLogName);
            response.setHeader("Content-Length", String.valueOf(fileLength));

            is = new FileInputStream(operationLogFile);
            os = response.getOutputStream();

            byte[] b = new byte[1024];
            int len = 0;
            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
            }
            os.flush();
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    private String createLogTitle(MaintainResult result,CommandCheckItem item) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        StringBuilder string = new StringBuilder();
        // string.append("网元：");
        // string.append(result.getNeName());
        // string.append(" ");
        string.append("单元：");
        string.append(result.getUnit().getUnitName());
        string.append("  ");
        string.append("检查项：");
        string.append(item.getName());
        string.append("  ");
        string.append("时间：");
        string.append(format.format(result.getRequestTime()));

        return string.toString();
    }
}

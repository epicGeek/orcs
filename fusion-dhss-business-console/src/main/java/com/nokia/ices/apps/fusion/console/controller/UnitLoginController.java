package com.nokia.ices.apps.fusion.console.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.TerminalProperties;
import com.nokia.ices.apps.fusion.common.connector.Account;
import com.nokia.ices.apps.fusion.common.connector.ConnectConfig;
import com.nokia.ices.apps.fusion.common.connector.Terminal;
import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.console.domain.ConsoleLog;
import com.nokia.ices.apps.fusion.console.repository.ConsoleLogRepository;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentProtocol;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.service.ConsoleLogService;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.core.utils.ModuleDownLoadNameDefinition;

@RequestMapping("console")
@RestController
public class UnitLoginController {

    private static final Logger logger = LoggerFactory.getLogger(UnitLoginController.class);

    @Autowired
    private EquipmentUnitRepository equipmentUnitRepository;
    

    @Autowired
    private ConsoleLogRepository consoleLogRepository;
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam("uid") Long unitId, @RequestParam("termType") String termType,
            @RequestParam("termXlength") String termXlength, @RequestParam("termYlength") String termYlength) {
        logger.debug(TerminalProperties.getProxyHost());
        logger.debug(TerminalProperties.getProxyUserName());
        logger.debug(TerminalProperties.getProxyPassword());
        String cid = "";

        EquipmentUnit unit = equipmentUnitRepository.findOne(unitId);
        EquipmentProtocol p = unit.getServerProtocol();
        String hostname = unit.getServerIp();
        // ShiroUser shiroUser = (ShiroUser)
        // SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
        // systemOperationLogService.saveSystemOperationLog(shiroUser, Resource.unitLogin,
        // OperationType.Other, "单元名称:"+unitName);

        int port = unit.getServerPort();
        String username = unit.getLoginName();
        String password = unit.getLoginPassword();

        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();

        // 连接
        try {

            if (p.equals(EquipmentProtocol.SSH)) {

                ConnectConfig config = new ConnectConfig();
                config.username = username;
                config.password = password;
                Map<String, String> extMap = new HashMap<String, String>();
                extMap.put("x_width", termXlength);
                extMap.put("y_width", termYlength);
                extMap.put("term", termType);
                config.ext = extMap;
                Account loginAccount = Terminal.getInstance().login(hostname, port, config,shiroUser.getUserName());
                cid = loginAccount.getCid();
            } else if (p.equals(EquipmentProtocol.TELNET)) {
                ConnectConfig configProxy = new ConnectConfig();
                Map<String, String> configExt = new HashMap<String, String>();
                configProxy.username = TerminalProperties.getProxyUserName();
                configProxy.password = TerminalProperties.getProxyPassword();
                configExt.put("term", TerminalProperties.getTermType());
                configProxy.ext = configExt;
                String proxyHostAddress = TerminalProperties.getProxyHost();
                Account loginAccountProxy = Terminal.getInstance().login(proxyHostAddress, 22, configProxy,shiroUser.getUserName());
                cid = loginAccountProxy.getCid();
                String out = "";

                Terminal.getInstance().write(cid, ("telnet " + hostname).getBytes(), "command");
                // AUTO ENTER
                Terminal.getInstance().write(cid, new byte[] {(byte) 0x0d}, "keyCode");
                int i = 0;
                String status = "userName";
                Thread.sleep(1000);
                //第一次交互
                while (status.equalsIgnoreCase("userName") && i < 10) {
                    out += Terminal.getInstance().read(cid);;
                    logger.debug("OutPut is {}", out);
                    Thread.sleep(i * 500);
                    if (out.trim().endsWith("login:") || out.trim().endsWith("Username:")
                            || out.trim().endsWith("USERNAME <")) {
                        logger.info("Send UserName {}", username);
                        Terminal.getInstance().write(cid, username.getBytes(), "command");
                        // AUTO ENTER
                        Terminal.getInstance().write(cid, new byte[] {(byte) 0x0d}, "keyCode");
                        status = "password";
                        break;
                    }else if(out.trim().endsWith("Password:")){
                        Terminal.getInstance().write(cid, password.getBytes(), "command");
                        // AUTO ENTER
                        Terminal.getInstance().write(cid, new byte[] {(byte) 0x0d}, "keyCode");
                        logger.info("Send Password {}", password);
                        status = "Login Success";
                        break;
                    }
                    i++;
                }
                // 输入密码交互
                i = 0;
                while (status.equalsIgnoreCase("password") && i < 10) {
                    out += Terminal.getInstance().read(cid);;
                    logger.debug("OutPut is {}", out);
                    Thread.sleep(i * 500);
                    if (out.trim().endsWith("Password:") || out.trim().endsWith("PASSWORD <")) {
                        Terminal.getInstance().write(cid, password.getBytes(), "command");
                        // AUTO ENTER
                        Terminal.getInstance().write(cid, new byte[] {(byte) 0x0d}, "keyCode");
                        logger.info("Send Password {}", password);
                        status = "Login Success";
                        break;
                    }
                    i++;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("连接失败");

        }
        
        try {
            String logPath = shiroUser.getUserName()+"/"+cid;
            ConsoleLog cl = new ConsoleLog();
            cl.setLogPath(logPath);
            cl.setLoginUnitName(unit.getUnitName());
            cl.setLoginUserName(shiroUser.getUserName());
            cl.setStartTime(new Date());
            consoleLogRepository.save(cl);
        } catch (Exception e) {
          //保存日志，不成功不影响登录
        }
        return cid;
    }

    @RequestMapping(value = "/logout")
    public String logout(@RequestParam("cid") String cid) {
        try {
            Terminal.getInstance().destroyConnector(cid);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/download-console-log")
    public void downloadLog(@RequestParam("filePath") String filePath, HttpServletRequest request,
            HttpServletResponse response) {
        //filePath = "userName/cid";
        String fileLogPath = null;
        try {
            fileLogPath = ProjectProperties.getLogBasePath()+ File.separator+ModuleDownLoadNameDefinition.DOWNLOAD_TERMINALHISTORY + File.separator;
            
            download(request, response, fileLogPath + filePath, "application/octet-stream",filePath);

        } catch (Exception e) {
            
            logger.error(fileLogPath + " ERROR " +e.getMessage());
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
    private void download(HttpServletRequest request,HttpServletResponse response, String downLoadPath,
            String contentType, String realName){
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        File file = new File(downLoadPath);
        response.setContentType(contentType);
        try {
            response.setHeader("Content-disposition", "attachment; filename="
                    + new String(realName.getBytes("utf-8"), "ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        response.setHeader("Content-Length", String.valueOf(file.length()));
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            bos = new BufferedOutputStream(response.getOutputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] buff = new byte[2048];
        int bytesRead;
        try {
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            bis.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            bos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

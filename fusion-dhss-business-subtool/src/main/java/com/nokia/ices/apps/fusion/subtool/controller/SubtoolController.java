package com.nokia.ices.apps.fusion.subtool.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nokia.ices.apps.fusion.command.domain.types.SubtoolCmdType;
import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.subtool.domain.CheckSubtoolResult;
import com.nokia.ices.apps.fusion.subtool.domain.SubtoolHelp;
import com.nokia.ices.apps.fusion.subtool.service.SubtoolCmdService;
import com.nokia.ices.apps.fusion.subtool.service.SubtoolResultService;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.core.utils.ExcelUtil;
import com.nokia.ices.core.utils.FileOperateUtil;
import com.nokia.ices.core.utils.ModuleDownLoadNameDefinition;

@RequestMapping("/subtool")
@RestController
public class SubtoolController {

	private final static Logger logger = LoggerFactory.getLogger(SubtoolController.class);

	/**
	 * 存放用户操作对应参数获取
	 */
	public Map<String, List<String[]>> numberMap = new HashMap<String, List<String[]>>();


	@Autowired
	SubtoolResultService subtoolService;

	@Autowired
	SubtoolCmdService subtoolCmdService;
	


	@RequestMapping(value = "/getSubtoolHelpData/{code}/all", method = RequestMethod.GET)
	public List<SubtoolHelp> getSubtoolHelpData(@PathVariable String code) {

		try {
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams.put("code", code);
			return subtoolService.findSubtoolHelpByCode(searchParams);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException("查询Subtool帮助信息失败");
		}

	}

	@RequestMapping(value = "/category/search", method = RequestMethod.GET)
	public SubtoolCmdType[] getSubtoolType() {
		return SubtoolCmdType.values();
	}

	@RequestMapping(value = "/userDataCheck/search", method = RequestMethod.GET)
	public Page<CheckSubtoolResult> getUserDataCheck(@RequestParam("page") Integer iDisplayStart,
			@RequestParam("pageSize") Integer iDisplayLength, @RequestParam("exeResults") String result,
			@RequestParam("searchField") String searchField,
			@RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime) {

		// ShiroUser shiroUser = (ShiroUser)
		// SecurityUtils.getSubject().getPrincipal();
		SystemRole systemRole = null;// shiroUser.getRole();

		List<String> sortSet = new ArrayList<String>();
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("searchField", searchField);
		searchParams.put("result", result);
		searchParams.put("startTime", startTime);
		searchParams.put("endTime", endTime);

		Page<CheckSubtoolResult> page = subtoolService.findSubtooResultPageBySearch(searchParams, systemRole,
				iDisplayStart, iDisplayLength, sortSet);

		return page;
	}

	/**
	 * 模板下载操作
	 * 
	 * @param request
	 * @param response
	 * @param params
	 *            参数
	 * @param checkName
	 *            检查项
	 * @param defaultValue
	 *            默认值
	 */
	@RequestMapping(value = "/downTemplateNumber")
	public void downloadLog(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("params") String params, @RequestParam("checkName") String checkName,
			@RequestParam("defaultValue") String defaultValue) {

		String filePath = null;
		OutputStream out = null;
		File fielDir = null;
		try {

			checkName = FileOperateUtil.replaceAllStr(checkName);
			// checkName = new String(checkName.getBytes("iso8859-1"),"utf-8");
			// params = new String(params.getBytes("iso8859-1"),"utf-8");
			// 创建excel模板并保存写入服务器
			String fileName = checkName + ".xls";
			String rootPath = request.getSession().getServletContext().getRealPath("");
			// Resource resource = new ClassPathResource("/number/template/");
			// File operationDir = new
			// File(ProjectProperties.getNumber()+"template"+File.separator);
			// //resource.getFile();
			File operationDir = new File(rootPath); // resource.getFile();
			filePath = operationDir.getPath() + fileName;
			fielDir = new File(filePath);
			if (!operationDir.exists() && !operationDir.isDirectory()) {
				operationDir.mkdir();
			}
			if (!fielDir.exists()) {
				fielDir.createNewFile();
			}
			out = new FileOutputStream(fielDir);
			ExcelUtil.createExcel(checkName, params.split(","), defaultValue.split(","), out);
			if (filePath != null && !"".equals(filePath)) {
				download(request, response, fielDir.getPath(), "application/octet-stream", fileName);
			}

		} catch (Exception e) {
			logger.error(e.toString());
			throw new RuntimeException(e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 下载完成后删除该文件
			fielDir.delete();
		}

	}

	/**
	 * 下载日志
	 * 
	 * @param request
	 * @param response
	 * @param downLoadPath
	 * @param contentType
	 * @param realName
	 * @throws Exception
	 */
	public static void download(HttpServletRequest request, HttpServletResponse response, String downLoadPath,
			String contentType, String realName) throws Exception {
		request.setCharacterEncoding("UTF-8");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		File file = new File(downLoadPath);
		response.setContentType(contentType);
		response.setHeader("Content-disposition",
				"attachment; filename=" + new String(realName.getBytes("utf-8"), "ISO8859-1"));
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

	@RequestMapping(value = "/downUserData")
	public void downUserData(@RequestParam("filePath") String filePath, HttpServletRequest request,
			HttpServletResponse response) {

		try {
			String downPath = ProjectProperties.getLogBasePath() + File.separator
					+ ModuleDownLoadNameDefinition.DOWNLOAD_SUBTOOL + File.separator;
			if (downPath != null && !"".equals(downPath)) {
				download(request, response, downPath + filePath, "application/octet-stream", filePath);
			}
		} catch (Exception e) {
			logger.error(e.toString());
			throw new RuntimeException(e.toString());
		}

	}

	/*
	 * 参数导入
	 * 
	 * @param fileName
	 */
	@RequestMapping(value = "/setParamMap")
	public Long uploadFile(@RequestParam("fileName") MultipartFile fileName, @RequestParam("params") String params,
			@RequestParam("checkName") String checkName) {

		try {
			Subject currentUser = SecurityUtils.getSubject();
			ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipals().getPrimaryPrincipal();
			String userName = shiroUser.getUserName();
			int cellLength = params.split(",").length;
			ExcelUtil.readXls(numberMap, fileName.getInputStream(), cellLength, userName + "-" + checkName);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}

		return 0L;
	}

	/**
	 * 获取参数导入数据
	 * 
	 * @param fileName
	 * @return
	 */
	@RequestMapping(value = "/getParamMap")
	public List<String[]> getNumberMap(@RequestParam("checkName") String checkName) {

		String userName = "";
		try {
			// checkName = new String(checkName.getBytes("iso8859-1"),"utf-8");
			Subject currentUser = SecurityUtils.getSubject();
			ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipals().getPrimaryPrincipal();
			userName = shiroUser.getUserName();
			List<String[]> listNumber = numberMap.get(userName + "-" + checkName);
			return listNumber;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			// 集合中删除当前checkName 数据
			numberMap.remove(userName + "-" + checkName);
		}

	}

	/*
	 * 发送subTool以及 export PGWAdd=10.223.76.175 命令
	 * 
	 * @param command 支持批量操作
	 */
	@RequestMapping(value = "/sendCommand", method = RequestMethod.POST)
	public Map<String,Object> sendSubToolCommand(@RequestParam("operationType") String operationType,
			@RequestParam("checkedName") String checkName, @RequestParam("command") String command) {

		Map<String,Object> message = new HashMap<>();
		try {
		
			// 单个操作
			if ("0".equals(operationType)) {
				String key = subtoolCmdService.sendCommandSubtool(command, checkName);
				message.put(key, command);
			} else {
				// 批量操作
				String[] commands = command.split(",");
				for (String sendCmd : commands) {
					String key  = subtoolCmdService.sendCommandSubtool(sendCmd, checkName);
					message.put(key, sendCmd);
				}
			}

			return message;
		} catch (Exception e){
			logger.debug(e.toString());
			throw new RuntimeException("命令执行失败");
		}

	}

}
package com.nokia.ices.apps.fusion.score.controller;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nokia.ices.apps.fusion.score.common.utils.ExportExcel;
import com.nokia.ices.apps.fusion.score.common.utils.FileOperateUtil;
import com.nokia.ices.apps.fusion.score.domain.Scorelevel;
import com.nokia.ices.apps.fusion.score.service.ScorelevelService;
import com.nokia.ices.core.utils.JsonMapper;

@RequestMapping("/scorelevel")
@RestController
public class ScorelevelController {

	public static final Logger logger = LoggerFactory.getLogger(ScorelevelController.class);
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	@Autowired
	ScorelevelService scorelevelService;

	@RequestMapping(value = "/search")
	public Page<Scorelevel> getScorelevelList(@RequestParam("page") Integer iDisplayStart, @RequestParam("pageSize") Integer iDisplayLength, @RequestParam("scorefrom") String scorefrom,
			@RequestParam("scoreto") String scoreto, @RequestParam("level") String level) {

		List<String> sortSet = new ArrayList<String>();

		/*
		 * ShiroUser shiroUser = (ShiroUser)
		 * SecurityUtils.getSubject().getPrincipal(); SystemRole systemRole =
		 * shiroUser.getRole();
		 */
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("scorefrom", scorefrom);
		searchParams.put("scoreto", scoreto);
		searchParams.put("level", level);

		Page<Scorelevel> page = scorelevelService.findScorelevelPageBySearch(searchParams, null, iDisplayStart, iDisplayLength, sortSet);
		logger.debug(new JsonMapper().toJson(page));
		return page;
	}

	// 导出
	@RequestMapping(value = "/exportFile")
	public void saveSession(@RequestParam("scorefrom") String scorefrom, @RequestParam("scoreto") String scoreto, @RequestParam("level") String level, HttpServletRequest request,
			HttpServletResponse response) {

		List<String> sortSet = new ArrayList<String>();
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("scorefrom", scorefrom);
		searchParams.put("scoreto", scoreto);
		searchParams.put("level", level);

		ExportExcel<Scorelevel> ex = new ExportExcel<Scorelevel>();
		List<Scorelevel> scorelevelList = scorelevelService.findScorelevelListByCreator(searchParams, sortSet);
		String[] headers = { "最低分", "最高分", "等级", "修改时间" };
		String scorelevelPath = FileOperateUtil.getPropertieValue("iceslte.kpi.file.path");
		String fileName = "scorelevelExcel-" + sdf.format(new Date()) + "-" + ".xls";
		OutputStream out = null;
		File fielDir = null;
		// 保存到本地xls
		try {
			File operationDir = new File(scorelevelPath);
			fielDir = new File(scorelevelPath + fileName);
			if (!operationDir.exists() && !operationDir.isDirectory()) {
				operationDir.mkdir();
			}
			if (!fielDir.exists()) {
				fielDir.createNewFile();
			}
			out = new FileOutputStream(fielDir);
			ex.exportExcel("分数等级导出", headers, scorelevelList, out, "yyyy-MM-dd HH:mm:ss", "0", null);

			// 导出excel
			FileOperateUtil.download(request, response, scorelevelPath + fileName, "application/octet-stream", fileName);

		} catch (Exception e) {
			logger.error(e.getMessage());
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

	// 上传
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public void uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

		try {
			if (!file.isEmpty()) {
				List<String[]> dataList = ExportExcel.readXls(file.getInputStream(), 3);
				for (String[] item : dataList) {
					Scorelevel data = new Scorelevel();
					data.setScorefrom(Integer.parseInt(item[0]));
					data.setScoreto(Integer.parseInt(item[1]));
					data.setLevel(Integer.parseInt(item[2]));
					data.setUpdatetime(new Date());
					scorelevelService.addScorelevel(data);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	// 模板
	@RequestMapping(value = "/downFile")
	public void downloadLog(HttpServletRequest request, HttpServletResponse response) {
		try {

			String rootPath = request.getSession().getServletContext().getRealPath("");
			String operationPath = rootPath + File.separator + "file/template/scoreTemplate.xls";
			if (operationPath != null && !"".equals(operationPath)) {
				FileOperateUtil.download(request, response, operationPath, "application/octet-stream", "scoreTemplate.xls");
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}

	}

	@RequestMapping(value = "/getScoreLevel")
	public List<Scorelevel> getScoreLevel() {
		return scorelevelService.findScorelevelAll();
	}
}

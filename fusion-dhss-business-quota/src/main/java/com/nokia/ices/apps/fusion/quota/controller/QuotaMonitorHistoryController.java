package com.nokia.ices.apps.fusion.quota.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.quota.domain.QuotaMonitorHistory;
import com.nokia.ices.apps.fusion.quota.service.QuotaMonitorHistoryService;
import com.nokia.ices.apps.fusion.quota.service.QuotaMonitorService;

@RepositoryRestController
@RestController
public class QuotaMonitorHistoryController {
	@Autowired
	QuotaMonitorHistoryService quotaMonitorHistoryService;
	@Autowired
	QuotaMonitorService quotaMonitorService;
	@Autowired
	JdbcTemplate jdbcTemplate;

	@SuppressWarnings("rawtypes")
	@Autowired
	PagedResourcesAssembler pagedResourcesAssembler;
	private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final org.slf4j.Logger logger = LoggerFactory.getLogger(QuotaMonitorHistoryController.class);

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/findKpiHistoryDataByCondition", method = RequestMethod.GET)
	public PagedResources<QuotaMonitorHistory> getQuotaMonitorHistoryDataList(
			@RequestParam(value = "dhssName", required = true) String dhssName, // dhss缁�
			@RequestParam(value = "nodeName", required = true) String nodeName, // 灞�鍧�
			@RequestParam(value = "neType", required = true) String neType, // 缃戝厓绫诲瀷
			@RequestParam(value = "neName", required = true) String neName, // 缃戝厓鍚嶇О
			@RequestParam(value = "unitName", required = true) String unitName, // 鍗曞厓鍚嶇О
			//@RequestParam(value = "scene", required = true) String scene, // 鍦烘櫙锛屼篃灏辨槸鐜板湪鐨勨�滄寚鏍囩被鍨嬧��
			@RequestParam(value = "kpiName", required = true) String kpiName, // 鎸囨爣鍚嶇О
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime, Pageable pageable,
			PersistentEntityResourceAssembler assembler) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("dhssName_EQ", dhssName);
		paramMap.put("nodeName_EQ", nodeName);
		paramMap.put("neType_EQ", neType);
		paramMap.put("neName_EQ", neName);
		paramMap.put("unitName_EQ", unitName);
		//paramMap.put("scene_EQ", scene);
		paramMap.put("kpiCode_EQ", kpiName);
		/*
		 * simpleDateFormat.format(startTime).toString();
		 * simpleDateFormat.format(endTime).toString();
		 */

		if (startTime != null && startTime != "" && endTime != null && endTime != "") {
			try {
				paramMap.put("periodStartTime_GE", simpleDateFormat.parse(startTime));
				paramMap.put("periodStartTime_LE", simpleDateFormat.parse(endTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Page<QuotaMonitorHistory> page = quotaMonitorHistoryService.findQuotaMonitorHistoryFilter(paramMap, pageable);
		return pagedResourcesAssembler.toResource(page, assembler);
	}
}

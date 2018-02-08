package com.nokia.ices.apps.fusion.command.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate.BooleanOperator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.command.domain.CommandCheckItem;
import com.nokia.ices.apps.fusion.command.repository.CommandCheckItemRepository;
import com.nokia.ices.apps.fusion.command.service.EmsMonitorService;
import com.nokia.ices.apps.fusion.ems.domain.EmsMonitor;
import com.nokia.ices.apps.fusion.ems.domain.EmsMonitorHistory;
import com.nokia.ices.apps.fusion.ems.domain.EmsMutedItem;
import com.nokia.ices.apps.fusion.ems.repository.EmsMutedItemRepository;
import com.nokia.ices.apps.fusion.ems.repository.EquipmentNodeGroupRepository;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;

@RestController
public class EmsMonitorController {

	@Autowired
	EmsMonitorService emsMonitorService;

	@Autowired
	CommandCheckItemRepository commandCheckItemRepository;

	@Autowired
	EquipmentUnitRepository equipmentUnitRepository;

	@Autowired
	EmsMutedItemRepository emsMutedItemRepository;

	@Autowired
	EquipmentNodeGroupRepository equipmentNodeGroupRepository;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@RequestMapping("/emsResult/result")
	public Map<String, List<Map<String, Object>>> emsResult(@RequestParam(value = "ids", required = false) String ids) {
		ids = ids.equals("ALL") ? null : ids ;
		return emsMonitorService.findEmsResult(ids);
	}
	
	@RequestMapping("/emsResult/emsMutedItem")
	public List<EmsMutedItem> emsMutedItem() {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			map.put("resumeTime_GT", sdf.parse(sdf.format(new Date())));
			Map<String, SearchFilter> filter = SearchFilter.parse(map);
			Specification<EmsMutedItem> spec = DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND,
					EmsMutedItem.class);
			return emsMutedItemRepository.findAll(spec);
		} catch (ParseException e) {
			return new ArrayList<EmsMutedItem>();
		}
		
	}
	
	@RequestMapping("/emsResult/errorItems")
	public List<EmsMonitor> errorItems() {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("resultLevel_NOTEQ", "0");
		return emsMonitorService.findEmsMonitor(map);
	}
	
	@RequestMapping("/emsResult/isnotice")
	public boolean isNotice(@RequestParam(value = "command", required = false) String command,
			@RequestParam(value = "unit", required = false) String unit){
		EquipmentUnit units = equipmentUnitRepository.findEquipmentUnitByUnitName(unit);
		CommandCheckItem item = commandCheckItemRepository.findCommandCheckItemByName(command);
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("mutedUnitId_EQ", units == null ? null : units.getId()+"");
		map.put("mutedCommandId_EQ", item == null ? null : item.getId()+"");
		try {
			map.put("resumeTime_GT", sdf.parse(sdf.format(new Date())));
		} catch (ParseException e) {
			return true;
		}
		Map<String, SearchFilter> filter = SearchFilter.parse(map);
		Specification<EmsMutedItem> spec = DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND,
				EmsMutedItem.class);
		return emsMutedItemRepository.findAll(spec).size() == 0 ? false : true;
	}
	
	

	@RequestMapping("/emsResult/notice")
	public boolean notice(@RequestParam(value = "command", required = false) String command,
			@RequestParam(value = "unit", required = false) String unit,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "end", required = false) String end) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<EmsMutedItem> item = new ArrayList<EmsMutedItem>();
		switch (type) {
		case "1":
			emsMutedItemRepository
					.save(saveEmsMutedItem(end, equipmentUnitRepository.findEquipmentUnitByUnitName(unit).getId(),
							commandCheckItemRepository.findCommandCheckItemByName(command).getId()));
			break;
		case "2":
			// 取消指令
			Iterable<EquipmentUnit> units = equipmentUnitRepository.findAll();

			for (EquipmentUnit equipmentUnit : units) {
				item.add(saveEmsMutedItem(end, equipmentUnit.getId(),
						commandCheckItemRepository.findCommandCheckItemByName(command).getId()));
			}
			emsMutedItemRepository.save(item);
			break;
		case "3":
			// 取消单元
			map.put("emsType_EQ", "EMS");
			Map<String, SearchFilter> filter = SearchFilter.parse(map);
			Specification<CommandCheckItem> spec = DynamicSpecifications.bySearchFilter(filter.values(),
					BooleanOperator.AND, CommandCheckItem.class);
			List<CommandCheckItem> items = commandCheckItemRepository.findAll(spec);

			for (CommandCheckItem commandCheckItem : items) {
				item.add(saveEmsMutedItem(end, equipmentUnitRepository.findEquipmentUnitByUnitName(unit).getId(),
						commandCheckItem.getId()));
			}
			emsMutedItemRepository.save(item);
			break;
		case "4":
			map.put("mutedUnitId_EQ",equipmentUnitRepository.findEquipmentUnitByUnitName(unit).getId()+"" );
			map.put("mutedCommandId_EQ",commandCheckItemRepository.findCommandCheckItemByName(command).getId()+"" );
			emsMutedItemRepository.delete(emsMonitorService.findEmsMutedItem(map));
			break;
		case "5":
			map.put("mutedCommandId_EQ",commandCheckItemRepository.findCommandCheckItemByName(command).getId()+"" );
			emsMutedItemRepository.delete(emsMonitorService.findEmsMutedItem(map));
			break;
		case "6":
			map.put("mutedUnitId_EQ",equipmentUnitRepository.findEquipmentUnitByUnitName(unit).getId()+"" );
			emsMutedItemRepository.delete(emsMonitorService.findEmsMutedItem(map));
			break;
		}

		return true;
	}
	
	
	
	

	public EmsMutedItem saveEmsMutedItem(String end, Long unitId, Long command) {
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		EmsMutedItem emi = new EmsMutedItem();
		emi.setMutedCommandId(command);
		emi.setMutedUnitId(unitId);
		emi.setOpUser(shiroUser.getRealName());
		if (StringUtils.isNotBlank(end)) {
			try {
				emi.setResumeTime(sdf.parse(end));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			emi.setResumeTime(new Date());
		}
		emi.setStopTime(new Date());
		return emi;
	}

	@RequestMapping("/emsResult/trend")
	public List<EmsMonitorHistory> trend(@RequestParam(value = "command", required = false) String command,
			@RequestParam(value = "unit", required = false) String unit, Sort sort) throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(command)) {
			map.put("monitoredCommandName_EQ", command);
		}
		if (StringUtils.isNotBlank(unit)) {
			map.put("monitoredUnitName_EQ", unit);
		}

		Calendar date = Calendar.getInstance();
		date.setTime(new Date());
		date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);

		map.put("executeTime_GE", sdf.parse(sdf.format(date.getTime())));
		return emsMonitorService.findEmsMonitorHistoryTrend(map, sort);
	}

	@RequestMapping("/emsResult/download")
	public static void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String downLoadPath = request.getParameter("path");
		String realName = downLoadPath.substring(downLoadPath.lastIndexOf("\\") + 1);
		request.setCharacterEncoding("UTF-8");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		File file = new File(downLoadPath);
		response.setContentType("application/octet-stream");

		String operationLogName = new String(realName.getBytes("UTF-8"), "iso-8859-1");
		if (request.getHeader("User-Agent").indexOf("Trident") != -1) {
			operationLogName = java.net.URLEncoder.encode(realName, "UTF-8");
		}
		response.setHeader("Content-disposition", "attachment; filename=" + operationLogName);

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
/*
	@RequestMapping(value = "/equipment-node-group/searchAll")
	public List<EquipmentNodeGroup> getAllEquipmentNodeGroupInfo() {
		List<EquipmentNodeGroup> equipmentNodeGroupList = new ArrayList<>();
		Iterable<EquipmentNodeGroup> iter =  equipmentNodeGroupRepository.findAll();
		for (EquipmentNodeGroup equipmentNodeGroup : iter) {
			equipmentNodeGroupList.add(equipmentNodeGroup);
		}
		return equipmentNodeGroupList;
	}*/

}

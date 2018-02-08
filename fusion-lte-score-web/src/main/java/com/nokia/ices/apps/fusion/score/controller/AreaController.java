package com.nokia.ices.apps.fusion.score.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.score.service.AreaService;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.system.repository.SystemAreaRepository;

@RestController
public class AreaController {

	public static final Logger logger = LoggerFactory.getLogger(AreaController.class);
	@Autowired
	AreaService areaService;

	//y用户拥有的地区权限
	@RequestMapping(value = "/city/search")
	public List<Map<String, Object>> getCityList() {

		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		
		List<Map<String, Object>> areaList = null;
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		//地区权限缓存到session中
		if(session.getAttribute(shiroUser.getUserName())  == null) {
			areaList = areaService.findCityList(shiroUser);
			session.setAttribute(shiroUser.getUserName(), areaList);
		}else{
			areaList = areaService.findCityList(shiroUser);
		}
		
		return areaList;

	}

	@RequestMapping(value = "/area/search")
	public List<Map<String, Object>> getAreaList(@RequestParam("cityCode") String cityCode) {
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("cityCode", cityCode);
		List<Map<String, Object>> cityList =  areaService.findAreaList(searchParams);		
		return cityList;
				

	}

	@RequestMapping(value = "/areaExt/search")
	public List<Map<String, Object>> getAreaExtList() {
		return areaService.findAreaExtList();

	}
}

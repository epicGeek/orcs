package com.nokia.ices.apps.fusion.score.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.mysql.jdbc.StringUtils;
import com.nokia.ices.apps.fusion.mybatis.MapperLoadDao;
import com.nokia.ices.apps.fusion.score.repository.CityAndAreaRepository;
import com.nokia.ices.apps.fusion.score.service.AreaService;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.system.domain.SystemArea;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.system.repository.SystemAreaRepository;
import com.nokia.ices.apps.fusion.system.repository.SystemRoleRepository;

@Service("areaService")
public class AreaServiceImpl implements AreaService, InitializingBean {

	private final static Logger logger = LoggerFactory.getLogger(AreaServiceImpl.class);

	@Autowired
	MapperLoadDao mapperLoadDao;

	@Autowired
	private SystemAreaRepository systemAreaRepository;
	@Autowired
	private SystemRoleRepository systemRoleRepository;

	CityAndAreaRepository cityAndArea;

	public List<Map<String, Object>> findCityList(ShiroUser shiroUser) {

		List<String> areaList = new ArrayList<String>();

		Collection<SystemRole> roleSet = systemRoleRepository.findRoleBySystemUserUserName(shiroUser.getUserName());
		Assert.notNull(roleSet, "登录角色为空");

		Collection<SystemArea> areaSet = systemAreaRepository.findAreaBySystemRoleIn(roleSet);  // ?????
		Assert.notNull(areaSet, "配置地区为空");

		for (SystemArea systemArea : areaSet) {
			String code = systemArea.getAreaCode();
			areaList.add(code);   
		}

		Map<String, Object> params = new HashMap<>();
		params.put("areas", areaList.toArray());

		return cityAndArea.findCityList(params);
	}

	public List<Map<String, Object>> findAreaList(Map<String, Object> params) {
		return cityAndArea.findAreaList(params);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		logger.debug("init CityAndAreaRepository Bean success.............");
		cityAndArea = mapperLoadDao.getObject(CityAndAreaRepository.class);

	}

	@Override
	public List<Map<String, Object>> findAreaExtList() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getCurrentAreaAuth(String userName,String areaCode) {
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		List<Map<String, Object>> list = Lists.newArrayList();
		StringBuilder result = new StringBuilder();
		if (session.getAttribute(userName) != null) {
			list = (List<Map<String, Object>>) session.getAttribute(userName);
			for (Map areaMap : list) {
				result.append(areaMap.get("areaCode")).append(",");
			}
			if (result.length() > 0)
				result.deleteCharAt(result.length() - 1);
			if (StringUtils.isNullOrEmpty(areaCode)) {
				return result.toString();

			} else {
				if (Integer.valueOf(areaCode) == NumberUtils.INTEGER_ZERO || Integer.valueOf(areaCode) == NumberUtils.INTEGER_ONE) {
					return areaCode;
				} else if (result.indexOf(areaCode) != -1) {
					return areaCode;
				} else {
					return NumberUtils.INTEGER_MINUS_ONE.toString();
				}
			}

		} else {
			return NumberUtils.INTEGER_MINUS_ONE.toString();
		}

	}

}

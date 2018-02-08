package com.nokia.ices.apps.fusion.userdata.service;


import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.userdata.domain.UserDataLog;

public interface UserDataService{

	Page<UserDataLog> findUserDataPageBySearch(Map<String,Object> searchParams,ShiroUser shiroUser,Pageable pageable);
	
	public Map<String, Object> saveUserData(String type,String value,Boolean isAdvance);
	
	public EquipmentUnit findEquipmentUnitByUnitName(String unitName);
	
}

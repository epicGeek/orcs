package com.nokia.ices.apps.fusion.score.service;

import java.util.List;
import java.util.Map;

import com.nokia.ices.apps.fusion.security.domain.ShiroUser;


public interface AreaService {

   List<Map<String,Object>> findCityList(ShiroUser shiroUser);
	
   List<Map<String,Object>> findAreaList(Map<String, Object> params);

   List<Map<String,Object>> findAreaExtList();
   String getCurrentAreaAuth(String userName,String areaCode);
}

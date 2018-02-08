package com.nokia.ices.apps.fusion.score.repository;

import java.util.List;
import java.util.Map;


public interface CityAndAreaRepository {
	
	List<Map<String,Object>> findCityList(Map<String, Object> params);
	
	List<Map<String,Object>> findAreaList(Map<String, Object> params);

}

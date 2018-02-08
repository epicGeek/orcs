package com.nokia.ices.apps.fusion.boss.service;

import java.util.List;
import java.util.Map;

public interface BossBusinessService {
    
    
	    //业务类型
		public List<Map<String, Object>> findMonitorType();
	    
		//上级网元
	    public List<String> findNeParentName();
	    
	    public List<Map<String,Object>> findErrorCode();
	    
	    //查询指令类型
	    public List<String> findCmdTypeName();
	    
	    //统计boss成功率
	    public List<Map<String, Object>> findBossMonitorData(Map<String, Object> param,String period);
	    
	  	//用户boss查询
	    public Map<String,Object> findUserBossBusiness(Map<String, Object> params);
	    
	    public Integer getBossCount(Map<String, Object> params);
	    
	    public String getBossMessageService(String column,String reId,String reTime);
	    //业务类型和指令类型映射关系
	    public List<Map<String,Object>> businessTypeAndCmdTypeMapList();
}

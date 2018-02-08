package com.nokia.ices.apps.fusion.boss.repository;

import java.util.List;
import java.util.Map;

public interface BossBusinessDao  {
    
	//业务类型
	public List<Map<String, Object>> findMonitorType();
    
	//上级网元
    public List<String> findNeParentName();
    
    //查询errorCode
    public List<Map<String,Object>> findErrorCode();
    //查询指令类型
    public List<String> findCmdTypeName();
    
    //用户boss查询
    public Map<String,Object> findUserBossBusiness(Map<String, Object> param);
    
    //统计boss成功率
    public List<Map<String, Object>> findBossMonitorDataDao(Map<String, Object> param);
    
    
    public Integer getBossCount(Map<String, Object> params);
    
    public String getBossMessage(String column,String reId,String startTime,String endTime);
    
    public List<Map<String, Object>> cmdTypeAndBusinessTypeMapping();
}

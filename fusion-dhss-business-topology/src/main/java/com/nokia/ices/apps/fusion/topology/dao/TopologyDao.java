package com.nokia.ices.apps.fusion.topology.dao;

import java.util.List;
import java.util.Map;

public interface TopologyDao {

//    public List<Map<String, Object>> queryNeList(Map<String, Object> param);

//    public List<Map<String, Object>> queryCsDomainList(Map<String, Object> param);

//    public List<Map<String, Object>> queryLineList(Map<String, Object> param);

    public List<Map<String, Object>> queryNeKpiList(Map<String, Object> param);

//    public List<Map<String, Object>> queryUnitKpiList(Map<String, Object> param);

    public List<Map<String, Object>> queryNeAlarmList(Map<String, Object> param);

    public List<Map<String, Object>> queryUnitAlarmList(Map<String, Object> param);

    public List<Map<String, Object>> queryTopologyNeList(Map<String, Object> param);

    public List<Map<String, Object>> queryTopologyCsList(Map<String, Object> param);

//    public List<Map<String, Object>> queryTopologyLineList(Map<String, Object> param);

    public List<Map<String, Object>> queryCoDnFromSite(Map<String, Object> param);

    public Map<String, Object> queryCoDnFromNeUnit(Map<String, Object> param);

    public List<Map<String, Object>> queryCoDnFromUnitType(Map<String, Object> param);

    public List<Map<String, Object>> queryAlarmRecord(Map<String, Object> param);

    public List<Map<String, Object>> queryNeFromSite(Map<String, Object> param);

    public List<Map<String, Object>> queryUnitFromUnitType(Map<String, Object> param);

    public List<Map<String, Object>> queryAlarmMonotorFromNe(Map<String, Object> param);

    public List<Map<String, Object>> queryAlarmMonotorFromUnit(Map<String, Object> param);
}

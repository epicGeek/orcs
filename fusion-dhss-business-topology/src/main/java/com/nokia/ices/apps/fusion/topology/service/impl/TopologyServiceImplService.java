package com.nokia.ices.apps.fusion.topology.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.topology.dao.impl.TopologyDaoImpl;
import com.nokia.ices.apps.fusion.topology.service.TopologyService;

@Service("topologyService")
public class TopologyServiceImplService implements TopologyService {

    @Autowired
    TopologyDaoImpl topologyDao;
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Override
	public List<Map<String, Object>> findAlarmItem(String name) {
		String sql = "SELECT alarm.id,alarm.alarm_level AS alarmlevel,alarm.alarm_text AS alarm_content,"+
						"DATE_FORMAT(alarm.receive_start_time,'%Y-%m-%d %H:%i') AS date FROM `alarm_receive_record` alarm where alarm.alarm_cell LIKE '%"+name+"%';";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}

    
    @Override
	public List<Map<String, Object>> findAhubItem(String name) {
    	String sql = "SELECT self_port AS selfPort,target_mode as targetMode,target_lan AS targetLan,vlan_id as vlanId, "+
						" ip_address AS ipAddress,target_equipment AS targetEquipment,target_port AS"+
						 " targetPort,up_link_ip_address AS upLinkIpAddress"+
						" FROM `ahub_conn_info` WHERE ahub_name = ? ;";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, name);
		return list;
	}

    
    
    public TopologyDaoImpl getTopologyDao() {
        return topologyDao;
    }

    public void setTopologyDao(TopologyDaoImpl topologyDao) {
        this.topologyDao = topologyDao;
    }

   /* @Override
    public List<Map<String, Object>> queryNeList(Map<String, Object> param) {
        return topologyDao.queryNeList(param);
    }*/

   /* @Override
    public List<Map<String, Object>> queryCsDomainList(Map<String, Object> param) {
        return topologyDao.queryCsDomainList(param);
    }*/

    /*@Override
    public List<Map<String, Object>> queryLineList(Map<String, Object> param) {
        return topologyDao.queryLineList(param);
    }*/

    @Override
    public List<Map<String, Object>> queryNeKpiList(Map<String, Object> param) {
        return topologyDao.queryNeKpiList(param);
    }

   /* @Override
    public List<Map<String, Object>> queryUnitKpiList(Map<String, Object> param) {
        return topologyDao.queryUnitKpiList(param);
    }*/

    @Override
    public List<Map<String, Object>> queryNeAlarmList(Map<String, Object> param) {
        return topologyDao.queryNeAlarmList(param);
    }

    @Override
    public List<Map<String, Object>> queryUnitAlarmList(Map<String, Object> param) {
        return topologyDao.queryUnitAlarmList(param);
    }
    
    public static void main(String[] args) {
		if("".equals("")){
			
		}{
			
		}
	}

    @Override
    public List<Map<String, Object>> queryTopologyNeList(Map<String, Object> param) {
        return topologyDao.queryTopologyNeList(param);
    }

    @Override
    public List<Map<String, Object>> queryTopologyCsList(Map<String, Object> param) {
        return topologyDao.queryTopologyCsList(param);
    }

    /*@Override
    public List<Map<String, Object>> queryTopologyLineList(Map<String, Object> param) {
        return topologyDao.queryTopologyLineList(param);
    }*/

    @Override
    public List<Map<String, Object>> queryCoDnFromSite(Map<String, Object> param) {
        return topologyDao.queryCoDnFromSite(param);
    }

    @Override
    public Map<String, Object> queryCoDnFromNeUnit(Map<String, Object> param) {
        return topologyDao.queryCoDnFromNeUnit(param);
    }

    @Override
    public List<Map<String, Object>> queryCoDnFromUnitType(Map<String, Object> param) {
        return topologyDao.queryCoDnFromUnitType(param);
    }

    @Override
    public List<Map<String, Object>> queryAlarmRecord(Map<String, Object> param) {
        return topologyDao.queryAlarmRecord(param);
    }

    @Override
    public List<Map<String, Object>> queryNeFromSite(Map<String, Object> param) {
        return topologyDao.queryNeFromSite(param);
    }

    @Override
    public List<Map<String, Object>> queryUnitFromUnitType(Map<String, Object> param) {
        return topologyDao.queryUnitFromUnitType(param);
    }

    @Override
    public List<Map<String, Object>> queryAlarmMonotorFromNe(Map<String, Object> param) {
        return topologyDao.queryAlarmMonotorFromNe(param);
    }

    @Override
    public List<Map<String, Object>> queryAlarmMonotorFromUnit(Map<String, Object> param) {
        return topologyDao.queryAlarmMonotorFromUnit(param);
    }

	
}

package com.nokia.ices.apps.fusion.command.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.command.service.EmsMonitorService;
import com.nokia.ices.apps.fusion.ems.domain.EmsMonitor;
import com.nokia.ices.apps.fusion.ems.domain.EmsMonitorHistory;
import com.nokia.ices.apps.fusion.ems.domain.EmsMutedItem;
import com.nokia.ices.apps.fusion.ems.repository.EmsMonitorHistoryRepository;
import com.nokia.ices.apps.fusion.ems.repository.EmsMonitorRepoitory;
import com.nokia.ices.apps.fusion.ems.repository.EmsMutedItemRepository;
import com.nokia.ices.apps.fusion.ems.repository.EquipmentNodeGroupRepository;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;

@Service("emsMonitorService")
public class EmsMonitorServiceImpl implements EmsMonitorService {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	EmsMonitorHistoryRepository emsMonitorHistoryRepository;
	
	@Autowired
	EquipmentNodeGroupRepository equipmentNodeGroupRepository;
	
	@Autowired
	EmsMutedItemRepository emsMutedItemRepository;
	
	@Autowired
	EmsMonitorRepoitory emsMonitorRepoitory;

	@Override
	public Map<String,List<Map<String, Object>>> findEmsResult(String ids) {
		Map<String,List<Map<String, Object>>> result = new HashMap<String,List<Map<String, Object>>>();
		String sql ="SELECT distinct monitored_unit_name  FROM  ems_monitor ";
		String whw = "";
		if(ids != null){
			sql = sql + " WHERE ";
			String [] arr = ids.split(",");
			int i = 0;
			for (String iterable_element : arr) {
				i++;
				if(iterable_element != null && !"".equals(iterable_element)){
					whw += " monitored_unit_id= " + iterable_element ; 
					if(i != arr.length){
						whw = whw+" or";
					}
				}
			}
			
			sql = sql + whw;
		}
		
		List<Map<String, Object>> units = jdbcTemplate.queryForList(sql);
		
		List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
		
		
		StringBuffer sb = new StringBuffer("SELECT monitored_command_name");
		
		int i = 0;
		for (Map<String, Object> map : units) {
			i++;
			
			sb.append(",MAX( CASE WHEN monitored_unit_name = '" + 
					map.get("monitored_unit_name") + 
					"' THEN concat(result_value,'&',result_level,'&',result_path,"
					+ "'&',monitored_command_name,'&',monitored_unit_name,'&',monitored_command_id,'&',monitored_unit_id) ELSE NULL END) as 'value" 
					+ i + "'");
			temp.add(map);
			if(i>=100){
				break;
			}
		}
		sb.append(" FROM  ems_monitor ");
		if(ids != null){
			sb.append(" WHERE ");
			sb.append(" " + whw);
		}
		sb.append(" GROUP BY monitored_command_name ");
		List<Map<String,Object>> results = null;
		
		results = jdbcTemplate.queryForList(sb.toString());
		
		result.put("units", temp);
		result.put("result", results);
		return result;
	}

	@Override
	public List<EmsMonitorHistory> findEmsMonitorHistoryTrend(Map<String, Object> map,Sort sort) {
		Map<String,SearchFilter> filter = SearchFilter.parse(map);
		Specification<EmsMonitorHistory> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, EmsMonitorHistory.class);
		
		return emsMonitorHistoryRepository.findAll(spec,sort );
	}
	
	@Override
	public List<EmsMutedItem> findEmsMutedItem(Map<String, Object> map) {
		Map<String,SearchFilter> filter = SearchFilter.parse(map);
		Specification<EmsMutedItem> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, EmsMutedItem.class);
		
		return emsMutedItemRepository.findAll(spec);
	}

	@Override
	public List<EmsMonitor> findEmsMonitor(Map<String, Object> map) {
		Map<String,SearchFilter> filter = SearchFilter.parse(map);
		Specification<EmsMonitor> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, EmsMonitor.class);
		
		return emsMonitorRepoitory.findAll(spec);
	}

/*	@Override
	public Page<EquipmentNodeGroup> findEquipmentNodeGroupPageByFilter(Map<String, Object> paramMap,
			Pageable pageable) {
		Map<String,SearchFilter> filter = SearchFilter.parse(paramMap);
		Specification<EquipmentNodeGroup> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, EquipmentNodeGroup.class);
		return (Page<EquipmentNodeGroup>) equipmentNodeGroupRepository.findAll();
	}*/

}

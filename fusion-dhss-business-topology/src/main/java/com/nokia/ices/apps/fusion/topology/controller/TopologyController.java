package com.nokia.ices.apps.fusion.topology.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentNeRepository;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.quota.repository.QuotaMonitorRepository;
import com.nokia.ices.apps.fusion.topology.service.TopologyService;
import com.nokia.ices.core.utils.JsonMapper;

@RestController
public class TopologyController {

	private final static Logger logger = LoggerFactory.getLogger(TopologyController.class);

	@Autowired
	TopologyService topologyService;
	
	@Autowired
	EquipmentNeRepository EquipmentNeRepository;
	
	@Autowired
	EquipmentUnitRepository EquipmentUnitRepository;

	@Autowired
	QuotaMonitorRepository quotaMonitorRepository;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	

	public TopologyService getTopologyService() {
		return topologyService;
	}

	public void setTopologyService(TopologyService topologyService) {
		this.topologyService = topologyService;
	}
	
	@RequestMapping(value ="/findKpiInfoByEquipmentName")
	public List<Map<String,Object>> kpiInfo(@RequestParam(required = false, value = "NENAME") String NENAME){
		String sqlNe = "SELECT kpi_code as kpiid,kpi_name as kpiname,kpi_value as kpivalue,kpi_unit as kpiunit ,period_start_time as date FROM quota_monitor where kpi_value > 0 and ne_name = ?";
		String sqlUnit = "SELECT kpi_code as kpiid,kpi_name as kpiname,kpi_value as kpivalue,kpi_unit as kpiunit,period_start_time as date FROM quota_monitor where kpi_value > 0 and unit_name = ?";
		List<Map<String,Object>> kpiInfoOriginal = jdbcTemplate.queryForList(sqlUnit,NENAME);
		if(kpiInfoOriginal.size() != 0){
			
			//如果传单元名，可以直接返回不需要整合数据了
			return kpiInfoOriginal;
		}
		if(kpiInfoOriginal.size() == 0){
			kpiInfoOriginal = jdbcTemplate.queryForList(sqlNe,NENAME);
		}
		//如果传网元名，KPI需要整合去重
		String sql = "SELECT\n" +
				"	kpi_code as kpiid,\n" +
				"	kpi_name as kpiname,\n" +
				"	kpi_unit as kpiunit,\n" +
				"	period_start_time as date,\n" +
				"	case kpi_unit\n" +
				"			 when '%' then (kpi_success_count/kpi_request_count) *100 \n" +
				"			 when '次数'  then kpi_request_count\n" +
				"	END\n" +
				"   as kpivalue\n" +
				"from\n" +
				" (\n" +
				"		SELECT\n" +
				"			kpi_code,\n" +
				"			kpi_name,\n" +
				"			kpi_unit,\n" +
				"			period_start_time,\n" +
				"			sum(kpi_success_count) as kpi_success_count,\n" +
				"			sum(kpi_request_count) as kpi_request_count\n" +
				"		FROM\n" +
				"			quota_monitor\n" +
				"		WHERE\n" +
				"			ne_name = ? \n" +
				"		GROUP BY kpi_code,kpi_name,kpi_unit,period_start_time\n" +
				"		having sum(kpi_request_count)<> 0\n" +
				"	) v";
		List<Map<String,Object>> kpiInfo = new ArrayList<Map<String,Object>>();
		try {
			kpiInfo = jdbcTemplate.queryForList(sql,NENAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kpiInfo;
	}
	
	
	@RequestMapping("new-topology-alarm")
	public Map<String, Object> findKpiItem(@RequestParam(required = false, value = "NENAME") String NENAME){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("result", topologyService.findAlarmItem(NENAME));
		return map;
	}
	
	@RequestMapping("new-topology-ahub")
	public Map<String, Object> findAhubItem(@RequestParam(required = false, value = "NENAME") String NENAME){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("result", topologyService.findAhubItem(NENAME));
		return map;
	}
	
	
	
	@RequestMapping("new-topology")
	public Map<String, Object> findInit(@RequestParam(required = false, value = "HSS") String HSS){
		
		List<Map<String, Object>> root = new ArrayList<Map<String, Object>>();
		
		
		List<EquipmentNe> item = findEquipmentNeByhss(EquipmentNeRepository.findAll(), HSS);
		
		
		for (EquipmentNe hssNe : item) {
				Map<String,Object> hssMap = new HashMap<String,Object>();
				hssMap.put("neid", hssNe.getDhssName());
				hssMap.put("nename", hssNe.getDhssName());
				hssMap.put("netype", "ALLHSS");
				hssMap.put("cnum", "");
				
				List<Map<String, Object>> locationList = new ArrayList<Map<String, Object>>();
				
				for (EquipmentNe loca  : findEquipmentNeBylocation(item)) {
					Map<String,Object> locationMap = new HashMap<String,Object>();
					locationMap.put("neid", loca.getLocation());
					locationMap.put("nename", loca.getLocation());
					locationMap.put("netype",loca.getNeType());
					locationMap.put("neState", "正常");
					locationMap.put("location", loca.getLocation());
					locationMap.put("idsVersion",loca.getNeIdsVersion());
					locationMap.put("swVersion",loca.getNeSwVersion());
					locationMap.put("remarks", loca.getRemarks());
					locationMap.put("cnum", "");
					List<Map<String, Object>> neList = new ArrayList<Map<String, Object>>();
					
					for (EquipmentNe ne : findEquipmentNeByNe(item,  loca.getLocation())) {
						Map<String,Object> neMap = new HashMap<String,Object>();
						neMap.put("neid", ne.getNeName());
						neMap.put("nename", ne.getNeName());
						neMap.put("netype", ne.getNeType());
						neMap.put("neState", "正常");
						neMap.put("location", ne.getLocation());
						neMap.put("idsVersion", ne.getNeIdsVersion());
						neMap.put("swVersion", ne.getNeSwVersion());
						neMap.put("remarks", ne.getRemarks());
						neMap.put("cnum", ne.getCnum());
						List<EquipmentUnit> units = findUnitByNe(ne.getId());
						
						List<Map<String, Object>> unitList = new ArrayList<Map<String, Object>>();
						for (EquipmentUnit unit : units) {
							Map<String,Object> unitMap = new HashMap<String,Object>();
							unitMap.put("neid", unit.getUnitName());
							unitMap.put("nename", unit.getUnitName());
							unitMap.put("netype", unit.getUnitType());
							unitMap.put("neState", "正常");
							unitMap.put("location", unit.getNe().getLocation());
							unitMap.put("idsVersion",unit.getUnitIdsVersion());
							unitMap.put("swVersion", unit.getUnitSwVersion());
							unitMap.put("remarks", unit.getNe().getRemarks());
							unitMap.put("cnum", unit.getCnum());
							unitList.add(unitMap);
						}
						
						
						neMap.put("children", unitList);
						neList.add(neMap);
					};
					
					locationMap.put("children", neList);
					locationList.add(locationMap);
				}
				
				hssMap.put("children", locationList);
				root.add(hssMap);
		}
		
		Map<String, Object> rootMap = new HashMap<String, Object>();
		rootMap.put("result", root);
		return rootMap;
	}
	
	
	private List<EquipmentUnit> findUnitByNe(Long neid){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("ne.id_EQ", String.valueOf(neid));
		Map<String,SearchFilter> filter = SearchFilter.parse(map);
		Specification<EquipmentUnit> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, EquipmentUnit.class);
		return EquipmentUnitRepository.findAll(spec);
	}
	
	
	private List<EquipmentNe> findEquipmentNeByhss(Iterable<EquipmentNe> equipmentNes,String hss){
		List<EquipmentNe> item = new ArrayList<EquipmentNe>();
		for (EquipmentNe equipmentNe : equipmentNes) {
			if(equipmentNe.getDhssName().equals(hss)){
				item.add(equipmentNe);
			}
		}
		return item;
	}
	
	
	private List<EquipmentNe> findEquipmentNeBylocation(Iterable<EquipmentNe> equipmentNes){
		Map<String , String> map = new HashMap<String,String>();
		List<EquipmentNe> item = new ArrayList<EquipmentNe>();
		for (EquipmentNe equipmentNe : equipmentNes) {
			if(map.get(equipmentNe.getLocation()) == null){
				item.add(equipmentNe);
				map.put(equipmentNe.getLocation(), equipmentNe.getLocation());
			}
		}
		return item;
	}
	
	private List<EquipmentNe> findEquipmentNeByNe(Iterable<EquipmentNe> equipmentNes,String location){
		List<EquipmentNe> item = new ArrayList<EquipmentNe>();
		for (EquipmentNe equipmentNe : equipmentNes) {
			if( equipmentNe.getLocation().equals(location)){
				item.add(equipmentNe);
			}
		}
		return item;
	}
	
	
	


	/**
	 * 
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryNeAndCsDomainList/all", method = RequestMethod.GET, produces = "application/json;charset=utf8")
	public Map<String, Object> queryNeAndCsDomainList(@RequestParam(required = false, value = "SHHSS") String SHHSS) {

		try {

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("SHHSS", SHHSS);

			Map<String, Object> result = new HashMap<String, Object>();

			// 已修改
			List<Map<String, Object>> topologyNeList = topologyService.queryTopologyNeList(param);

			Set<String> existParentList = new HashSet<String>();
			Set<String> existLocationList = new HashSet<String>();
			Set<String> existNeList = new HashSet<String>();
			Set<String> existUnitTypeList = new HashSet<String>();

			List<Map<String, Object>> resultNeList = new ArrayList<Map<String, Object>>();
			
			List<String> unitNames = new ArrayList<String>();
			for (Map<String, Object> topologyNeMap : topologyNeList) {
				String parent = topologyNeMap.get(/* "parent" */"dhss_name").toString();
				String location = topologyNeMap.get("location").toString();
				String ne = topologyNeMap.get(/* "ne" */"ne_name").toString();
				String neIdsVersion = topologyNeMap.get("ne_ids_version") == null ? "" : topologyNeMap.get("ne_ids_version"). toString();
				String neSwVersion = topologyNeMap.get("ne_sw_version") == null ? "" : topologyNeMap.get("ne_sw_version"). toString();
				String remarks =  topologyNeMap.get("remarks") == null ? "" : topologyNeMap.get("remarks").toString() ;
				String unitTypeName = topologyNeMap.get(/* "unit_type_name" */"unit_type").toString();
				String unit = topologyNeMap.get(/* "unit" */"unit_name").toString();
				String unitIdsVersion = topologyNeMap.get("unit_ids_version") == null ? "" : topologyNeMap.get("unit_ids_version").toString();
				String unitSwVersion = topologyNeMap.get("unit_sw_version") == null ? "" : topologyNeMap.get("unit_sw_version") .toString();
				
				if(unitNames.contains(unit)){
					continue;
				}
				unitNames.add(unit);

				if (existParentList.add(parent)) {
					Map<String, Object> resultNeMap = new HashMap<String, Object>();
					resultNeMap.put("neid", parent);
					resultNeMap.put("nename", parent);
					resultNeMap.put("netype", "ALLHSS");
					resultNeMap.put("father", "");
					resultNeMap.put("son", "YES");
					resultNeMap.put("nestate", "0");
					resultNeMap.put("location", location);
					resultNeMap.put("idsversion", neIdsVersion);
					resultNeMap.put("swversion", neSwVersion);
					resultNeMap.put("remarks", remarks);
					resultNeList.add(resultNeMap);
				}
				if (existLocationList.add(parent + "_" + location)) {
					Map<String, Object> resultNeMap = new HashMap<String, Object>();
					resultNeMap.put("neid", location);
					resultNeMap.put("nename", location);
					resultNeMap.put("netype", "SITE");
					resultNeMap.put("father", parent);
					resultNeMap.put("son", "YES");
					resultNeMap.put("nestate", "0");
					resultNeMap.put("location", location);
					resultNeMap.put("idsversion", neIdsVersion);
					resultNeMap.put("swversion", neSwVersion);
					resultNeMap.put("remarks", remarks);
					resultNeList.add(resultNeMap);
				}
				if (existNeList.add(parent + "_" + location + "_" + ne)) {
					Map<String, Object> resultNeMap = new HashMap<String, Object>();
					resultNeMap.put("neid", ne);
					resultNeMap.put("nename", ne);
					resultNeMap.put("netype", "HSS");
					resultNeMap.put("father", location);
					resultNeMap.put("son", "YES");
					resultNeMap.put("nestate", "0");
					resultNeMap.put("location", location);
					resultNeMap.put("idsversion", neIdsVersion);
					resultNeMap.put("swversion", neSwVersion);
					resultNeMap.put("remarks", remarks);
					resultNeList.add(resultNeMap);
				}
				if (existUnitTypeList.add(parent + "_" + location + "_" + ne + "_" + unitTypeName)) {
					Map<String, Object> resultNeMap = new HashMap<String, Object>();
					resultNeMap.put("neid", ne + ">_<" + unitTypeName);
					resultNeMap.put("nename", unitTypeName);
					resultNeMap.put("netype", "HSSTYPE");
					resultNeMap.put("father", ne);
					resultNeMap.put("son", "YES");
					resultNeMap.put("nestate", "0");
					resultNeMap.put("location", location);
					resultNeMap.put("idsversion", neIdsVersion);
					resultNeMap.put("swversion", neSwVersion);
					resultNeMap.put("remarks", remarks);
					resultNeList.add(resultNeMap);
				}
				{
					Map<String, Object> resultNeMap = new HashMap<String, Object>();
					resultNeMap.put("neid", unit);
					resultNeMap.put("nename", unit);
					resultNeMap.put("netype", "HSS-FE");
					resultNeMap.put("father", ne + ">_<" + unitTypeName);
					resultNeMap.put("son", "");
					resultNeMap.put("nestate", "0");
					resultNeMap.put("location", location);
					resultNeMap.put("idsversion", unitIdsVersion);
					resultNeMap.put("swversion", unitSwVersion);
					resultNeMap.put("remarks", remarks);
					resultNeList.add(resultNeMap);
				}

			}

			List<Map<String, Object>> topologyCsList = topologyService.queryTopologyCsList(param);
			List<Map<String, Object>> resultCsList = new ArrayList<Map<String, Object>>();
			{
				Map<String, Object> resultCsMap = new HashMap<String, Object>();
				resultCsMap.put("neid", "rootCs");
				resultCsMap.put("nename", "CS");
				resultCsMap.put("netype", "ALLCE");
				resultCsMap.put("father", "");
				resultCsMap.put("son", "YES");
				resultCsList.add(resultCsMap);
			}
			for (Map<String, Object> topologyCsMap : topologyCsList) {
				String csCode = topologyCsMap.get("cs_code") != null ? topologyCsMap.get("cs_code").toString() : "";
				String csName = topologyCsMap.get("cs_name") != null ? topologyCsMap.get("cs_name").toString() : "";

				Map<String, Object> resultCsMap = new HashMap<String, Object>();
				resultCsMap.put("neid", csCode);
				resultCsMap.put("nename", csName);
				resultCsMap.put("netype", "CE");
				resultCsMap.put("father", "rootCs");
				resultCsMap.put("son", "");
				resultCsList.add(resultCsMap);
			}

			result.put("nehss", resultNeList);
			result.put("nece", resultCsList);

			return result;

		} catch (Exception e) {

			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	// @RequestMapping(value = "/queryNeAndCsDomainList/all", method =
	// RequestMethod.GET)
	/*
	 * public Map<String, Object> queryNeAndCsDomainListTest(HttpServletRequest
	 * request) { Map<String, Object> resultMap = new HashMap<String, Object>();
	 * List<Map<String, Object>> nehssList = new ArrayList<Map<String,
	 * Object>>(); for (int i = 0; i < 1; i++) { Map<String, Object> nehssMap =
	 * new HashMap<String, Object>(); nehssMap.put("neid", "hssid" + i);
	 * nehssMap.put("nename", "hssname" + i); nehssMap.put("netype", "ALLHSS");
	 * nehssMap.put("father", ""); nehssMap.put("son", "YES");
	 * nehssMap.put("neState", "0"); nehssMap.put("location", "shanghai");
	 * nehssMap.put("idsversion", "1.2"); nehssMap.put("swversion", "1.3");
	 * nehssMap.put("remarks", "test"); nehssList.add(nehssMap); } for (int i =
	 * 0; i < 5; i++) { Map<String, Object> nehssMap = new HashMap<String,
	 * Object>(); nehssMap.put("neid", "hssid0" + i); nehssMap.put("nename",
	 * "name0" + i); nehssMap.put("netype", "SITE"); nehssMap.put("father",
	 * "hssid0"); if (i == 2) nehssMap.put("son", "YES"); else
	 * nehssMap.put("son", ""); nehssMap.put("neState", "0");
	 * nehssMap.put("location", "shanghai"); nehssMap.put("idsversion", "1.2");
	 * nehssMap.put("swversion", "1.3"); nehssMap.put("remarks", "test");
	 * nehssList.add(nehssMap); } for (int i = 0; i < 5; i++) { Map<String,
	 * Object> nehssMap = new HashMap<String, Object>(); nehssMap.put("neid",
	 * "hssid02" + i); nehssMap.put("nename", "name02" + i);
	 * nehssMap.put("netype", "HSS"); nehssMap.put("father", "hssid02"); if (i
	 * == 2) nehssMap.put("son", "YES"); else nehssMap.put("son", "");
	 * nehssMap.put("neState", "0"); nehssMap.put("location", "shanghai");
	 * nehssMap.put("idsversion", "1.2"); nehssMap.put("swversion", "1.3");
	 * nehssMap.put("remarks", "test"); nehssList.add(nehssMap); } for (int i =
	 * 0; i < 5; i++) { Map<String, Object> nehssMap = new HashMap<String,
	 * Object>(); nehssMap.put("neid", "hssid022" + i); nehssMap.put("nename",
	 * "name022" + i); nehssMap.put("netype", "HSSTYPE"); nehssMap.put("father",
	 * "hssid022"); if (i == 2) nehssMap.put("son", "YES"); else
	 * nehssMap.put("son", ""); nehssMap.put("neState", "0");
	 * nehssMap.put("location", "shanghai"); nehssMap.put("idsversion", "1.2");
	 * nehssMap.put("swversion", "1.3"); nehssMap.put("remarks", "test");
	 * nehssList.add(nehssMap); } for (int i = 0; i < 5; i++) { Map<String,
	 * Object> nehssMap = new HashMap<String, Object>(); nehssMap.put("neid",
	 * "hssid0222" + i); nehssMap.put("nename", "name0222" + i);
	 * nehssMap.put("netype", "HSS-FE"); nehssMap.put("father", "hssid0222");
	 * nehssMap.put("son", ""); nehssMap.put("neState", "0");
	 * nehssMap.put("location", "shanghai"); nehssMap.put("idsversion", "1.2");
	 * nehssMap.put("swversion", "1.3"); nehssMap.put("remarks", "test");
	 * nehssList.add(nehssMap); } List<Map<String, Object>> necsList = new
	 * ArrayList<Map<String, Object>>(); for (int i = 0; i < 1; i++) {
	 * Map<String, Object> necsMap = new HashMap<String, Object>();
	 * necsMap.put("neid", "csid" + i); necsMap.put("nename", "csname" + i);
	 * necsMap.put("netype", "ALLCE"); necsMap.put("father", "");
	 * necsMap.put("son", "YES"); necsList.add(necsMap); } for (int i = 0; i <
	 * 5; i++) { Map<String, Object> necsMap = new HashMap<String, Object>();
	 * necsMap.put("neid", "csid0" + i); necsMap.put("nename", "csname0" + i);
	 * necsMap.put("netype", "CE"); necsMap.put("father", "csid0");
	 * necsMap.put("son", ""); necsList.add(necsMap); } resultMap.put("nehss",
	 * nehssList); resultMap.put("nece", necsList); return resultMap; }
	 */
	/**
	 * 获取网元和电路域连线关系
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryNeAndCsDomainRelationJoinList/all", method = RequestMethod.GET,produces = "application/json;charset=utf8")
	public Map<String, Object> queryNeAndCsDomainRelationJoinList(
			@RequestParam(required = false, value = "SHHSS") String SHHSS,
			@RequestParam(required = false, value = "rnd") String rnd,
			@RequestParam(required = false, value = "FROM") String FROM,
			@RequestParam(required = false, value = "TO") String TO) {

		try {

			List<Map<String, Object>> fromList = new JsonMapper().fromJson(FROM, List.class);
			Set<String> fromNeSet = new HashSet<String>();
			for (Map<String, Object> fromMap : fromList) {
				if (fromNeSet.add(fromMap.get("neid").toString()))
					System.out.println("FROM NE ID:" + fromMap.get("neid"));
			}

			List<Map<String, Object>> toList = new JsonMapper().fromJson(TO, List.class);
			Set<String> toCsSet = new HashSet<String>();
			for (Map<String, Object> toMap : toList) {
				if (toCsSet.add(toMap.get("neid").toString()))
					System.out.println("TO CS ID:" + toMap.get("neid"));
			}

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("SHHSS", SHHSS);

			// List<Map<String, Object>> topologyLineList =
			// topologyService.queryTopologyLineList(param);
			List<Map<String, Object>> topologyLineList = topologyService.queryTopologyNeList(param);
			Set<String> existLocationCsList = new HashSet<String>();
			Set<String> existNeCsList = new HashSet<String>();
			Set<String> existUnitTypeCsList = new HashSet<String>();

			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

			for (Map<String, Object> topologyLineMap : topologyLineList) {
				String csCode = topologyLineMap.get(/* "cs_code" */"cs_code") == null ? ""
						: topologyLineMap.get(/* "cs_code" */"cs_code").toString();
				String location = topologyLineMap.get("location").toString();
				String ne = topologyLineMap.get(/* "ne" */"ne_name").toString();
				String unit = topologyLineMap.get(/* "unit" */"unit_name").toString();
				String unitTypeName = topologyLineMap.get(/* "unit_type_name" */"unit_type").toString();

				if (!toCsSet.contains(csCode)) {
					continue;
				}

				if (existLocationCsList.add(location + "_" + csCode) && fromNeSet.contains(location)) {
					Map<String, Object> dataMap = new HashMap<String, Object>();
					dataMap.put("id", location + "_" + csCode);
					dataMap.put("linename", location + "_" + csCode);
					dataMap.put("fromid", location);
					dataMap.put("fromnetype", "SITE");
					dataMap.put("toid", csCode);
					dataMap.put("tonetype", "CE");
					dataMap.put("state", "0");
					dataList.add(dataMap);
				}
				if (existNeCsList.add(ne + "_" + csCode) && fromNeSet.contains(ne)) {
					Map<String, Object> dataMap = new HashMap<String, Object>();
					dataMap.put("id", ne + "_" + csCode);
					dataMap.put("linename", ne + "_" + csCode);
					dataMap.put("fromid", ne);
					dataMap.put("fromnetype", "HSS");
					dataMap.put("toid", csCode);
					dataMap.put("tonetype", "CE");
					dataMap.put("state", "0");
					dataList.add(dataMap);
				}
				if (existUnitTypeCsList.add(ne + "_" + unitTypeName + "_" + csCode)
						&& fromNeSet.contains(ne + ">_<" + unitTypeName)) {
					Map<String, Object> dataMap = new HashMap<String, Object>();
					dataMap.put("id", ne + "_" + unitTypeName + "_" + csCode);
					dataMap.put("linename", ne + "_" + unitTypeName + "_" + csCode);
					dataMap.put("fromid", ne + ">_<" + unitTypeName);
					dataMap.put("fromnetype", "HSSTYPE");
					dataMap.put("toid", csCode);
					dataMap.put("tonetype", "CE");
					dataMap.put("state", "0");
					dataList.add(dataMap);
				}
				if (fromNeSet.contains(unit)) {
					Map<String, Object> dataMap = new HashMap<String, Object>();
					dataMap.put("id", unit + "_" + csCode);
					dataMap.put("linename", unit + "_" + csCode);
					dataMap.put("fromid", unit);
					dataMap.put("fromnetype", "HSS-FE");
					dataMap.put("toid", csCode);
					dataMap.put("tonetype", "CE");
					dataMap.put("state", "0");
					dataList.add(dataMap);
				}
			}

			Map<String, Object> dataResult = new HashMap<String, Object>();
			dataResult.put("datas", dataList);
			return dataResult;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	// @RequestMapping(value = "/queryNeAndCsDomainRelationJoinList/all", method
	// = RequestMethod.GET)
	/*
	 * @SuppressWarnings("unchecked") public Map<String, Object>
	 * queryNeAndCsDomainRelationJoinListTest(HttpServletRequest request) {
	 * Map<String, Object> resultMap = new HashMap<String, Object>();
	 * List<Map<String, Object>> dataList = new ArrayList<Map<String,
	 * Object>>();
	 * 
	 * String paramFrom = request.getParameter("FROM"); String paramTo =
	 * request.getParameter("TO");
	 * 
	 * System.out.println("paramFrom::" + paramFrom);
	 * System.out.println("paramTo::" + paramTo);
	 * 
	 * Set<String> fromSet = new HashSet<String>(); List<Map<String, Object>>
	 * fromList = new JsonMapper().fromJson(paramFrom, List.class); for
	 * (Map<String, Object> fromMap : fromList) {
	 * fromSet.add(fromMap.get("neid").toString()); }
	 * 
	 * for (int i = 0; i < 5; i++) { String fromId = "hssid0" + i; if
	 * (!fromSet.contains(fromId)) continue;
	 * 
	 * Map<String, Object> dataMap = new HashMap<String, Object>();
	 * dataMap.put("id", "line1" + i); dataMap.put("linename", "line1" + i);
	 * dataMap.put("fromid", fromId); dataMap.put("fromnetype", "SITE");
	 * dataMap.put("toid", "csid0" + i); dataMap.put("tonetype", "CE");
	 * dataMap.put("state", "1"); dataList.add(dataMap); } for (int i = 0; i <
	 * 5; i++) { String fromId = "hssid02" + i; if (!fromSet.contains(fromId))
	 * continue;
	 * 
	 * Map<String, Object> dataMap = new HashMap<String, Object>();
	 * dataMap.put("id", "line2" + i); dataMap.put("linename", "line2" + i);
	 * dataMap.put("fromid", fromId); dataMap.put("fromnetype", "HSS");
	 * dataMap.put("toid", "csid0" + i); dataMap.put("tonetype", "CE");
	 * dataMap.put("state", "1"); dataList.add(dataMap); } for (int i = 0; i <
	 * 5; i++) { String fromId = "hssid022" + i; if (!fromSet.contains(fromId))
	 * continue;
	 * 
	 * Map<String, Object> dataMap = new HashMap<String, Object>();
	 * dataMap.put("id", "line3" + i); dataMap.put("linename", "line3" + i);
	 * dataMap.put("fromid", fromId); dataMap.put("fromnetype", "HSSTYPE");
	 * dataMap.put("toid", "csid0" + i); dataMap.put("tonetype", "CE");
	 * dataMap.put("state", "1"); dataList.add(dataMap); } for (int i = 0; i <
	 * 5; i++) { String fromId = "hssid0222" + i; if (!fromSet.contains(fromId))
	 * continue;
	 * 
	 * Map<String, Object> dataMap = new HashMap<String, Object>();
	 * dataMap.put("id", "line4" + i); dataMap.put("linename", "line4" + i);
	 * dataMap.put("fromid", fromId); dataMap.put("fromnetype", "HSS-FE");
	 * dataMap.put("toid", "csid0" + i); dataMap.put("tonetype", "CE");
	 * dataMap.put("state", "1"); dataList.add(dataMap); }
	 * 
	 * resultMap.put("datas", dataList); return resultMap; }
	 */

	/**
	 * 获取网元KPI指标
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryNeKpiList/all", method = RequestMethod.GET,produces = "application/json;charset=utf8")
	public Map<String, Object> queryNeKpiList(@RequestParam(required = false, value = "neId") String neId,
			@RequestParam(required = false, value = "netype") String neType) {

		try {

			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("neName", neId);

			if (neType.equals("SITE")) {
				Map<String, Object> emptyData = new HashMap<String, Object>();
				emptyData.put("kpiid", 0);
				emptyData.put("kpiname", "请选择网元");
				emptyData.put("kpivalue", "");
				emptyData.put("unit", "");
				emptyData.put("date", "");
				datas.add(emptyData);
			} else if (neType.equals("HSS")) {
				param.put("TYPE", "ne_name");
				datas = topologyService.queryNeKpiList(param);
			} else if (neType.equals("HSSTYPE")) {
				Map<String, Object> emptyData = new HashMap<String, Object>();
				emptyData.put("kpiid", 0);
				emptyData.put("kpiname", "请选择板卡");
				emptyData.put("kpivalue", "");
				emptyData.put("unit", "");
				emptyData.put("date", "");
				datas.add(emptyData);
			} else if (neType.equals("HSS-FE")) {
				param.put("TYPE", "unit_name");
				datas = topologyService.queryNeKpiList(param);
			} else {
				throw new Exception("Unsupported type: " + neType);
			}

			if (datas.size() == 0) {
				Map<String, Object> emptyData = new HashMap<String, Object>();
				emptyData.put("kpiid", 0);
				emptyData.put("kpiname", "未获取到KPI数据");
				emptyData.put("kpivalue", "");
				emptyData.put("unit", "");
				emptyData.put("date", "");
				datas.add(emptyData);
			}

			Map<String, Object> result = new HashMap<String, Object>();
			result.put("neName", neId);
			result.put("neType", neType);
			result.put("datas", datas);

			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取网元告警
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryNeAlarmList/all", method = RequestMethod.GET,produces = "application/json;charset=utf8")
	public Map<String, Object> queryNeAlarmList(@RequestParam(required = false, value = "neId") String neId,
			@RequestParam(required = false, value = "netype") String neType) {

		try {

			List<String> queryAlarmCellList = new ArrayList<String>();
			List<String> queryAlarmNeList = new ArrayList<String>();
			List<String> queryAlarmUnitList = new ArrayList<String>();

			if (neType.equals("SITE")) {
				// 仅精确匹配，不做汇总，效率考虑
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("NEID", neId);
				List<Map<String, Object>> queryList = topologyService.queryCoDnFromSite(param);
				for (Map<String, Object> queryMap : queryList) {
					String coDn = queryMap.get(/* "co_dn" */"cnum") == null ? ""
							: queryMap.get(/* "co_dn" */"cnum").toString();
					String ne = queryMap.get("ne_name") == null ? "" : queryMap.get("ne_name").toString();
					queryAlarmCellList.add(coDn);
					queryAlarmNeList.add(ne);
				}

				/*
				 * queryList = topologyService.queryNeFromSite(param); for
				 * (Map<String, Object> queryMap : queryList) { String ne =
				 * queryMap.get("ne").toString(); queryAlarmNeList.add(ne); }
				 */

			} else if (neType.equals("HSS")) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("NEID", neId);
				Map<String, Object> queryMap = topologyService.queryCoDnFromNeUnit(param);
				if (null != queryMap && null != queryMap.get(/* "co_dn" */"cnum")) {
					String coDn = queryMap.get(/* "co_dn" */"cnum") == null ? ""
							: queryMap.get(/* "co_dn" */"cnum").toString();
					queryAlarmCellList.add(coDn);
				}

				queryAlarmNeList.add(neId);
			} else if (neType.equals("HSSTYPE")) {
				// 仅精确匹配，不做汇总，效率考虑
				String[] splitNeId = neId.split(">_<");
				String neName = splitNeId[0];
				String unitTypeName = splitNeId[1];
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("neName", neName);
				param.put("unitTypeName", unitTypeName);
				List<Map<String, Object>> queryList = topologyService.queryCoDnFromUnitType(param);
				for (Map<String, Object> queryMap : queryList) {
					String coDn = queryMap.get(/* "co_dn" */"cnum") == null ? ""
							: queryMap.get(/* "co_dn" */"cnum").toString();
					queryAlarmCellList.add(coDn);
				}

				queryList = topologyService.queryUnitFromUnitType(param);
				for (Map<String, Object> queryMap : queryList) {
					String unit = queryMap.get(/* "unit" */"unit_name") == null ? ""
							: queryMap.get(/* "unit" */"unit_name").toString();
					queryAlarmUnitList.add(unit);
				}
			} else if (neType.equals("HSS-FE")) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("NEID", neId);
				Map<String, Object> queryMap = topologyService.queryCoDnFromNeUnit(param);
				if (null != queryMap && null != queryMap.get(/* "co_dn" */"cnum")) {
					String coDn = queryMap.get(/* "co_dn" */"cnum") == null ? ""
							: queryMap.get(/* "co_dn" */"cnum").toString();
					queryAlarmCellList.add(coDn);
				}

				queryAlarmUnitList.add(neId);
			} else {
				throw new Exception("Unsupported type: " + neType);
			}

			List<Map<String, Object>> alarmDataList = new ArrayList<Map<String, Object>>();

			Map<String, Object> param = new HashMap<String, Object>();
			if (queryAlarmCellList.size() > 0) {
				param.put("cellList", queryAlarmCellList);
				List<Map<String, Object>> alarmRecordList = topologyService.queryAlarmRecord(param);
				for (Map<String, Object> alarmRecordMap : alarmRecordList) {
					String alarmId = alarmRecordMap.get(/* "alarm_id" */"id").toString();
					String alarmNum = alarmRecordMap.get(/* "alarm_num" */"alarm_no").toString();
					String alarmLevel = alarmRecordMap.get("alarm_level").toString();
					String alarmText = alarmRecordMap.get("alarm_text").toString();
					String startTime = alarmRecordMap.get(/* "alarm_time2" */"start_time").toString();

					Map<String, Object> alarmMap = new HashMap<String, Object>();
					alarmMap.put("alarmid", alarmId);
					alarmMap.put("alarm_level", alarmLevel);
					alarmMap.put("alarm_content", alarmId + " " + alarmNum + " " + alarmLevel + " " + alarmText);
					alarmMap.put("date", startTime);

					alarmDataList.add(alarmMap);
				}
			}

			param = new HashMap<String, Object>();
			if (queryAlarmNeList.size() > 0) {
				param.put("neList", queryAlarmNeList);
				List<Map<String, Object>> alarmMonotorList = topologyService.queryAlarmMonotorFromNe(param);
				for (Map<String, Object> alarmMonotorMap : alarmMonotorList) {
					String alarmId = alarmMonotorMap.get("id").toString();
					String alarmLevel = alarmMonotorMap.get("alarm_level").toString();
					String alarmContent = alarmMonotorMap.get("alarm_content").toString();
					String date = alarmMonotorMap.get("start_time").toString();
					String unitName = alarmMonotorMap.get("unit_name").toString();
					String neName = alarmMonotorMap.get("ne_name").toString();

					Map<String, Object> alarmMap = new HashMap<String, Object>();
					alarmMap.put("alarmid", alarmId);
					alarmMap.put("alarm_level", alarmLevel);
					alarmMap.put("alarm_content", unitName + " " + neName + " " + alarmContent);
					alarmMap.put("date", date);

					alarmDataList.add(alarmMap);
				}
			}

			param = new HashMap<String, Object>();
			if (queryAlarmUnitList.size() > 0) {
				param.put("unitList", queryAlarmUnitList);
				List<Map<String, Object>> alarmMonotorList = topologyService.queryAlarmMonotorFromUnit(param);
				for (Map<String, Object> alarmMonotorMap : alarmMonotorList) {
					String alarmId = alarmMonotorMap.get("id").toString();
					String alarmLevel = alarmMonotorMap.get("alarm_level").toString();
					String alarmContent = alarmMonotorMap.get("alarm_content").toString();
					String date = alarmMonotorMap.get("start_time").toString();
					String unitName = alarmMonotorMap.get("unit_name").toString();
					String neName = alarmMonotorMap.get("ne_name").toString();

					Map<String, Object> alarmMap = new HashMap<String, Object>();
					alarmMap.put("alarmid", alarmId);
					alarmMap.put("alarm_level", alarmLevel);
					alarmMap.put("alarm_content", unitName + " " + neName + " " + alarmContent);
					alarmMap.put("date", date);

					alarmDataList.add(alarmMap);
				}
			}

			if (alarmDataList.size() == 0) {
				Map<String, Object> alarmMap = new HashMap<String, Object>();
				alarmMap.put("alarmid", 0);
				alarmMap.put("alarm_level", "");
				alarmMap.put("alarm_content", "未产生告警");
				alarmMap.put("date", "");
				alarmDataList.add(alarmMap);
			}

			Map<String, Object> alarmResult = new HashMap<String, Object>();
			alarmResult.put("datas", alarmDataList);

			return alarmResult;
		} catch (Exception e) {
			return new HashMap<String, Object>();
			// e.printStackTrace();
			// throw new RuntimeException(e);
		}
	}
}

class SiteCs {
	int lineId;
	String siteName;
	String csName;

	public SiteCs(int lineId, String siteName, String csName) {
		this.lineId = lineId;
		this.siteName = siteName;
		this.csName = csName;
	}
}

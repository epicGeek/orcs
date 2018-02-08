package com.nokia.ices.apps.fusion.score.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nokia.ices.apps.fusion.mybatis.MapperLoadDao;
import com.nokia.ices.apps.fusion.score.common.utils.DateUtils;
import com.nokia.ices.apps.fusion.score.domain.BtsProxy;
import com.nokia.ices.apps.fusion.score.domain.IndexScoringRule;
import com.nokia.ices.apps.fusion.score.repository.DashBoardRepository;
import com.nokia.ices.apps.fusion.score.service.AreaService;
import com.nokia.ices.apps.fusion.score.service.BtsInformationCellService;
import com.nokia.ices.apps.fusion.score.service.DashBoardService;
import com.nokia.ices.apps.fusion.score.service.IndexScoringRuleService;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;

@Service("dashBoardService")
public class DashBoardServiceImpl implements DashBoardService, InitializingBean {
	@Autowired
	MapperLoadDao mapperLoadDao;
	@Autowired
	BtsInformationCellService btsService;
	
	@Autowired
	AreaService areaService;
	
	@Autowired
	IndexScoringRuleService indexScoringRuleService;

	private final static Logger logger = LoggerFactory.getLogger(BtsSummaryScoreServiceImpl.class);

	DashBoardRepository dashBoardRepository;

	
	//故障原因 饼图 点击跳转
	@Override
	public List<Map<String, Object>> searchAreaAndCityScore(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
		String areaCode = map.get("areaCode")!=null? map.get("areaCode").toString():"";
		String kpiName = map.get("kpiName")!=null? map.get("kpiName").toString():"";
		
		Double total =  dashBoardRepository.searchTotal(map);
		if(StringUtils.isNotEmpty(areaCode)){
			//区县排名
			map.put("filed", "city_name,city_code,SUM("+kpiName+") AS "+kpiName);
			map.put("groupByName", "city_code");
			map.put("percent", "city_name,city_code,ROUND("+kpiName+"/"+total+"*100,2) AS "+kpiName);
			
		}else{
			//地市排名
			map.put("filed", "area_name,area_code,SUM("+kpiName+") AS "+kpiName);
			map.put("groupByName", "area_code");
			map.put("percent", "area_name,area_code,ROUND("+kpiName+"/"+total+"*100,2) AS "+kpiName);
		}
		map.put("kpiName", kpiName);
		
		return dashBoardRepository.searchAreaAndCityScore(map);
	}
	 //  故障统计 饼图
	@Override
	public List<Map<String, Object>> searchCurBreakReasion(Map<String, Object> map) {
		return dashBoardRepository.searchCurBreakReasion(map);
	}

	/**
	 * 
	 * 地市、折线图
	 */
	@Override
	public List<Map<String, Object>> searchCurAreaScore(Map<String, Object> param) {
		
		
		String startDate = param.get("startDate")==null?"":param.get("startDate").toString();
		String endDate = param.get("endDate")==null?"":param.get("endDate").toString();
		String areaCode = param.get("areaCode")==null?"":param.get("areaCode").toString();
		String tableName = param.get("tableName")==null?"":param.get("tableName").toString();
		StringBuffer where = new StringBuffer();
		//获取地区权限
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		String authCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
		if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate) && "area_score_hour".equals(tableName) ){
			// 查询最后一个周期 第三方和诺基亚
			String lastNokiaCycle = dashBoardRepository.searchMaxCycleAreaScoreByNokia();
			String lastThirdCycle = dashBoardRepository.searchMaxCycleAreaScoreByThird();  
			param.put("startDate", lastNokiaCycle);
			param.put("endDate", lastThirdCycle);
			String dataType = StringUtils.isEmpty(areaCode)==true?"2":"0";
			param.put("flag", "true");
			param.put("dataType", dataType);
			param.put("areaCode", authCode);
			
		}else{
			/**
			 * 各统计方式有时间的情况
			 */
			String  condition="";
			String groupBy = "";
			if("area_score_hour".equals(tableName)){
				condition="cycle>='"+startDate+"' AND cycle<='"+endDate+"'";
				groupBy="cycle";
			}else if("area_score_day".equals(tableName)){
				condition="cycle_date>='"+startDate+"' AND cycle_date<='"+endDate+"'";
				groupBy="cycle_date";
			}else if("area_score_week".equals(tableName)){
				param =DateUtils.getStartDateAndEndDate(param);
				String year = param.get("year")==null?"":param.get("year").toString();
				startDate = param.get("startDate")==null?"":param.get("startDate").toString();
				endDate = param.get("endDate")==null?"":param.get("endDate").toString();
				condition="cycle_year_of_week="+year+" AND cycle_week>="+startDate+" AND cycle_week<="+endDate;
				groupBy="cycle_year_of_week,cycle_week";
			}else if("area_score_month".equals(tableName)){
				param =DateUtils.getStartDateAndEndDate(param);
				String year = param.get("year")==null?"":param.get("year").toString();
				startDate = param.get("startDate")==null?"":param.get("startDate").toString();
				endDate = param.get("endDate")==null?"":param.get("endDate").toString();
				condition="cycle_year="+year+" AND cycle_month>="+startDate+" AND cycle_month<="+endDate;
				groupBy="cycle_year,cycle_month";
			}
			if(StringUtils.isNotEmpty(authCode)){
				if(StringUtils.isEmpty(areaCode)){         //如果area_code 为空 说明是地市级别
					param.put("groupBy", "area_code");
					where.append("  "+tableName+"  where  data_type=2 AND area_code in("+authCode+") AND  "+condition);
				}else { //区县级别
					param.put("groupBy", "area_code,city_code");
					where.append("  "+tableName+"  where  data_type=0 AND area_code in("+authCode+") AND  "+condition);
				}
				param.put("flag", "false");
				param.put("tableName", where.toString());
			}
		}
		return  dashBoardRepository.searchCurAreaScore(param);
	}
	
	
	/**
	 * 基站个数占比折线图
	 */
	@Override
	public List<Map<String, Object>> searchAreaGrade(Map<String, Object> param) {
		String tableName = param.get("tableName")==null?"":param.get("tableName").toString();
		String cityCode = param.get("cityCode")==null?"":param.get("cityCode").toString();
		
		//12.2新增加 地市小时选择的点击跳转
		if("area_score_hour".equals(tableName)){
			param.put("column", "cycle");
		}else
		if("area_score_day".equals(tableName)){
			param.put("column", "cycle_date");
		}else if("area_score_week".equals(tableName)){
			param.put("column", "cycle_week");
			param =DateUtils.getStartDateAndEndDate(param);
		}else if("area_score_month".equals(tableName)){
			param.put("column", "cycle_month");
			param =DateUtils.getStartDateAndEndDate(param);
		}
		
		 if(StringUtils.isEmpty(cityCode)){
			 param.put("groupBy", param.get("column")+",area_code");
			 param.put("where", " AND  data_type=2 AND  area_code=#{areaCode} ");
		 }else{
			 param.put("groupBy", param.get("column")+",area_code,city_code");
			 param.put("where", " AND  data_type=0 AND  area_code=#{areaCode}  AND  city_code=#{cityCode} ");
		 }
		return dashBoardRepository.areaGradeSearch(param);
	}
	
	/**
	 * 雷达图
	 */
	@Override
	public List<Map<String, Object>> areaAvgScore(Map<String, Object> param) {
		String cityCode = param.get("cityCode")==null?"":param.get("cityCode").toString();
		String tableName = param.get("tableName")==null?"":param.get("tableName").toString();
		String areaCode = param.get("areaCode")==null?"":param.get("areaCode").toString();
		//拼写条件
		 if("bts_score_day".equals(tableName)){
			param.put("column", "cycle_date");
		 }else if("bts_score_week".equals(tableName)){
			param.put("column", "cycle_week");
			param =DateUtils.getStartDateAndEndDate(param);
		 }else if("bts_score_month".equals(tableName)){
			param.put("column", "cycle_month");
			param =DateUtils.getStartDateAndEndDate(param);
		 }else if("bts_score_hour".equals(tableName)){
			 //基站级别
			 param.put("column","cycle");
		 }
		 param = getAreaRadar(param);
		 
		 if(StringUtils.isEmpty(cityCode)){
			 param.put("groupBy", "area_code");
		 }else{
			 param.put("groupBy", "area_code,city_code");
		 }
		//获取地区权限
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
		param.put("areaCode", areaCode);
		List<Map<String, Object>> result = dashBoardRepository.areaAvgScore(param);
		return result;
	}
	

	@Override
	public List<Map<String, Object>> searchCurWorstArea() {
		// 最差占比 查询最后一个周期
		String lastCycle = searchMaxCycleWorstArea();
		//获取地区权限
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		String authCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),null);
		Map<String, Object> param = new HashMap<>();
		param.put("lastCycle", lastCycle);
		param.put("areaCode", authCode);
		return dashBoardRepository.searchCurWorstArea(param);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.debug("init  DashBoardRepository Bean success.............");
		dashBoardRepository = mapperLoadDao.getBtsObject(DashBoardRepository.class);

	}

	@Override
	public List<Map<String, Object>> searchAreaScore(Map<String, String> map) {
		// 查询最后一个周期
		String cycle = dashBoardRepository.searchMaxCycleAreaScore(map);
		map.put("cycle", cycle);
		return dashBoardRepository.searchAreaScore(map);
	}

	//饼图 最新周期的数据
	@Override
	public String searchMaxCycleBreakReasion() {
		return dashBoardRepository.searchMaxCycleBreakReasion();
	}

	//最差占比 最后一个周期
	@Override
	public String searchMaxCycleWorstArea() {
		return dashBoardRepository.searchMaxCycleWorstArea();
	}
 
	public String searchMaxCycleBtsScore(String flag) {
		return dashBoardRepository.searchMaxCycleBtsScore(flag);
	}
	
	@Override
	public List<BtsProxy> searchBtsMap(Map<String, Object> map) {
		// 1.查询最后一个周期的基站评分信息
		/*String lastNokiaCycle = searchMaxCycleBtsScore("0");
		String lastThirdCycle = searchMaxCycleBtsScore("1");
		map.put("lastNokiaCycle",lastNokiaCycle);
		map.put("lastThirdCycle",lastThirdCycle);*/
		List<BtsProxy> gradeList = dashBoardRepository.searchBtsGrade(map);  //等级
		// Map<String, BtsProxy> starGroup = wrapOriginProxy(gradeList);
		// 2.查询基站信息
		List<BtsProxy> cellList = btsService.findBtsProxy();
		
		logger.debug("cellList size........"+cellList.size());
		// ArrayList<Object> jsonList = Lists.newArrayList();
		Map<Integer, BtsProxy> mrc = wrapOriginCell(cellList);
		// 3.根据ne_code，合并数据返回.
		for (BtsProxy grade : gradeList) {
			if(mrc.containsKey(grade.getBsNo())) {
				BtsProxy proxy = mrc.get(grade.getBsNo());
				grade.setProxyX(proxy.getProxyX());
				grade.setProxyY(proxy.getProxyY());
				List<BigDecimal> list = Lists.newArrayList(proxy.getProxyY(), proxy.getProxyX());
				// jsonList.add(list);
				grade.setLatlng(list);

			}
		}
		Map<String, Object> resultMap = Maps.newHashMap();
		// 基站列表
		resultMap.put("detailsData", gradeList);
		// resultMap.put("starGroup", starGroup);//放到最大的数据
		// resultMap.put("yunnanJson", jsonList); //缩到最小的数据
		// 基站星级分组
		// logger.info(new JsonMapper().toJson(resultMap));
		// return resultMap;

		return gradeList;
	}
	//地图
	private static Map<Integer, BtsProxy> wrapOriginCell(List<BtsProxy> btsProxyList) {
		return Maps.uniqueIndex(btsProxyList, indexNeCode());
	}
	//地图
	private static Function<BtsProxy, Integer> indexNeCode() {
		return new Function<BtsProxy, Integer>() {
			@Override
			public Integer apply(BtsProxy input) {
				return input.getBsNo();
			}
		};
	}
	
	/**
	 * 地市 雷达图  各指标的平均扣分
	 * @return
	 */
	private Map<String, Object>  getAreaRadar(Map<String, Object> map){
		
		//String day = map.get("day")==null?"":map.get("day").toString();
		List<IndexScoringRule> indexList = indexScoringRuleService.findIndexScoringRuleAll();
		StringBuffer params = new StringBuffer();
		StringBuffer sumStr = new StringBuffer();
		String kpiTotal = "";
		//获取kpi指标
		for(int j=0;j<indexList.size();j++){
			kpiTotal += indexList.get(j).getKpiId()+"_total+";
			sumStr.append("sum("+ indexList.get(j).getKpiId().toLowerCase()+"_score"+") AS "+ indexList.get(j).getKpiId()+"_total,");
		}
		//kpi指标+退服+告警
		kpiTotal = kpiTotal.substring(0,kpiTotal.length()-1)+"+alarm_total+out_of_total";
		sumStr.append("sum(alarm_score) AS alarm_total,sum(out_of_score) AS out_of_total");
		
		for(int j=0;j<indexList.size();j++){
			params.append("ROUND(100-a."+indexList.get(j).getKpiId()+"_total/b.total,2) AS "+indexList.get(j).getKpiId()+"_score,");
		}
		params.append("ROUND(100-alarm_total/b.total,2) AS alarm_score,ROUND(100-out_of_total/b.total,2) AS out_of_score");
		map.put("round", params);
		map.put("sumParam", sumStr);
		
		return map;
	}
	
}

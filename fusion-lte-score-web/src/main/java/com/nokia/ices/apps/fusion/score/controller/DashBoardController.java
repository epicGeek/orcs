package com.nokia.ices.apps.fusion.score.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.score.domain.BtsProxy;
import com.nokia.ices.apps.fusion.score.domain.IndexScoringRule;
import com.nokia.ices.apps.fusion.score.service.AreaService;
import com.nokia.ices.apps.fusion.score.service.DashBoardService;
import com.nokia.ices.apps.fusion.score.service.IndexScoringRuleService;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;

@RestController
public class DashBoardController {
	@Autowired
	DashBoardService dashBoardService;
	@Autowired
	IndexScoringRuleService indexScoringRuleService;
	@Autowired
	AreaService areaService;

	
	/**
	 * 饼状图 跳转过去的url
	 * @param startDate
	 * @param endDate
	 * @param areaCode
	 * @param kpiName
	 * @return
	 */
	@RequestMapping(value = "/dashboard/searchAreaAndCityScore", method = RequestMethod.GET)
	public List<Map<String, Object>> searchAreaAndCityScore(@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate, @RequestParam("areaCode") String areaCode, 
			@RequestParam("kpiName") String kpiName){
		
		//Map<String, Object> map = getParams(kpiName);
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate) ){
			//map.put("list", indexList);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
		}else{
			String lastCycle = dashBoardService.searchMaxCycleBreakReasion();
			map.put("cycle", lastCycle);
		}
		map.put("areaCode", areaCode);
		map.put("kpiName", kpiName);
		return dashBoardService.searchAreaAndCityScore(map);
	}
	
	private Map<String, Object>  getParams(){
		
		Map<String, Object> map = new HashMap<String, Object>();
		//获取动态的kpi
		List<IndexScoringRule> indexList = indexScoringRuleService.findIndexScoringRuleAll();
		
		StringBuffer params = new StringBuffer();
		StringBuffer sumStr = new StringBuffer();
		String sum = "";
		for(int j=0;j<indexList.size();j++){
			sum +="+"+ indexList.get(j).getKpiId();
		}
		sum = sum.substring(1, sum.length())+"+alarm+outof";
		
		for(int i=0;i<indexList.size();i++){
			sumStr.append(",SUM("+indexList.get(i).getKpiId()+"_total) AS "+indexList.get(i).getKpiId()+" ");
			params.append(",ROUND("+indexList.get(i).getKpiId()+"/("+sum+")*100,2) as "+indexList.get(i).getKpiId()+" ");
			/*if(indexList.get(i).getKpiId().equalsIgnoreCase(kpiName)){
				params.append(",ROUND("+indexList.get(i).getKpiId()+"/("+sum+")*100,2) as "+indexList.get(i).getKpiId()+" ");
			}else if("alarm".equalsIgnoreCase(kpiName) && i==0){
				params.append(",ROUND(alarm/("+sum+")*100,2) as alarm ");
			}else if(StringUtils.isEmpty(kpiName)){
			}*/
		}
		params.append(",ROUND(alarm/("+sum+")*100,2) as alarm,ROUND(outof/("+sum+")*100,2) as out_of ");
		sumStr.append(",SUM(alarm_total) AS alarm,SUM(outof_total) AS outof");
		String round = params.toString().substring(1, params.toString().length());   //计算
		String sumParam = sumStr.toString().substring(1, sumStr.toString().length());  //累加
		
		map.put("round", round);
		map.put("sumParam", sumParam);
		return map;
	}
	
	//故障原因-饼状图
	@RequestMapping(value = "/dashboard/searchCurBreakReasion", method = RequestMethod.GET)
	public List<Map<String, Object>> searchCurBreakReasion(@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate, @RequestParam("areaCode") String areaCode) {
		
		//获取地区权限
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
		
		//update by zhangj 默认查询最大的时间数据
		Map<String, Object> map = getParams();
		if(StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate) ){
			//map.put("list", indexList);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
		}else{
			String lastCycle = dashBoardService.searchMaxCycleBreakReasion();
			map.put("cycle", lastCycle);
		}
		map.put("areaCode", areaCode);
		return dashBoardService.searchCurBreakReasion(map);
	}
	
	//柱状图+折线图
	@RequestMapping(value = "/dashboard/searchCurAreaScore", method = RequestMethod.POST)
	public List<Map<String, Object>> searchCurAreaScore(@RequestBody ModelMap paramMap) {
		return dashBoardService.searchCurAreaScore(paramMap);
	}
	
	/**
	 * 一级基站占比折线图
	 * @param startDate
	 * @param endDate
	 * @param areaCode
	 * @return
	 */
	@RequestMapping(value = "/dashboard/searchAreaGrade", method = RequestMethod.POST)
	public List<Map<String, Object>> areaGradeSearch(@RequestBody ModelMap paramMap){
		return dashBoardService.searchAreaGrade(paramMap);
	}

	//最差区县前十排名
	@RequestMapping(value = "/dashboard/searchCurWorstArea", method = RequestMethod.GET)
	public List<Map<String, Object>> searchCurWorstArea() {
		return dashBoardService.searchCurWorstArea();
	}

	@RequestMapping(value = "/dashboard/searchAreaScore", method = RequestMethod.GET)
	public List<Map<String, Object>> searchAreaScore(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		String areaCode = null == parameterMap.get("areaCode") ? "" : parameterMap.get("areaCode")[0];
		Map<String, String> params = new HashMap<String, String>();
		// 获取有权限的地区
  		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
  		areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
		params.put("areaCode", areaCode);
		return dashBoardService.searchAreaScore(params);
	}

	//地图
	@RequestMapping(value = "/dashboard/searchBtsMap", method = RequestMethod.POST)
	public List<BtsProxy> searchBtsMap(@RequestBody ModelMap paramMap) {
		return dashBoardService.searchBtsMap(paramMap);
	}
	
	
	/**
	 * 地市雷达图
	 * @param startDate
	 * @param endDate
	 * @param areaCode
	 * @return
	 */
	@RequestMapping(value = "/dashboard/areaRadar", method = RequestMethod.POST)
	public List<Map<String, Object>> areaAvgScore(@RequestBody ModelMap paramMap){
		
		return dashBoardService.areaAvgScore(paramMap);
	}
	
	
}

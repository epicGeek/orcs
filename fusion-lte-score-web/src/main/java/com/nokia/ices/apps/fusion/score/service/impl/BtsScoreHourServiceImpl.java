package com.nokia.ices.apps.fusion.score.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.mybatis.MapperLoadDao;
import com.nokia.ices.apps.fusion.score.common.utils.DateUtils;
import com.nokia.ices.apps.fusion.score.domain.IndexScoringRule;
import com.nokia.ices.apps.fusion.score.repository.BtsScoreHourRepository;
import com.nokia.ices.apps.fusion.score.service.BtsScoreHourService;
import com.nokia.ices.apps.fusion.score.service.IndexScoringRuleService;

@Service("btsScoreHourService")
public class BtsScoreHourServiceImpl implements BtsScoreHourService, InitializingBean {

	private final static Logger logger = LoggerFactory.getLogger(BtsScoreHourServiceImpl.class);

	@Autowired
	MapperLoadDao mapperLoadDao;

	BtsScoreHourRepository btsScoreHourRepository; 
	@Autowired
	IndexScoringRuleService indexScoringRuleService;

	// 基站
	@Override
	public Map<String, Object> findBtsScoreHourAll(Map<String, Object> params) {
		
		String tableName = params.get("tableName")==null?"":params.get("tableName").toString();
		String kpiValue = params.get("kpiValue")==null?"":params.get("kpiValue").toString();
		String page = params.get("page")==null?"":params.get("page").toString();
		String pageSize = params.get("pageSize")==null?"":params.get("pageSize").toString();
		String tjType = params.get("tjType")==null?"":params.get("tjType").toString();
		
        if("bts_score_hour".equals(tableName)){
        	params.put("column", "cycle");
        }else if("bts_score_day".equals(tableName)){
        	if("3".equals(tjType)){
        		params =DateUtils.getStartDateAndEndDate(params);
        		params.put("column", "cycle_week");
        	}else{
        		params.put("column", "cycle_date");
        	}
        }if("bts_score_week".equals(tableName)){
        	params =DateUtils.getStartDateAndEndDate(params);
        	params.put("column", "cycle_week");
        }if("bts_score_month".equals(tableName)){
        	params =DateUtils.getStartDateAndEndDate(params);
        	params.put("column", "cycle_month");
        }
        if(StringUtils.isNotEmpty(kpiValue)){
        	params.put("where", "ta1."+kpiValue+">0");
        }else{
        	params.put("where","1=1");
        }
        
        List<IndexScoringRule> indexList = indexScoringRuleService.findIndexScoringRuleAll();
		IndexScoringRule alarm = new IndexScoringRule();
		alarm.setKpiId("alarm");
		indexList.add(alarm);	
		StringBuffer sb = new StringBuffer();
		
		for (IndexScoringRule isr : indexList) {
			sb.append("ta1."+isr.getKpiId().toLowerCase() + "_score,");
		}
		String kpiColumns= sb.toString().substring(0, sb.toString().length()-1);
    	params.put("kpiValue", kpiColumns);
    	Integer total  =0; 
    	if(StringUtils.isNotEmpty(pageSize)){
    		params.put("page", (Integer.parseInt(page)-1)*Integer.parseInt(pageSize));
    		params.put("pageSize", pageSize);
    		total = btsScoreHourRepository.findBtsHourCount(params);
    	}else{
    		params.put("pageSize", pageSize);
    	}
		List<Map<String, Object>> btsList = btsScoreHourRepository.findBtsHourAll(params);
		Map<String, Object> resusltMap = new HashMap<String, Object>();
		resusltMap.put("totalElements", total);
		resusltMap.put("content", btsList);

		return resusltMap;
	}

	/**
	 * 指标健康度明细
	 */
	@Override
	public Map<String, Object> findKpiScoreHourAll(Map<String, Object> params) {

		Integer total = btsScoreHourRepository.findKpiHourCount(params);
		List<Map<String, Object>> btsList = btsScoreHourRepository.findKpiHourAll(params);
		Map<String, Object> resusltMap = new HashMap<String, Object>();
		resusltMap.put("totalElements", total);
		resusltMap.put("content", btsList);

		return resusltMap;
	}

	// 基站 指标明细
	@Override
	public List<Map<String, Object>> findByBtsHourCodeAll(Map<String, Object> params) {
		return btsScoreHourRepository.findByBtsHourCodeAll(params);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		logger.debug("init btsScoreHourRepository Bean success.............");
		btsScoreHourRepository = mapperLoadDao.getBtsObject(BtsScoreHourRepository.class);

	}

	/**
	 * KPI查询
	 */
	@Override
	public Map<String, Object> findKpiQueryAll(Map<String, Object> params) {

		Integer total = btsScoreHourRepository.findKpiQueryCount(params);
		List<Map<String, Object>> btsList = btsScoreHourRepository.findKpiQueryAll(params);
		Map<String, Object> resusltMap = new HashMap<String, Object>();
		resusltMap.put("totalElements", total);
		resusltMap.put("content", btsList);

		return resusltMap;
	}

	/**
	 * KPI图形 图
	 */
	@Override
	public Map<String, Object> findKpiChartAll(Map<String, Object> params) {
		
		String cellIds = params.get("cellId")!=null?params.get("cellId").toString():"";
		String []  ces = cellIds.split(",");
		 Map<String, Object> resultMap = new HashMap<>();
		for(String id:ces){
			params.put("cellId", id);
			List<Map<String,Object>> list = btsScoreHourRepository.findKpiChartAll(params);
			resultMap.put(id, list);
		}
		
		return resultMap;
	}

	/**
	 * kpi图形 图明细
	 */
	@Override
	public List<Map<String, Object>> findKpiGraphicDetailAll(Map<String, Object> params) {
		return btsScoreHourRepository.kpiGraphicDetailAll(params);
	}

	/**
	 * 基站 指标图形
	 * 
	  */
	@Override
	public  List<Map<String, Object>> findByBtsScoreGraphicAll(Map<String, Object> params) {
		
		String tableName = params.get("tableName")==null?"":params.get("tableName").toString();
	    if("bts_score_week".equals(tableName)){
	        	params =DateUtils.getStartDateAndEndDate(params);
	      }
	    
	    List<Map<String, Object>> param =  btsScoreHourRepository.findByBtsHourGraphicAll(params);
	    return param;
	}

	@Override
	public Integer getKpiTotal(Map<String, Object> searchParams) {
		return btsScoreHourRepository.getKpiTotal(searchParams);
	}

	@Override
	public List<Map<String, Object>> getAllList(Map<String, Object> searchParams) {
		return btsScoreHourRepository.queryAll(searchParams);
	}
	
	@Override
	public Map<String, Object> getKpiValueData(Map<String, Object> params){
		Map<String, Object> resultMap = new HashMap<>();
		List<Map<String, Object>> currenMap =  btsScoreHourRepository.getKpiValueData(params);
		String date = params.get("cycle")==null?"":params.get("cycle").toString();
		String cycle = DateUtils.getSubtractHourDate(1,date).toString();
		params.put("cycle", cycle);
		List<Map<String, Object>> beforeMap =  btsScoreHourRepository.getKpiValueData(params);
		List<Map<String, Object>> listMap =new  ArrayList<Map<String,Object>>();
		Vector<String> ve = new Vector<>();
		for(Map<String, Object> cu:currenMap){
			Map<String, Object> data = new HashMap<>();
			String cell_id_cu = cu.get("cell_id").toString();
			if(null!=beforeMap && beforeMap.size()>0){
				for(Map<String, Object> be:beforeMap){
					String cell_id_be = be.get("cell_id").toString();
					if(cell_id_be.equals(cell_id_cu)){
						data.put("be"+cell_id_be, be);
						data.put("cu"+cell_id_be, cu);
						continue;
					}
				}
				listMap.add(data);
			}else{
				data.put("cu"+cell_id_cu, cu);
				listMap.add(data);
			}
			ve.add(cell_id_cu);
			
		}
		
		resultMap.put("data", listMap);
		resultMap.put("id", ve);
		return resultMap;
	}
	
	// 基站导出
		@Override
		public Map<String, Object> exportAll(Map<String, Object> params) {
			
			String tableName = params.get("tableName")==null?"":params.get("tableName").toString();
			String kpiValue = params.get("kpiValue")==null?"":params.get("kpiValue").toString();
			String tjType = params.get("tjType")==null?"":params.get("tjType").toString();
			
	        if("bts_score_hour".equals(tableName)){
	        	params.put("column", "cycle");
	        }else if("bts_score_day".equals(tableName)){
	        	if("3".equals(tjType)){
	        		params =DateUtils.getStartDateAndEndDate(params);
	        		params.put("column", "cycle_week");
	        	}else{
	        		params.put("column", "cycle_date");
	        	}
	        }if("bts_score_week".equals(tableName)){
	        	params =DateUtils.getStartDateAndEndDate(params);
	        	params.put("column", "cycle_week");
	        }if("bts_score_month".equals(tableName)){
	        	params =DateUtils.getStartDateAndEndDate(params);
	        	params.put("column", "cycle_month");
	        }
	        if(StringUtils.isNotEmpty(kpiValue)){
	        	params.put("where", "ta1."+kpiValue+">0");
	        }else{
	        	params.put("where","1=1");
	        }
	  
	        List<IndexScoringRule> indexList = indexScoringRuleService.findIndexScoringRuleAll();
			IndexScoringRule alarm = new IndexScoringRule();
			alarm.setKpiId("alarm");
			indexList.add(alarm);	
			StringBuffer sb = new StringBuffer();
			
			for (IndexScoringRule isr : indexList) {
				sb.append("ta1."+isr.getKpiId().toLowerCase() + "_score,");
			}
			String kpiColumns= sb.toString().substring(0, sb.toString().length()-1);
			
	    	params.put("kpiValue", kpiColumns);
	    	Integer total  =0; 
			List<Map<String, Object>> btsList = btsScoreHourRepository.findBtsHourAll(params);
			Map<String, Object> resusltMap = new HashMap<String, Object>();
			resusltMap.put("totalElements", total);
			resusltMap.put("content", btsList);

			return resusltMap;
		}
}

package com.nokia.ices.apps.fusion.score.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.jpa.SearchFilter.Operator;
import com.nokia.ices.apps.fusion.score.domain.AlarmScoreRule;
import com.nokia.ices.apps.fusion.score.repository.AlarmScoreRuleRepository;
import com.nokia.ices.apps.fusion.score.service.AlarmScoreRuleService;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;

@Service("alarmScoreRuleService")
public class AlarmScoreRuleServiceImpl implements AlarmScoreRuleService{

	@Autowired
	AlarmScoreRuleRepository alarmScoreRuleRepository;
	
	@Override
	@SuppressWarnings("unchecked")
	public Page<AlarmScoreRule> findAlarmScoreRulePageBySearch(Map<String, Object> searchParams, SystemRole systemRole,
			int page, int size, List<String> sortType) {
		   PageRequest pageable = buildPageRequest(page, size, sortType);
	 	   String alarmNo = searchParams.get("alarmNo").toString();
	 	   String alarmName = searchParams.get("alarmName").toString();
	 	   
	 	   List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>(); 
	 	   if(StringUtils.isNotEmpty(alarmNo)){
	 		  searchFilterAnd.add(new SearchFilter("alarmNo", Operator.EQ, alarmNo)); //告警号
	 	   }
	 	   
	 	  if(StringUtils.isNotEmpty(alarmName)){
	 	    	//searchFilterOr.add(new SearchFilter("alarmNameEn", Operator.LIKE, alarmName)); //告警英文名称
	 		 searchFilterAnd.add(new SearchFilter("alarmNameCn", Operator.LIKE, alarmName)); //告警中文名称     
	 	  } 
	 	  Specification<AlarmScoreRule> specAlarmScoreRuleAnd = DynamicSpecifications.bySearchFilter(searchFilterAnd,BooleanOperator.AND,AlarmScoreRule.class);       
	      
		  return alarmScoreRuleRepository.findAll(Specifications.where(specAlarmScoreRuleAnd),pageable);
		    
		
	}
	
	 private Sort buildSort(List<String> sortType) {
	        Sort sort = new Sort(Direction.ASC, "id");
	        for (String orderStr : sortType) {
	            String[] order = orderStr.split(",");
	            if (order.length == 1 || order[1].equalsIgnoreCase("asc"))
	                sort.and(new Sort(Direction.ASC, order[0]));
	            else
	                sort.and(new Sort(Direction.DESC, order[0]));

	        }
	        return sort;
	    }
	
	private PageRequest buildPageRequest(int page, int size, List<String> sortType) {
		Sort sort = buildSort(sortType);
        return new PageRequest(page - 1, size, sort);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<AlarmScoreRule> findAlarmScoreRuleListByCreator(Map<String, Object> searchParams, Sort sort) {
		
           String alarmNo = searchParams.get("alarmNo").toString();
	 	   String alarmName = searchParams.get("alarmName").toString();
	 	   
	 	   List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>(); 
	 	   
	 	   if(StringUtils.isNotEmpty(alarmNo)){
	 		  searchFilterAnd.add(new SearchFilter("alarmNo", Operator.EQ, alarmNo)); //告警号
	 	   }
	 	   if(StringUtils.isNotEmpty(alarmName)){
	 		 searchFilterAnd.add(new SearchFilter("alarmNameCn", Operator.LIKE, alarmName)); //告警中文名称     
	 	   } 
	 	  
	 	  Specification<AlarmScoreRule> specAlarmScoreRuleAnd = DynamicSpecifications.bySearchFilter(searchFilterAnd,BooleanOperator.AND,AlarmScoreRule.class);       
			
		  return alarmScoreRuleRepository.findAll(Specifications.where(specAlarmScoreRuleAnd),sort);
		    
	}

	@Override
	public List<AlarmScoreRule> findAlarmScoreRuleAll() {
		return alarmScoreRuleRepository.findAll();
	}
	@Override
	public void addAlarmScore(AlarmScoreRule alarmScoreRule) {
		// TODO Auto-generated method stub
		alarmScoreRuleRepository.saveAndFlush(alarmScoreRule);
	}
	
}

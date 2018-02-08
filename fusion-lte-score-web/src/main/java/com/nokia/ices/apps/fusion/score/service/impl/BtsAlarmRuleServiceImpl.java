package com.nokia.ices.apps.fusion.score.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.jpa.SearchFilter.Operator;
import com.nokia.ices.apps.fusion.score.domain.BtsAlarmRule;
import com.nokia.ices.apps.fusion.score.repository.BtsAlarmRuleRepository;
import com.nokia.ices.apps.fusion.score.service.BtsAlarmRuleService;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;

//基站退服规则
@Service("btsAlarmRuleService")
public class BtsAlarmRuleServiceImpl implements BtsAlarmRuleService{

	@Autowired
	BtsAlarmRuleRepository alarmRuleRepository;

	@Override
	@SuppressWarnings("unchecked")
	public Page<BtsAlarmRule> findAlarmRulePageBySearch(Map<String, Object> searchParams, SystemRole systemRole,
			int page, int size, List<String> sortType) {
		
		   PageRequest pageable = buildPageRequest(page, size, sortType);   
		   String alarmNo = searchParams.get("alarmNo").toString();
	 	   String alarmTitle = searchParams.get("alarmTitle").toString();
	 	   
	 	   List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>(); 
	 	   
	 	   if(StringUtils.isNotEmpty(alarmNo)){
	 		  searchFilterAnd.add(new SearchFilter("alarmNo", Operator.EQ, alarmNo)); //告警号
	 	   }
	 	   if(StringUtils.isNotEmpty(alarmTitle)){
	 		 searchFilterAnd.add(new SearchFilter("alarmTitle", Operator.LIKE, alarmTitle)); //告警中文名称     
	 	   } 
	 	  
	 	  Specification<BtsAlarmRule> specAlarmRuleAnd = DynamicSpecifications.bySearchFilter(searchFilterAnd,BooleanOperator.AND,BtsAlarmRule.class);       
			
		// return alarmRuleRepository.findAll(Specifications.where(specAlarmRuleAnd),sortType);
		  return alarmRuleRepository.findAll(Specifications.where(specAlarmRuleAnd),pageable);
	}
	/**
     * 创建排序请求.
     */
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
	  /**
     * 创建分页.
     */
    private PageRequest buildPageRequest(int page, int size, List<String> sortType) {
        Sort sort = buildSort(sortType);
        return new PageRequest(page - 1, size, sort);
    }
    
	@Override
	public List<BtsAlarmRule> findBtsAlarmRuleListByCreator(Map<String, Object> searchParams, Sort sortType) {
		
		   String alarmNo = searchParams.get("alarmNo").toString();
	 	   String alarmTitle = searchParams.get("alarmTitle").toString();
	 	   
	 	   List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>(); 
	 	   
	 	   if(StringUtils.isNotEmpty(alarmNo)){
	 		  searchFilterAnd.add(new SearchFilter("alarmNo", Operator.EQ, alarmNo)); //告警号
	 	   }
	 	   if(StringUtils.isNotEmpty(alarmTitle)){
	 		 searchFilterAnd.add(new SearchFilter("alarmTitle", Operator.LIKE, alarmTitle)); 
	 	   } 
	 	  
	 	  Specification<BtsAlarmRule> specAlarmRuleAnd = DynamicSpecifications.bySearchFilter(searchFilterAnd,BooleanOperator.AND,BtsAlarmRule.class);       
			
		  return alarmRuleRepository.findAll(Specifications.where(specAlarmRuleAnd));
	}

	@Override
	public List<BtsAlarmRule> findBtsAlarmRuleAll() {
		// TODO Auto-generated method stub
		return alarmRuleRepository.findAll();
	}

	@Override
	public void addAlarmScore(BtsAlarmRule alarmRule) {
		// TODO Auto-generated method stub
		alarmRuleRepository.saveAndFlush(alarmRule);
		
	}

}

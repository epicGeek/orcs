/*package com.nokia.ices.apps.fusion.score.service.impl;

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
import com.nokia.ices.apps.fusion.score.domain.JobRuleConfug;
import com.nokia.ices.apps.fusion.score.repository.JobManageRuleRepository;
import com.nokia.ices.apps.fusion.score.service.JobManageService;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;

*//**
 * 工单派发规则管理
 * @author Administrator
 *
 *//*
@Service("jobManageRuleService")
public class JobManageRuleServiceImpl implements JobManageService{

	@Autowired
	JobManageRuleRepository jobManageRepository;

	@Override
	public Page<JobRuleConfug> findJobRulePageBySearch(Map<String, Object> searchParams, SystemRole systemRole,
			int page, int size, List<String> sortType) {
		// TODO Auto-generated method stub
		 PageRequest pageable = buildPageRequest(page, size, sortType);   
		   String cellId = searchParams.get("cellId").toString();
	 	   String ruleTitle = searchParams.get("ruleTitle").toString();
	 	  String jobType = searchParams.get("jobType").toString();
	 	   
	 	   List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>(); 
	 	   
	 	   if(StringUtils.isNotEmpty(cellId)){
	 		  searchFilterAnd.add(new SearchFilter("cellId", Operator.EQ, cellId));
	 	   }
	 	   if(StringUtils.isNotEmpty(ruleTitle)){
	 		 searchFilterAnd.add(new SearchFilter("ruleTitle", Operator.LIKE, ruleTitle));    
	 	   } 
	 	  if(StringUtils.isNotEmpty(jobType)){
	 		  searchFilterAnd.add(new SearchFilter("jobType", Operator.EQ, jobType)); 
	 	   }
	 	  
	 	  Specification<JobRuleConfug> specAlarmRuleAnd = DynamicSpecifications.bySearchFilter(searchFilterAnd,BooleanOperator.AND,JobRuleConfug.class);       
		// return alarmRuleRepository.findAll(Specifications.where(specAlarmRuleAnd),sortType);
		  return jobManageRepository.findAll(Specifications.where(specAlarmRuleAnd),pageable);
	}
	
	*//**
     * 创建排序请求.
     *//*
    private Sort buildSort(List<String> sortType) {
        Sort sort = new Sort(Direction.DESC, "id");
        for (String orderStr : sortType) {
            String[] order = orderStr.split(",");
            if (order.length == 1 || order[1].equalsIgnoreCase("asc"))
                sort.and(new Sort(Direction.ASC, order[0]));
            else
                sort.and(new Sort(Direction.DESC, order[0]));

        }
        return sort;
    }
	  *//**
     * 创建分页.
     *//*
    private PageRequest buildPageRequest(int page, int size, List<String> sortType) {
        Sort sort = buildSort(sortType);
        return new PageRequest(page - 1, size, sort);
    }
    
	@Override
	public List<JobRuleConfug> findJobRuleListByCreator(Map<String, Object> searchParams, Sort sortType) {
		
		
		   String searchFild = searchParams.get("searchFild").toString();
	 	   String valiDateFild = searchParams.get("valiDateFild").toString();
	 	   List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>(); 
	 	   if(StringUtils.isNotEmpty(searchFild)){
	 		  searchFilterAnd.add(new SearchFilter(valiDateFild, Operator.EQ, searchFild)); //告警号
	 	   }
	 	  Specification<JobRuleConfug> specAlarmRuleAnd = DynamicSpecifications.bySearchFilter(searchFilterAnd,BooleanOperator.AND,JobRuleConfug.class);       
			
		  return jobManageRepository.findAll(Specifications.where(specAlarmRuleAnd));
	}

	@Override
	public List<JobRuleConfug> findJobManageRuleAll() {
		// TODO Auto-generated method stub
		return jobManageRepository.findAll();
	}

	@Override
	public void addJobManage(JobRuleConfug jobRule) {
		// TODO Auto-generated method stub
		jobManageRepository.saveAndFlush(jobRule);
		
	}

}
*/
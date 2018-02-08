package com.nokia.ices.apps.fusion.subtool.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.nokia.ices.apps.fusion.subtool.domain.CheckSubtoolResult;
import com.nokia.ices.apps.fusion.subtool.domain.SubtoolHelp;
import com.nokia.ices.apps.fusion.subtool.domain.SubtoolPgwLdapIp;
import com.nokia.ices.apps.fusion.subtool.repository.SubtoolRepository;
import com.nokia.ices.apps.fusion.subtool.repository.SubtoolHelpRepository;
import com.nokia.ices.apps.fusion.subtool.repository.SubtoolLadpIpRepository;
import com.nokia.ices.apps.fusion.subtool.service.SubtoolResultService;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;



@Service("subtoolService")
public class SubtoolResultServiceImpl implements SubtoolResultService {
	
	
	public static final String SRCQ_NAME_UNIT = "DHLR-MML";
	 
	@Autowired
	SubtoolRepository subtoolRepository;
	
	@Autowired
	SubtoolHelpRepository SubtoolHelpRepository;
	
	@Autowired
	SubtoolLadpIpRepository subtoolLadpIpRepository;

	@Override
	public Page<CheckSubtoolResult> findSubtooResultPageBySearch(
			Map<String, Object> searchParams, SystemRole currentUserRole,
			int page, int size, List<String> sortType) {
		
		 PageRequest pageable = buildPageRequest(page, size, sortType);    
		 //模糊查询对象
		 List<SearchFilter> searchFilterOr = new ArrayList<SearchFilter>();  
		 String searchField = searchParams.get("searchField").toString();
		 String result = searchParams.get("result").toString();
		 if(StringUtils.isNotEmpty(searchField)){
			 searchFilterOr.add(new SearchFilter("userNumber", Operator.LIKE, searchField));
			 searchFilterOr.add(new SearchFilter("checkName", Operator.LIKE, searchField));
		 }
		 Specification<CheckSubtoolResult> specListOR = DynamicSpecifications.bySearchFilter(searchFilterOr,BooleanOperator.OR,CheckSubtoolResult.class);
		 
		 List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>(); 
		 if(StringUtils.isNotEmpty(result)){
			 searchFilterAnd.add(new SearchFilter("exeResults", Operator.EQ, result));
		 }
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 if(StringUtils.isNotBlank(searchParams.get("startTime").toString())){
			 try {
				searchFilterAnd.add(new SearchFilter("createTime", Operator.GE, sdf.parse(searchParams.get("startTime").toString())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		 }
		 if(StringUtils.isNotBlank(searchParams.get("endTime").toString())){
			 try {
				searchFilterAnd.add(new SearchFilter("createTime", Operator.LT, sdf.parse(searchParams.get("endTime").toString())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		 }
		 Specification<CheckSubtoolResult> specListAnd = DynamicSpecifications.bySearchFilter(searchFilterAnd,BooleanOperator.AND,CheckSubtoolResult.class);
		 
//	     Specification<CheckSubtoolResult> specListAll = DynamicSpecifications.concatSpecification(BooleanOperator.AND,specListAnd,specListOR);  
		 return subtoolRepository.findAll(Specifications.where(specListAnd).and(specListOR),pageable);
	}

	@Override
	public List<SubtoolHelp> findSubtoolHelpByCode(Map<String, Object> searchParams) {
		
		 String code = searchParams.get("code").toString();
		
		 List<SearchFilter> searchFilter = new ArrayList<SearchFilter>(); 
		 if(StringUtils.isNotEmpty(code)){
			 searchFilter.add(new SearchFilter("code", Operator.EQ, code));
		 }
		 Specification<SubtoolHelp> specListAnd = DynamicSpecifications.bySearchFilter(searchFilter,BooleanOperator.AND,SubtoolHelp.class);
		 
		 return SubtoolHelpRepository.findAll(specListAnd);
		
	}
	
	
	@Override
	public List<SubtoolPgwLdapIp> fingSubtoolLadpIpList(String pgwIp) {
		// TODO Auto-generated method stub
		 List<SearchFilter> searchFilter = new ArrayList<SearchFilter>(); 
		 if(StringUtils.isNotEmpty(pgwIp)){
			 searchFilter.add(new SearchFilter("pgwIp", Operator.EQ, pgwIp));
		 }
		 Specification<SubtoolPgwLdapIp> specListAnd = DynamicSpecifications.bySearchFilter(searchFilter,BooleanOperator.AND,SubtoolPgwLdapIp.class);
		 return subtoolLadpIpRepository.findAll(specListAnd);
	}
	
	/**
	  * 判断字符串是否是整数
	  * @param value
	  * @return
	  */
	 @SuppressWarnings("unused")
	private static boolean isInteger(String value) {
	  try {
	   Integer.parseInt(value);
	   return true;
	  } catch (NumberFormatException e) {
	    return false;
	  }
	 }
	
	
	
  
	/**
    * 创建排序请求.
    */
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
   
  /**
  * 创建分页.
  */
 private PageRequest buildPageRequest(int page, int size, List<String> sortType) {
     Sort sort = buildSort(sortType);
     return new PageRequest(page - 1, size, sort);
 }


}

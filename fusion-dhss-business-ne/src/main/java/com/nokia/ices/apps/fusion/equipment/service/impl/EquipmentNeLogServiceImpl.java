package com.nokia.ices.apps.fusion.equipment.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNeOperationLog;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentNeOperationLogRepsitory;
import com.nokia.ices.apps.fusion.equipment.service.EquipmentNeOperationService;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.jpa.SearchFilter.Operator;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;


@Service("equipmentNeOperationService")
public class EquipmentNeLogServiceImpl implements EquipmentNeOperationService{

	@Autowired
	EquipmentNeOperationLogRepsitory EquipmentNeOperationLogRep;

	@Override
	public Page<EquipmentNeOperationLog> findNeLogPageBySearch(Map<String,Object> searchParams,ShiroUser shiroUser, Pageable pageable) {
		List<SearchFilter> searchFilterOR = new ArrayList<SearchFilter>();
		String searchField = searchParams.get("searchField")!=null?searchParams.get("searchField").toString():"";
		if(StringUtils.isNotEmpty(searchField)){
		  searchFilterOR.add(new SearchFilter("unitName", Operator.LIKE, searchField));
		  searchFilterOR.add(new SearchFilter("neName", Operator.LIKE, searchField));
		}
		Specification<EquipmentNeOperationLog> specListOR = DynamicSpecifications.bySearchFilter(searchFilterOR,BooleanOperator.OR,EquipmentNeOperationLog.class);       
    	
        List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>(); 
        searchFilterAnd.add(new SearchFilter("giveTime", Operator.GE, searchParams.get("neStratTime")));
        searchFilterAnd.add(new SearchFilter("giveTime", Operator.LE, searchParams.get("neEndTime")));
        Specification<EquipmentNeOperationLog> specListAnd = DynamicSpecifications.bySearchFilter(searchFilterAnd,BooleanOperator.AND,EquipmentNeOperationLog.class);
        
        List<SearchFilter> searchFilterEq = new ArrayList<SearchFilter>(); 
        String neType = searchParams.get("neType")!=null?searchParams.get("neType").toString():"";
        String unitType =searchParams.get("unitType")!=null?searchParams.get("unitType").toString():"";
        if(StringUtils.isNotEmpty(neType)){
        	searchFilterEq.add(new SearchFilter("neType", Operator.EQ, neType));
        }
        if(StringUtils.isNotEmpty(unitType)){
        	searchFilterEq.add(new SearchFilter("unitType", Operator.EQ, unitType));
        }
    
        Specification<EquipmentNeOperationLog> specListEq = DynamicSpecifications.bySearchFilter(searchFilterEq,BooleanOperator.AND,EquipmentNeOperationLog.class);
        
//		Specification<EquipmentNeOperationLog> specListTotal = DynamicSpecifications.concatSpecification(BooleanOperator.AND,specListAnd,specListOR,specListEq);
		return EquipmentNeOperationLogRep.findAll(Specifications.where(specListAnd).and(specListOR).and(specListEq),pageable);
		
		
	}	
    

	

	@Override
	public List<EquipmentNeOperationLog> findAll() {
		return EquipmentNeOperationLogRep.findAll();
	}

	@Override
	public List<EquipmentNeOperationLog> findNeLogListByCreator(
			Map<String, Object> searchParams,ShiroUser shiroUser, Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	

}

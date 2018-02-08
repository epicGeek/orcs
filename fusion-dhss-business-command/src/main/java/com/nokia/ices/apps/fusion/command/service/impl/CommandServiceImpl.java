package com.nokia.ices.apps.fusion.command.service.impl;

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

import com.nokia.ices.apps.fusion.command.domain.CommandCheckItem;
import com.nokia.ices.apps.fusion.command.domain.CommandGroup;
import com.nokia.ices.apps.fusion.command.repository.CommandCheckItemRepository;
import com.nokia.ices.apps.fusion.command.repository.CommandGroupRepository;
import com.nokia.ices.apps.fusion.command.service.CommandService;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.jpa.SearchFilter.Operator;
import com.nokia.ices.apps.fusion.monitor.domain.MonitorTable;
import com.nokia.ices.apps.fusion.monitor.repository.MonitorTableRepository;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;



@Service("commandService")
public class CommandServiceImpl implements CommandService{

	@Autowired
	private CommandCheckItemRepository commandCheckItemRepository;
	@Autowired
	private CommandGroupRepository commandGroupRepository;
	
	@Autowired
	private MonitorTableRepository monitorTableRepository;
	

	
	@Override
	public Page<CommandCheckItem> findCommandCheckItemPageBySearch(
			String searchField,SystemRole currentUserRole,Pageable pageable) {
		    //PageRequest pageable = buildPageRequest(page, size, sortType);    
    	    List<SearchFilter> searchFilter = new ArrayList<SearchFilter>(); 
    	    
    	    if(StringUtils.isNotEmpty(searchField)){
    	    	 searchFilter.add(new SearchFilter("name", Operator.LIKE, searchField));
    	    	 searchFilter.add(new SearchFilter("command", Operator.LIKE, searchField));
    	    	 searchFilter.add(new SearchFilter("remarks", Operator.LIKE, searchField));      
    	    } 
            Specification<CommandCheckItem> specCommandCheckItem = DynamicSpecifications.bySearchFilter(searchFilter,BooleanOperator.OR,CommandCheckItem.class);              
		    return commandCheckItemRepository.findAll(specCommandCheckItem,pageable);
		    
	}
	@Override
	public List<CommandCheckItem> findCommandCheckItemByGroup(Long group_id){
		
		return commandCheckItemRepository.findListByCommandGroupIdEquals(group_id);
	}
	
	@Override
	public List<CommandCheckItem> findCommandCheckItemBySearch(String searchField){	
		List<SearchFilter> searchFilter = new ArrayList<SearchFilter>(); 
		if(StringUtils.isNotEmpty(searchField)){
	    	 searchFilter.add(new SearchFilter("name", Operator.LIKE, searchField));
	    	 searchFilter.add(new SearchFilter("command", Operator.LIKE, searchField));
	    	 searchFilter.add(new SearchFilter("applyUnit", Operator.LIKE, searchField));      
	    } 
       Specification<CommandCheckItem> specCommandCheckItem = DynamicSpecifications.bySearchFilter(searchFilter,BooleanOperator.OR,CommandCheckItem.class);              	
	   return commandCheckItemRepository.findAll(specCommandCheckItem);
	}
	
	@Override
	public List<CommandCheckItem> findCommandCheckItemListByCreator(
			Map<String, Object> searchParams,
			List<String> sortType) {
		
		
		return null;
	}


	@Override
	public Page<CommandGroup> findCommandGroupPageBySearch(
			String searchField,SystemRole currentUserRole,Pageable pageable) { 
		List<SearchFilter> searchFilter = new ArrayList<SearchFilter>();  
		
		if(StringUtils.isNotEmpty(searchField)){
			searchFilter.add(new SearchFilter("commandGroupName", Operator.LIKE, searchField));
			 searchFilter.add(new SearchFilter("commandGroupDesc", Operator.LIKE, searchField));
			 searchFilter.add(new SearchFilter("remark", Operator.LIKE, searchField));       
		}    
        Specification<CommandGroup> specList = DynamicSpecifications.bySearchFilter(searchFilter,BooleanOperator.OR,CommandGroup.class);       	  
	    return commandGroupRepository.findAll(specList,pageable);
	    
	}

	@Override
	public List<CommandGroup> findCommandGroupListByCreator(
			Map<String, Object> searchParams, List<String> sortType) {
		return null;
	}
	@Override
	public List<CommandCheckItem> findCheckItemByCreator(){
//		Set<CommandGroup> setCommandGroup = new HashSet<CommandGroup>();
//		return commandCheckItemRepository.findListByCommandGroupIn(setCommandGroup);
		return commandCheckItemRepository.findAll();
	}
	
	
//	@Override
//	public List<CommandCheckItem> findCheckItemByCreator() {
//		
//    	
//		String cmdType = params.get("cmdType")==null?"":params.get("cmdType").toString();
//		String itemName = params.get("itemName")==null?"":params.get("itemName").toString();
//		String moduleName = params.get("moduleName")==null?"":params.get("moduleName").toString();
//		String unitTypeId = params.get("unitTypeId")==null?"":params.get("unitTypeId").toString();
//		String neTypeId = params.get("neTypeId")==null?"":params.get("neTypeId").toString();
//		
//		
//		
//    	List<SearchFilter> searchFilter = new ArrayList<SearchFilter>(); 
//    	if(StringUtils.isNotEmpty(itemName)){
//    		searchFilter.add(new SearchFilter("name", Operator.LIKE, itemName));
//    	}
//    	Specification<CommandCheckItem> specMod = CommandCheckItemSpecifications.CommandCheckItemEqual(moduleName);
//    	Specification<CommandCheckItem> specCmd = 	CommandCheckItemSpecifications.CommandCheckItemSubtoolCmdTypeEqual(cmdType);
//    	Specification<CommandCheckItem> specUnit = 	CommandCheckItemSpecifications.CommandCheckItemCommandGroupUnitType(unitTypeId);
//    	
//    	Set<CommandGroup> setCom = commandGroupRepository.findCommandGroupByNeTypeEquals(EquipmentNeType.valueOf(EquipmentNeType.class, neTypeId));
//    	Specification<CommandCheckItem> specNe	=CommandCheckItemSpecifications.CommandCheckItemCommandGroupNeType(setCom);
//    	
//		Specification<CommandCheckItem> specCommandCheckItem = DynamicSpecifications.bySearchFilter(searchFilter,BooleanOperator.AND,CommandCheckItem.class);              
//		return commandCheckItemRepository.findAll(Specifications.where(specCommandCheckItem)
//				.and(specMod).and(specCmd).and(specUnit).and(specNe));
//	}
	@Override
	public List<CommandGroup> findCommandGroupListBySearch(String searchField,
			SystemRole currentUserRole, Sort sort) {
		List<SearchFilter> searchFilter = new ArrayList<SearchFilter>();  
		
		if(StringUtils.isNotEmpty(searchField)){
			searchFilter.add(new SearchFilter("commandGroupName", Operator.LIKE, searchField));
			 searchFilter.add(new SearchFilter("commandGroupDesc", Operator.LIKE, searchField));
			 searchFilter.add(new SearchFilter("remark", Operator.LIKE, searchField));       
		}    
        Specification<CommandGroup> specList = DynamicSpecifications.bySearchFilter(searchFilter,BooleanOperator.OR,CommandGroup.class);       	  
	    return commandGroupRepository.findAll(specList,sort);
	}
	
	
	@Override
	public List<CommandCheckItem> findCheckItemByNeTypeAndUnitType(List<CommandGroup> CommandGroupSet,
			String cmdType,String categoryId,String itemName) {
		
    	List<SearchFilter> searchFilter = new ArrayList<SearchFilter>(); 
    	if(StringUtils.isNotEmpty(categoryId)){
    		searchFilter.add(new SearchFilter("category.id", Operator.EQ, categoryId));
    	}
    	if(StringUtils.isNotEmpty(itemName)){
    		searchFilter.add(new SearchFilter("name", Operator.LIKE, itemName));
    	}
    	if(StringUtils.isNotEmpty(cmdType)){
    		searchFilter.add(new SearchFilter("cmdType.cmdtypeId", Operator.EQ, cmdType));
    	}
    	if(!CommandGroupSet.isEmpty()){
    	/*if(!checkItemSet.isEmpty()){*/
    		
    		searchFilter.add(new SearchFilter("commandGroup", Operator.ENTITYIN, CommandGroupSet));
    	}
    	
		Specification<CommandCheckItem> specCommandCheckItem = DynamicSpecifications.bySearchFilter(searchFilter,BooleanOperator.AND,CommandCheckItem.class);              
		return commandCheckItemRepository.findAll(specCommandCheckItem);
	}
	
	
	@Override
	public List<CommandGroup> findCommandGroupList(String unitTypeId,
			String neTypeId) {
		List<SearchFilter> searchFilter = new ArrayList<SearchFilter>(); 
		if(StringUtils.isNotEmpty(unitTypeId)){
    		searchFilter.add(new SearchFilter("unitType.id", Operator.EQ, unitTypeId));
    	}
    	if(StringUtils.isNotEmpty(neTypeId)){
    		searchFilter.add(new SearchFilter("NeType.id", Operator.EQ, neTypeId));
    	}
    	
    	Specification<CommandGroup> specCommandGroupItem = DynamicSpecifications.bySearchFilter(searchFilter,BooleanOperator.AND,CommandGroup.class);              
		return commandGroupRepository.findAll(specCommandGroupItem);
	}
    @Override
    public List<CommandGroup> findCommandGroupList(SystemRole systemRole) {
        List<SearchFilter> searchFilter = new ArrayList<SearchFilter>(); 
        searchFilter.add(new SearchFilter("systemRole",Operator.EQ,systemRole));

        Specification<CommandGroup> specCommandGroupItem = DynamicSpecifications.bySearchFilter(searchFilter,BooleanOperator.AND,CommandGroup.class);              
        return commandGroupRepository.findAll(specCommandGroupItem);

    }


	@Override
	public Page<MonitorTable> findMonitorTablePageByFilter(Map<String, Object> paramMap, Pageable pageable) {
		Map<String,SearchFilter> filter = SearchFilter.parse(paramMap);
		Specification<MonitorTable> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, MonitorTable.class);
		return monitorTableRepository.findAll(spec, pageable);
	}
	@Override
	public Page<CommandCheckItem> findCommandCheckItemPageBySearch(String searchField, String searchField01,
			SystemRole currentUserRole, Pageable pageable) {
		 List<SearchFilter> searchFilter = new ArrayList<SearchFilter>(); 
		 List<SearchFilter> searchFilter01 = new ArrayList<SearchFilter>(); 
 	    if(StringUtils.isNotEmpty(searchField)){
 	    	 searchFilter.add(new SearchFilter("name", Operator.LIKE, searchField));
 	    	 searchFilter.add(new SearchFilter("command", Operator.LIKE, searchField));
 	    	 searchFilter.add(new SearchFilter("remarks", Operator.LIKE, searchField));      
 	    }
 	   if(StringUtils.isNotEmpty(searchField01)){
// 		     searchFilter01.add(new SearchFilter("name", Operator.LIKE, searchField01));
// 		     searchFilter01.add(new SearchFilter("command", Operator.LIKE, searchField01));
//	    	 searchFilter01.add(new SearchFilter("remarks", Operator.LIKE, searchField01));
	    	 searchFilter01.add(new SearchFilter("emsType", Operator.LIKE, searchField01));
	    	// searchFilter01.add(new SearchFilter("cmd_type", Operator.LIKE, searchField01));
	    	// searchFilter01.add(new SearchFilter("remarks", Operator.LIKE, searchField01));      
	    } 

         Specification<CommandCheckItem> specCommandCheckItem = DynamicSpecifications.bySearchFilter(searchFilter,BooleanOperator.OR,CommandCheckItem.class);  
         Specification<CommandCheckItem> specCommandCheckItem01 = DynamicSpecifications.bySearchFilter(searchFilter01,BooleanOperator.OR,CommandCheckItem.class); 
		 return commandCheckItemRepository.findAll(Specifications.where(specCommandCheckItem).and(specCommandCheckItem01),pageable);
	
	}
	
	
	
}

package com.nokia.ices.apps.fusion.log.service.impl;

import java.text.ParseException;
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
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.jpa.SearchFilter.Operator;
import com.nokia.ices.apps.fusion.log.repository.UserManagerRepository;
import com.nokia.ices.apps.fusion.log.service.UserManagerService;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.system.domain.SystemUser;
import com.nokia.ices.apps.fusion.system.repository.SystemUserRepository;

@Service("userManagerService")
public class UserManagerServiceImpl implements UserManagerService{
	
	@Autowired
	SystemUserRepository systemUserRepository;	
	
	@Autowired
	UserManagerRepository userManagerRepository;	

	@Transactional (propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=true)
	public Page<SystemUser> findUserDataPageBySearch(Map<String, Object> searchParams, SystemRole systemRole,
			int page, int size, List<String> sortType) throws ParseException {
		
		PageRequest pageable = buildPageRequest(page, size, sortType);   
		
		String userName = searchParams.get("userName")!=null?searchParams.get("userName").toString():"";
		/*ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(StringUtils.isEmpty(userName)){
			userName = shiroUser.getRealName();
		}*/
	 	List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>();
	 	 
	 	if(StringUtils.isNotEmpty(userName)){
			searchFilterAnd.add(new SearchFilter("userName", Operator.EQ, userName));
		}
	 	
		Specification<SystemUser> specListAnd = DynamicSpecifications.bySearchFilter(searchFilterAnd,BooleanOperator.AND,SystemUser.class);            		
		return systemUserRepository.findAll(Specifications.where(specListAnd),pageable);
		
	}

	/**
     * 创建排序请求. 历史记录
     */
    private Sort buildSort(List<String> sortType) {
    	List<Order> listSort= new ArrayList<Order>();
        for (String orderStr : sortType) {
            String[] order = orderStr.split(",");
            if (order[1].equalsIgnoreCase("asc")){
            	listSort.add(new Order (Direction.ASC,order[0]));
            }else{
            	listSort.add(new Order (Direction.DESC,order[0]));
            }
        }
        return new Sort(listSort);
    }
	  /**
     * 创建分页.
     */
    private PageRequest buildPageRequest(int page, int size, List<String> sortType) {
        Sort sort = buildSort(sortType);
        return new PageRequest(page-1, size, sort);
    }


	@Override
	@Transactional (propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=true)
	public List<SystemUser> findUserDataListByCreator(Map<String, Object> searchParams) throws ParseException {
		
		String userName = searchParams.get("userName")!=null?searchParams.get("userName").toString():"";
	/*	ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		
		if(StringUtils.isEmpty(userName)){
		//	String userName = shiroUser.getRealName();
			userName = shiroUser.getRealName();
		}*/
		
	 	List<SearchFilter> searchFilterAnd = new ArrayList<SearchFilter>();
	 	if(StringUtils.isNotEmpty(userName)){
			searchFilterAnd.add(new SearchFilter("userName", Operator.EQ,  userName));
		}
	 	
		Specification<SystemUser> specListAnd = DynamicSpecifications.bySearchFilter(searchFilterAnd,BooleanOperator.AND,SystemUser.class);            		
		return systemUserRepository.findAll(Specifications.where(specListAnd));
	}
	

	@Override
	@Transactional (propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=false)
	public void addSystemUser(SystemUser systemUser) {
		systemUserRepository.save(systemUser);
	}

	@Override
	@Transactional (propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=false)
	public void deleteByIdUserData(Long id) {
		
		systemUserRepository.delete(id);
	}

	@Override
	@Transactional (propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=false)
	public void updateSystemUserName(SystemUser systemUser) {
		
		userManagerRepository.updateUserDataName(systemUser.getRealName(),systemUser.getUserName(),
		  systemUser.getMobile(),systemUser.getEmail(),systemUser.getId());
	}

}

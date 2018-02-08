package com.nokia.ices.apps.fusion.system.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.jpa.SearchFilter.Operator;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.system.domain.SystemArea;
import com.nokia.ices.apps.fusion.system.domain.SystemOperationLog;
import com.nokia.ices.apps.fusion.system.domain.SystemResource;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.system.domain.SystemUser;
import com.nokia.ices.apps.fusion.system.repository.SystemAreaRepository;
import com.nokia.ices.apps.fusion.system.repository.SystemOperationLogRepository;
import com.nokia.ices.apps.fusion.system.repository.SystemUserRepository;
import com.nokia.ices.apps.fusion.system.repository.spec.SystemUserSpecifications;
import com.nokia.ices.apps.fusion.system.repository.types.SystemOperationLogOpType;
import com.nokia.ices.apps.fusion.system.service.SystemService;

@Service
public class SystemServiceImpl implements SystemService {

    @Autowired
    private SystemUserRepository systemUserRepository;

//    @Autowired
//    private SystemRoleRepository systemRoleRepository;

    @Autowired
    private SystemAreaRepository systemAreaRepository;
    
    @Autowired
    private SystemOperationLogRepository systemOperationLogRepository;
    
   


    @Override
    public Page<SystemUser> findUserPageByFilter(String searchField, Collection<SystemRole> roleSet, Pageable pageable,String userName) {
        // 根据关键字构造查询
        Specification<SystemUser> specSearchByWord = SystemUserSpecifications.userNameLike(searchField);
        Specification<SystemUser> specSearchByWord1 = SystemUserSpecifications.realNameLike(searchField);
        Specification<SystemUser> specSearchByWord2 = SystemUserSpecifications.mobileLike(searchField);
        Specification<SystemUser> specSearchByWord3 = SystemUserSpecifications.emailLike(searchField);
        Specification<SystemUser> specSearchByWord4 = SystemUserSpecifications.creatorLike(userName);
        /*Set<SystemRole> roleSetQuery = new HashSet<SystemRole>(roleSet);
        Specification<SystemUser> specSearchByRoleSet = SystemUserSpecifications.roleIn(roleSetQuery);*/
        // 权限
//        Specification<SystemUser> privilegeSpec = 
        return systemUserRepository.findAll(Specifications.where(specSearchByWord)./*and(specSearchByRoleSet).*/and(specSearchByWord4).or(specSearchByWord1).or(specSearchByWord2).or(specSearchByWord3), pageable);
    }

    @Override
    public List<SystemUser> findUserListByFilter(Collection<SystemRole> roleSet, Sort sort) {
        Set<SystemRole> roleSetQuery = new HashSet<SystemRole>(roleSet);
        Specification<SystemUser> specSearchByRoleSet = SystemUserSpecifications.roleIn(roleSetQuery);
        return systemUserRepository.findAll(Specifications.where(specSearchByRoleSet), sort);
    }


    @Override
    public Page<SystemArea> findAreaPageByFilter(String searchField, Collection<SystemRole> roleSet, Pageable pageable) {
        List<SearchFilter> nameFilter = new ArrayList<SearchFilter>();
        if (StringUtils.isNotEmpty(searchField)) {
            nameFilter.add(new SearchFilter("areaName", Operator.LIKE, searchField));
            nameFilter.add(new SearchFilter("areaCode", Operator.LIKE, searchField));
        }
        Specification<SystemArea> specificationArea =
                DynamicSpecifications.bySearchFilter(nameFilter, BooleanOperator.OR, SystemArea.class);
        return systemAreaRepository.findAll(specificationArea, pageable);
    }

    @Override
    public List<SystemArea> findAreaListByFilter(String searchField, Collection<SystemRole> roleSet, Sort sort) {
        List<SearchFilter> nameFilter = new ArrayList<SearchFilter>();
        if (StringUtils.isNotEmpty(searchField)) {
            nameFilter.add(new SearchFilter("areaName", Operator.LIKE, searchField));
        }
        Specification<SystemArea> spec = DynamicSpecifications.bySearchFilter(nameFilter, BooleanOperator.OR, SystemArea.class);
        return systemAreaRepository.findAll(spec, sort);
    }

    @Override
    public Page<SystemResource> findResourcePageByFilter(String searchField, Collection<SystemRole> roleSet, Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<SystemRole> findRoleListByFilter(String searchField, Collection<SystemRole> roleSet, Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<SystemResource> findResourceListByFilter(String searchField, Collection<SystemRole> roleSet, Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public Page<SystemOperationLog> findSystemOperationLogPageByFilter(Map<String,Object> paramMap, Pageable pageable) {
		        Map<String,SearchFilter> filter = SearchFilter.parse(paramMap);
		        Specification<SystemOperationLog> spec = 
		                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, SystemOperationLog.class);
		        return systemOperationLogRepository.findAll(spec, pageable);
	}

	@Override
	public void saveSystemOperationLog(ShiroUser shiroUser,  String appModule, String OpText,
			SystemOperationLogOpType OpType) {
		SystemOperationLog systemOperationLog = new SystemOperationLog();
		systemOperationLog.setLoginUserName(shiroUser.getSelfLink());
		systemOperationLog.setApp("DHSS");
		systemOperationLog.setAppModule(appModule);
		systemOperationLog.setOpText(OpText);
		systemOperationLog.setOpType(OpType);
		systemOperationLog.setSip(shiroUser.getRemoteAddress());
		systemOperationLog.setLogTime(new Date());
		systemOperationLogRepository.save(systemOperationLog);
	}

}

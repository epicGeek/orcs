package com.nokia.ices.apps.fusion.system.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.system.domain.SystemArea;
import com.nokia.ices.apps.fusion.system.domain.SystemOperationLog;
import com.nokia.ices.apps.fusion.system.domain.SystemResource;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.system.domain.SystemUser;
import com.nokia.ices.apps.fusion.system.repository.types.SystemOperationLogOpType;


public interface SystemService {
    Page<SystemUser> findUserPageByFilter(String searchField, Collection<SystemRole> roleSet, Pageable pageable,String userName);
    
    Page<SystemResource> findResourcePageByFilter(String searchField, Collection<SystemRole> roleSet, Pageable pageable);

    Page<SystemArea> findAreaPageByFilter(String searchField, Collection<SystemRole> roleSet, Pageable pageable);

    List<SystemUser> findUserListByFilter(Collection<SystemRole> roleSet, Sort sort);

    List<SystemRole> findRoleListByFilter(String searchField, Collection<SystemRole> roleSet, Sort sort);

    List<SystemResource> findResourceListByFilter(String searchField, Collection<SystemRole> roleSet, Sort sort);

    List<SystemArea> findAreaListByFilter(String searchField, Collection<SystemRole> roleSet, Sort sort);
    
    Page<SystemOperationLog> findSystemOperationLogPageByFilter(Map<String,Object> paramMap,  Pageable pageable);
    
    void saveSystemOperationLog(ShiroUser shiroUser,String appModule,String OpText,SystemOperationLogOpType OpType);

}

package com.nokia.ices.apps.fusion.score.service;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nokia.ices.apps.fusion.score.domain.TreeNode;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.system.domain.SystemArea;
import com.nokia.ices.apps.fusion.system.domain.SystemMenu;
import com.nokia.ices.apps.fusion.system.domain.SystemUser;

public interface PlatformService {
	
	 Page<SystemUser> findSystemUserPageBySearch(Map<String, Object> searchParams,ShiroUser shiroUser, Pageable pageable);
		
	 List<SystemUser> findSystemUserManageListByCreator(Map<String, Object> searchParams);
	 
	 List<SystemArea> findSystemAreaListByCreator(Map<String, Object> searchParams);
	 
	 Map<String,List<SystemMenu>> getcurrentUserAuthority(ShiroUser shiroUser);
	 
	 List<String> getCurrentUserArea(ShiroUser shiroUser);

	 List<TreeNode> getMenuTree(String roleId, String id);

	 Integer validataUserNmaeExists(String valiDateField,String type);


}

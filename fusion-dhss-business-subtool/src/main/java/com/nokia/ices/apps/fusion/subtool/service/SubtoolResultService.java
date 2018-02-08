package com.nokia.ices.apps.fusion.subtool.service;


import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.nokia.ices.apps.fusion.subtool.domain.CheckSubtoolResult;
import com.nokia.ices.apps.fusion.subtool.domain.SubtoolHelp;
import com.nokia.ices.apps.fusion.subtool.domain.SubtoolPgwLdapIp;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;

public interface SubtoolResultService{
	
	

	Page<CheckSubtoolResult> findSubtooResultPageBySearch(Map<String,Object> searchParams,SystemRole currentUserRole,int page, int size,List<String> sortType);
	
	List<SubtoolHelp> findSubtoolHelpByCode(Map<String,Object> searchParams);
	
	List<SubtoolPgwLdapIp> fingSubtoolLadpIpList(String pgwIp);
	
	
}

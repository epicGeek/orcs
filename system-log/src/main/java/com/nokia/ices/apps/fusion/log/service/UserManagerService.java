package com.nokia.ices.apps.fusion.log.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.system.domain.SystemUser;


public interface UserManagerService {


	 Page<SystemUser> findUserDataPageBySearch(Map<String, Object> searchParams, SystemRole systemRole,int page, int size, List<String> sortType) throws ParseException;

	 List<SystemUser> findUserDataListByCreator(Map<String, Object> searchParams) throws ParseException;

	void updateSystemUserName(SystemUser systemUser);

	void addSystemUser(SystemUser systemUser);

	void deleteByIdUserData(Long id);


}

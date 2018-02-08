package com.nokia.ices.apps.fusion.userdata.service;


import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.userdata.domain.UserBacthLog;

public interface BacthUserDataService{

	 public abstract Long addBacthUserData(String[] paramArrayOfString);

	 public abstract Page<UserBacthLog> findUserBacthLogPageBySearch(Map<String, Object> paramMap, ShiroUser paramShiroUser, Pageable paramPageable);
}

package com.nokia.ices.apps.fusion.system.service;

import com.nokia.ices.apps.fusion.system.domain.UserHistoryAccount;

public interface UserHistoryAccountService {
	
	/**
	 * 根据给出用户ID，密码，判断是否与历史5次的密码相同
	 * @param userId
	 * @param newPasswd
	 * @return
	 */
	public UserHistoryAccount whetherHasRepeatAccount(Long userId, String newPasswd);
	

}

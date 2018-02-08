package com.nokia.ices.apps.fusion.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.system.domain.UserHistoryAccount;
import com.nokia.ices.apps.fusion.system.repository.SystemUserRepository;
import com.nokia.ices.apps.fusion.system.repository.UserHistoryAccountRepository;
import com.nokia.ices.apps.fusion.system.service.UserHistoryAccountService;


@Service("userHistoryAccountService")
public class UserHistoryAccountServiceImpl implements UserHistoryAccountService {
	
	@Autowired
	UserHistoryAccountRepository userHistoryAccountRepository;
	
	@Autowired
	SystemUserRepository systemUserRepository;
	@Override
	public UserHistoryAccount whetherHasRepeatAccount(Long userId, String newPasswd) {
		UserHistoryAccount userHistoryAccount = userHistoryAccountRepository.findByUser(systemUserRepository.findOne(userId));
		return userHistoryAccount;
	}

	
}

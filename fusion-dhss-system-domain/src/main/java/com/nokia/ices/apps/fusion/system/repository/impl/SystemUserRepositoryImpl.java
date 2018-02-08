package com.nokia.ices.apps.fusion.system.repository.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nokia.ices.apps.fusion.system.domain.SystemUser;
import com.nokia.ices.apps.fusion.system.repository.SystemUserRepositoryCustom;

public class SystemUserRepositoryImpl implements SystemUserRepositoryCustom  {
    @Override
    public Page<SystemUser> findUserByCreator(SystemUser user,Pageable pageable){
        System.out.println(pageable.toString());
        return null;
    }

}

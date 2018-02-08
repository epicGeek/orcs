package com.nokia.ices.apps.fusion.system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nokia.ices.apps.fusion.system.domain.SystemUser;

public interface SystemUserRepositoryCustom {
    Page<SystemUser> findUserByCreator(SystemUser user, Pageable pageable);
}

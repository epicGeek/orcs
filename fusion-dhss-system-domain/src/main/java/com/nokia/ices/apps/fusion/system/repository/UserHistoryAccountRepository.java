package com.nokia.ices.apps.fusion.system.repository;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.system.domain.SystemUser;
import com.nokia.ices.apps.fusion.system.domain.UserHistoryAccount;

@RepositoryRestResource(collectionResourceRel = "user-history-account", path = "user-history-account", itemResourceRel = "user-history-account")
public interface UserHistoryAccountRepository extends CrudRepository<UserHistoryAccount, Long>, JpaSpecificationExecutor<UserHistoryAccount>{
	UserHistoryAccount findByUser(SystemUser user);
}

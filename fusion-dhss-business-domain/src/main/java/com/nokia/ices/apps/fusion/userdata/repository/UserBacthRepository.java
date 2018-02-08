package com.nokia.ices.apps.fusion.userdata.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.userdata.domain.UserBacthLog;

@RepositoryRestResource(collectionResourceRel="userBacth-item", path="userBacth-item", itemResourceRel="userBacth-item")
public interface UserBacthRepository extends JpaRepository<UserBacthLog, Long>,JpaSpecificationExecutor<UserBacthLog>{

	
}


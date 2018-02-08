package com.nokia.ices.apps.fusion.userdata.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.nokia.ices.apps.fusion.userdata.domain.UserDataLog;

@RepositoryRestResource(collectionResourceRel = "userData-item", path = "userData-item" ,itemResourceRel="userData-item")
public interface UserDataRepository extends JpaRepository<UserDataLog, Long>,JpaSpecificationExecutor<UserDataLog>{

	
}


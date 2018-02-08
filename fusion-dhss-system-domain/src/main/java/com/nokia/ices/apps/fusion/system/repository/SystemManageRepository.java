package com.nokia.ices.apps.fusion.system.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.system.domain.SystemManage;

@RepositoryRestResource(collectionResourceRel = "system-manage", path = "system-manage" ,itemResourceRel="system-manage" )
public interface SystemManageRepository extends CrudRepository<SystemManage, Long>,JpaSpecificationExecutor<SystemManage>{

}

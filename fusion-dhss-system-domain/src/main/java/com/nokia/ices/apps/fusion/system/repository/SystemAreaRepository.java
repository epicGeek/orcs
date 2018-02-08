package com.nokia.ices.apps.fusion.system.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.system.domain.SystemArea;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;

@RepositoryRestResource(collectionResourceRel = "system-area", path = "system-area" ,itemResourceRel="system-area" )
public interface SystemAreaRepository extends CrudRepository<SystemArea, Long>,JpaSpecificationExecutor<SystemArea>{
    Collection<SystemArea> findAreaBySystemRoleIn(Collection<SystemRole> roleSet);
}

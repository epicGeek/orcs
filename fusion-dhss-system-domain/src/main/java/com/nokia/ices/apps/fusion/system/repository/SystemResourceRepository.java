package com.nokia.ices.apps.fusion.system.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.system.domain.SystemResource;
import com.nokia.ices.apps.fusion.system.domain.SystemResourceType;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;

@RepositoryRestResource(collectionResourceRel = "system-resource", path = "system-resource" ,itemResourceRel="system-resource")
public interface SystemResourceRepository extends CrudRepository<SystemResource, Long>,  JpaSpecificationExecutor<SystemResource>{
	
    List<SystemResource> findSystemResourceByResourceType(SystemResourceType resourceType);

    List<SystemResource> findSystemResourceBySystemRoleInAndResourceTypeEquals(Collection<SystemRole> roleSet,SystemResourceType resourceType);

    Iterable<SystemResource> findSystemResourceBySystemRoleIn(Iterable<SystemRole> roleList);
    
    List<SystemResource> findSystemResourceBySystemRoleIdEquals(Long roleId);
}

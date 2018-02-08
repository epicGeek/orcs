package com.nokia.ices.apps.fusion.system.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.system.domain.SystemRole;

@RepositoryRestResource(collectionResourceRel = "system-role", path = "system-role" ,itemResourceRel="system-role")
public interface SystemRoleRepository extends CrudRepository<SystemRole, Long> , JpaSpecificationExecutor<SystemRole> {
    
	public Page<SystemRole> findPageByRoleNameContains(@Param("q") String keyWord, Pageable pageable);

	public List<SystemRole> findListByRoleNameContains(@Param("q") String keyWord, Sort sort);
	

    public List<SystemRole> findListBy(@Param("q") String keyWord, Sort sort);
	
    public List<SystemRole> findRoleBySystemUserUserName(@Param("q") String userName);
    
    public SystemRole findOneBySystemUserUserName(@Param("q") String userName);

}

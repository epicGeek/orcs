package com.nokia.ices.apps.fusion.system.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.system.domain.SystemUser;

@RepositoryRestResource(collectionResourceRel = "system-user", path = "system-user", itemResourceRel = "system-user")
public interface SystemUserRepository extends CrudRepository<SystemUser, Long>, JpaSpecificationExecutor<SystemUser>,SystemUserRepositoryCustom {

    public List<SystemUser> findListByUserName(@Param("q") String userName, Sort sort);

    public List<SystemUser> findListByRealNameContains(String userName, Sort sort);
    
    public SystemUser findOneByUserName(@Param("q") String userName);

    public Page<SystemUser> findPageByUserName(@Param("user") String userName, Pageable page);

    public List<SystemUser> findUserBySystemRoleIn(Collection<SystemRole> roleSet);

}

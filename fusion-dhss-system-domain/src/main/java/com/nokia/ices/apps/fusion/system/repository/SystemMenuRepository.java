package com.nokia.ices.apps.fusion.system.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.system.domain.SystemMenu;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;

@RepositoryRestResource(collectionResourceRel = "system-menu", path = "system-menu", itemResourceRel = "system-menu")
public interface SystemMenuRepository extends CrudRepository<SystemMenu, Long>, JpaSpecificationExecutor<SystemMenu> {
    public List<SystemMenu> findAllBySystemRoleId(@Param("q") Long roleIds,Sort sort);
    
    public List<SystemMenu> findMenuBySystemRoleIn(Collection<SystemRole> roleSet);

    public SystemMenu findOneByMenuCode(String menuCode);
    
    public List<SystemMenu> findByParentMenuName(String menuCode);
}

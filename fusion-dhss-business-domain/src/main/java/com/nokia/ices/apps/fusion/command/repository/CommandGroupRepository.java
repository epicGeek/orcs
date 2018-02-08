package com.nokia.ices.apps.fusion.command.repository;


import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.command.domain.CommandGroup;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;


@RepositoryRestResource(collectionResourceRel = "command-group", path = "command-group" ,itemResourceRel="command-group")
public interface CommandGroupRepository extends CrudRepository<CommandGroup, Long>,JpaSpecificationExecutor<CommandGroup>{
    List<CommandGroup> findCommandGroupBySystemRoleIn(Collection<SystemRole> systemRoleSet);
}

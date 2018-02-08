package com.nokia.ices.apps.fusion.command.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.command.domain.CommandCheckItem;
import com.nokia.ices.apps.fusion.command.domain.CommandGroup;
import com.nokia.ices.apps.fusion.command.domain.types.CommandCategory;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckJob;

@RepositoryRestResource(collectionResourceRel = "check-item", path = "check-item",itemResourceRel="check-item")
public interface CommandCheckItemRepository  extends CrudRepository<CommandCheckItem, Long>,JpaSpecificationExecutor<CommandCheckItem>{
    public List<CommandCheckItem> findListByCommandGroupIdEquals(@Param("group_id") Long group_id);
    @Query("select checkItem from CommandCheckItem checkItem")
    public List<CommandCheckItem> findAll();
    
    public List<CommandCheckItem> findListByIdIn(Set<Long> ids);
    
    public List<CommandCheckItem> findListBySmartCheckJob(SmartCheckJob smartCheckJob);
    
    public List<CommandCheckItem> findListByCommandGroupIn(Collection<CommandGroup> commandGrouplist);
    
    public List<CommandCheckItem> findListByCategory(@Param("q") CommandCategory categoryName);
    
    public CommandCheckItem findCommandCheckItemByName(@Param("q")String name);
    
    public List<CommandCheckItem> findCommandCheckItemByNameLike(@Param("q")String name);
}

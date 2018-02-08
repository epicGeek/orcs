package com.nokia.ices.apps.fusion.command.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.nokia.ices.apps.fusion.command.domain.CommandCheckItem;
import com.nokia.ices.apps.fusion.command.domain.CommandGroup;
import com.nokia.ices.apps.fusion.monitor.domain.MonitorTable;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;

public interface CommandService {
	Page<CommandCheckItem> findCommandCheckItemPageBySearch(String searchField,SystemRole currentUserRole,Pageable pageable);
	Page<CommandCheckItem> findCommandCheckItemPageBySearch(String searchField,String searchField01,SystemRole currentUserRole,Pageable pageable);

	List<CommandCheckItem> findCommandCheckItemListByCreator(Map<String, Object> searchParams, List<String> sortType);
    List<CommandCheckItem> findCheckItemByCreator();
    Page<CommandGroup> findCommandGroupPageBySearch(String searschField,SystemRole currentUserRole, Pageable pageable);
    List<CommandGroup> findCommandGroupListBySearch(String searchField,SystemRole currentUserRole, Sort sort);
    List<CommandGroup> findCommandGroupListByCreator(Map<String, Object> searchParams, List<String> sortType);
    List<CommandGroup> findCommandGroupList(String unitTypeId,String neTypeId);
	List<CommandCheckItem> findCommandCheckItemBySearch(String searchField);
	List<CommandCheckItem> findCommandCheckItemByGroup(Long group_id);
	List<CommandCheckItem> findCheckItemByNeTypeAndUnitType(List<CommandGroup> CommandGroupSet, String cmdType,String categoryId, String itemName);
    List<CommandGroup> findCommandGroupList(SystemRole systemRole);
    Page<MonitorTable> findMonitorTablePageByFilter(Map<String,Object> paramMap,  Pageable pageable);
   
}

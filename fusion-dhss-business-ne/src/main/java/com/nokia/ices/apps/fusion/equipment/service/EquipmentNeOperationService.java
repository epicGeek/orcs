package com.nokia.ices.apps.fusion.equipment.service;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNeOperationLog;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;


/*
 * 网元操作日志业务接口
 * */
public interface EquipmentNeOperationService{

	Page<EquipmentNeOperationLog> findNeLogPageBySearch(Map<String,Object> searchParams,ShiroUser shiroUser, Pageable pageable);
	
    List<EquipmentNeOperationLog> findNeLogListByCreator(Map<String, Object> searchParams,ShiroUser shiroUser, Sort sort);
    
    List<EquipmentNeOperationLog> findAll();


}

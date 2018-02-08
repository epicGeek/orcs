package com.nokia.ices.apps.fusion.equipment.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNumberSection;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentWebInterface;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;


public interface EquipmentService {

    public static final String DESQ_NAME_UNIT = "DHLR-UNIT";
    
    public static final String DESQ_NE_LOG = "DHLR-NELOG";
    
    public static final String UNIT_ADD = "0";
    public static final String UNIT_DELETE = "1";
    public static final String UNIT_MODIFY = "2";
    
    
    
    Map<String,List<String>> getOptionMap(String dhssName,String nodeName,String neType,String unitType,String neName);
	List<EquipmentNumberSection> findEquipmentNumberSectionByAreaID(String searchField, String type, Long area_id);
	List<EquipmentWebInterface> findEquipmentWebInterfaceByUnitID(Long  unit_id);
	
	
    List<EquipmentNumberSection> findEquipmentNumberSectionListBySearchFilter(SystemRole currentUserRole,Sort sort);
    Page<EquipmentNumberSection> findEquipmentNumberSectionPageBySearchFilter(Map<String, Object> searchParams,SystemRole currentUserRole,Pageable pageable);

    List<EquipmentNe> findEquipmentNeListBySearchFilter(SystemRole currentUserRole,Sort sort);
    Page<EquipmentNe> findEquipmentNePageBySearchFilter(Map<String, Object> searchParams,SystemRole currentUserRole, Pageable pageable);
    
    List<EquipmentUnit> findEquipmentUnitListBySearchFilter(Map<String, Object> searchParams,SystemRole currentUserRole,Sort sort);

    List<EquipmentUnit> findEquipmentUnitListBySearchFilter(SystemRole currentUserRole,Sort sort);
    Page<EquipmentUnit> findEquipmentUnitPageBySearchFilter(Map<String, Object> searchParams,SystemRole currentUserRole,Pageable pageable);
    List<EquipmentUnit> findEquipmentUnitListBySearchFilter(Map<String, Object> searchParams, ShiroUser shiroUser,Sort sort);
   
}

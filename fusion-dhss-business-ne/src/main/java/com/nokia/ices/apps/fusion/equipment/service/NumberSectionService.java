package com.nokia.ices.apps.fusion.equipment.service;

import java.util.List;

import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNumberSection;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;

public interface NumberSectionService {
    public List<EquipmentUnit> findAllUnitTypeByNumberSection(String ns, String type, String unitTypeName, ShiroUser shiroUser);
    public List<EquipmentNumberSection> findEquipmentNumberSectionListByAreaAreaCodeEquals(String areaCode);
}
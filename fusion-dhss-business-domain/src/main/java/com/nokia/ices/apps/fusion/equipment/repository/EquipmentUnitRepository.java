package com.nokia.ices.apps.fusion.equipment.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckJob;

@RepositoryRestResource(collectionResourceRel = "equipment-unit", path = "equipment-unit" ,itemResourceRel="equipment-unit")
public interface EquipmentUnitRepository extends CrudRepository<EquipmentUnit, Long>,JpaSpecificationExecutor<EquipmentUnit>{
    Set<EquipmentUnit> findListBySmartCheckJob(SmartCheckJob smartCheckJob);

    List<EquipmentUnit> findListByUnitNameIn(Set<String> unitNameSet);
    
    List<EquipmentUnit> findListByNeNumberSectionIdInAndUnitTypeEquals(List<Long> matchIdList,EquipmentUnitType unitType);
    
    List<EquipmentUnit> findListByUnitTypeEquals(EquipmentUnitType unitType);
    
    EquipmentUnit findEquipmentUnitByUuId(@Param("q") String UuId);
    
    EquipmentUnit findEquipmentUnitByUnitName(@Param("q") String unitName);
}

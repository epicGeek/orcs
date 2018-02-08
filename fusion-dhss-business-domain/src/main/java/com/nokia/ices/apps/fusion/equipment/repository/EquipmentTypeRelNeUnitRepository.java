package com.nokia.ices.apps.fusion.equipment.repository;


import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.equipment.domain.EquipmentTypeRelNeUnit;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentNeType;

@RepositoryRestResource(collectionResourceRel = "equipment-type-rel-ne-unit", path = "equipment-type-rel-ne-unit" ,itemResourceRel="equipment-type-rel-ne-unit")
public interface EquipmentTypeRelNeUnitRepository extends CrudRepository<EquipmentTypeRelNeUnit, Long>,JpaSpecificationExecutor<EquipmentTypeRelNeUnit>{
	List<EquipmentTypeRelNeUnit> findByNeType(@Param("neType") EquipmentNeType neType,Sort sort);
}

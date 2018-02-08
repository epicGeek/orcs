package com.nokia.ices.apps.fusion.equipment.repository;


import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNumberSection;
import com.nokia.ices.apps.fusion.system.domain.SystemArea;

@RepositoryRestResource(collectionResourceRel = "equipment-number-section", path = "equipment-number-section",itemResourceRel = "equipment-number-section")
public interface EquipmentNumberSectionRepository extends CrudRepository<EquipmentNumberSection, Long>, JpaSpecificationExecutor<EquipmentNumberSection> {
    List<EquipmentNumberSection> findEquipmentNumberSectionListByNeIdEquals(@Param("ne_id") Long ne_id,Sort sort);
    List<EquipmentNumberSection> findEquipmentNumberSectionListByAreaAreaCodeEquals(@Param("areaCode") String areaCode);
	List<EquipmentNumberSection> findEquipmentNumberSectionListByAreaIn(Iterable<SystemArea> iterArea);
}

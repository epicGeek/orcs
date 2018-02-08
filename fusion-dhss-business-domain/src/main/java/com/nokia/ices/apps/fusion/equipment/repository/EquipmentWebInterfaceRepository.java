package com.nokia.ices.apps.fusion.equipment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentWebInterface;

@RepositoryRestResource(collectionResourceRel = "equipment-webinterface", path = "equipment-webinterface" ,itemResourceRel="equipment-webinterface")
public interface EquipmentWebInterfaceRepository  extends CrudRepository<EquipmentWebInterface, Long>,JpaSpecificationExecutor<EquipmentWebInterface>{

	
	List<EquipmentWebInterface> findByUnit(@Param("unit") EquipmentUnit unit);
}

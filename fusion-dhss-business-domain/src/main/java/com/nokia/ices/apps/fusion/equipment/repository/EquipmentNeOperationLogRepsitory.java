package com.nokia.ices.apps.fusion.equipment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNeOperationLog;

@RepositoryRestResource(collectionResourceRel = "equipment-ne-log", path = "equipment-ne-log" ,itemResourceRel="equipment-ne-log")
public interface EquipmentNeOperationLogRepsitory extends JpaRepository<EquipmentNeOperationLog, Long>,JpaSpecificationExecutor<EquipmentNeOperationLog>{

    public EquipmentNeOperationLog findByUnitNameEquals(@Param("q")String unitName);
}

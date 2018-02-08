package com.nokia.ices.apps.fusion.ems.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.ems.domain.EquipmentNodeGroup;

@RepositoryRestResource(collectionResourceRel = "equipment-node-group", path = "equipment-node-group")
public interface EquipmentNodeGroupRepository extends CrudRepository<EquipmentNodeGroup, Long> {
    List<EquipmentNodeGroup> findAllByGroupNameLike(@Param("gname") String gname,Sort sort);
}

package com.nokia.ices.apps.fusion.ems.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.ems.domain.EmsMutedItem;

@RepositoryRestResource(collectionResourceRel = "ems-muted-item", path = "ems-muted-item")
public interface EmsMutedItemRepository extends CrudRepository<EmsMutedItem, Long> , JpaSpecificationExecutor<EmsMutedItem>{

}

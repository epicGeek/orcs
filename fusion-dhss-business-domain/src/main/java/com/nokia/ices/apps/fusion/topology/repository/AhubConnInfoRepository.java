package com.nokia.ices.apps.fusion.topology.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.topology.domain.AhubConnInfo;

@RepositoryRestResource(collectionResourceRel = "ahub-conn-info", path = "ahub-conn-info" ,itemResourceRel="ahub-conn-info")
public interface AhubConnInfoRepository extends CrudRepository<AhubConnInfo, Long>,JpaSpecificationExecutor<AhubConnInfo>{
	List<AhubConnInfo> findByAhubName(@Param("ahubName") String ahubName);
}

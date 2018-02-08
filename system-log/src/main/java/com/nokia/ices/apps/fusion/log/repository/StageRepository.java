package com.nokia.ices.apps.fusion.log.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.nokia.ices.apps.fusion.log.domain.Stage;

/**
 * 产品阶段
 * @author Administrator
 */
@RepositoryRestResource(collectionResourceRel = "stage", path = "stage" ,itemResourceRel="stage")
public interface StageRepository extends JpaRepository<Stage, Long>,JpaSpecificationExecutor<Stage> {

}
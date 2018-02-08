package com.nokia.ices.apps.fusion.patrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckResultTmp;


@RepositoryRestResource(collectionResourceRel="smart-check-result-tmp",path="smart-check-result-tmp",itemResourceRel="smart-check-result-tmp")
public interface SmartCheckResultTmpRepository extends JpaRepository<SmartCheckResultTmp, Long>,JpaSpecificationExecutor<SmartCheckResultTmp> {

}

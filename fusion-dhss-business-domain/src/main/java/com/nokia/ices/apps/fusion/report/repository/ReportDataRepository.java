package com.nokia.ices.apps.fusion.report.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.report.domain.ReportDataConf;

@RepositoryRestResource(collectionResourceRel = "report-repository", path = "report-repository" ,itemResourceRel="report-repository")
public interface ReportDataRepository extends CrudRepository<ReportDataConf, Long>,JpaSpecificationExecutor<ReportDataConf> {
	
}

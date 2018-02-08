package com.nokia.ices.apps.fusion.report.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.report.domain.ReportTemplate;

@RepositoryRestResource(collectionResourceRel = "report-template", path = "report-template" ,itemResourceRel="report-template")
public interface ReportTemplateRepository extends CrudRepository<ReportTemplate, Long>,JpaSpecificationExecutor<ReportTemplate>  {

}

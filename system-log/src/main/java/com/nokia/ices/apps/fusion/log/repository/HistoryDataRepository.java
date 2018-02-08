package com.nokia.ices.apps.fusion.log.repository;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.nokia.ices.apps.fusion.log.domain.HistoryData;
import com.nokia.ices.apps.fusion.log.domain.Product;
import com.nokia.ices.apps.fusion.log.domain.Project;
import com.nokia.ices.apps.fusion.log.domain.Stage;
import com.nokia.ices.apps.fusion.log.domain.WorkPackage;
import com.nokia.ices.apps.fusion.log.domain.WorkType;


@RepositoryRestResource(collectionResourceRel = "log-history", path = "log-history" ,itemResourceRel="log-history")
public interface HistoryDataRepository extends CrudRepository<HistoryData, Long>,JpaSpecificationExecutor<HistoryData> {

	@Modifying
	@Query("update HistoryData h set h.product=:product,h.modular=:modular,h.projectName=:projectName,"
	    + "h.stage=:stage,h.workPackageType=:workPackageType,h.workPackage = :workPackage,h.time = :time,"
	    + "h.jobOperator = :jobOperator where h.id =:id")
	void updateHistoryDataName(@Param("product")Product product,@Param("modular")String modular,@Param("projectName")Project projectName,
			@Param("stage")Stage stage,@Param("workPackageType")WorkType workPackageType,@Param("workPackage")WorkPackage workPackage,
			@Param("time")Double time,@Param("jobOperator")String jobOperator,@Param("id")Long id);
	
	
}
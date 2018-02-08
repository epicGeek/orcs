package com.nokia.ices.apps.fusion.log.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.nokia.ices.apps.fusion.log.domain.WorkPackage;
import com.nokia.ices.apps.fusion.log.domain.WorkType;

/**
 * 工作包
 * @author Administrator
 */
@RepositoryRestResource(collectionResourceRel = "workPackage", path = "workPackage" ,itemResourceRel="workPackage")
public interface WorkPackageRepository extends JpaRepository<WorkPackage, Long>,JpaSpecificationExecutor<WorkPackage> {

	@Query("SELECT id,workPackage FROM WorkPackage w WHERE w.workType=?1")
	List<WorkPackage> findByWorkType(WorkType worktype);

}
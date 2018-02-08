package com.nokia.ices.apps.fusion.log.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.nokia.ices.apps.fusion.log.domain.Project;

/**
 * 项目
 * @author Administrator
 */
@RepositoryRestResource(collectionResourceRel = "project", path = "project" ,itemResourceRel="project")
public interface ProjectRepository extends JpaRepository<Project, Long>,JpaSpecificationExecutor<Project> {
    // order by convert(projectName USING gbk) COLLATE gbk_chinese_ci,convert(cbt USING gbk) COLLATE gbk_chinese_ci 
	//@Query("select id,projectName from Project")
	//public List<Project> findAll();
}
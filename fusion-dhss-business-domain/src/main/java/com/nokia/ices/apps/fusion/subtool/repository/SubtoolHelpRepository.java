package com.nokia.ices.apps.fusion.subtool.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.nokia.ices.apps.fusion.subtool.domain.SubtoolHelp;


@RepositoryRestResource(collectionResourceRel = "subtool-help", path = "subtool-help" ,itemResourceRel="subtool-help")
public interface SubtoolHelpRepository extends JpaRepository<SubtoolHelp, Long>,JpaSpecificationExecutor<SubtoolHelp>{

	
}


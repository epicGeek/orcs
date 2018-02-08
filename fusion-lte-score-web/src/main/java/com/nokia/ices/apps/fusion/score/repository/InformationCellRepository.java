package com.nokia.ices.apps.fusion.score.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.score.domain.InformationCell;

@RepositoryRestResource(collectionResourceRel = "cell", path = "cell" ,itemResourceRel="cell")
public interface InformationCellRepository extends JpaRepository<InformationCell, Long>,JpaSpecificationExecutor<InformationCell>{

}

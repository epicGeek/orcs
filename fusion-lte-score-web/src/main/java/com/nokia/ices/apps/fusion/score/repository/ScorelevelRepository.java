package com.nokia.ices.apps.fusion.score.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.score.domain.Scorelevel;

@RepositoryRestResource(collectionResourceRel = "scorelevel", path = "scorelevel" ,itemResourceRel="scorelevel")
public interface ScorelevelRepository extends JpaRepository<Scorelevel, Long>,JpaSpecificationExecutor<Scorelevel> {

}

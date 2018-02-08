package com.nokia.ices.apps.fusion.score.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.score.domain.IndexScoringRule;


@RepositoryRestResource(collectionResourceRel = "kpiIndex", path = "kpiIndex" ,itemResourceRel="kpiIndex")
public interface IndexScoringRuleRepository extends JpaRepository<IndexScoringRule, Long>,JpaSpecificationExecutor<IndexScoringRule>{

}

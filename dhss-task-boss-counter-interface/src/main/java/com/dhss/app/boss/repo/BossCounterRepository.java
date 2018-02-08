package com.dhss.app.boss.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.dhss.app.boss.domain.BossCounter;



@RepositoryRestResource(collectionResourceRel = "boss-counter", path = "boss-counter" ,itemResourceRel="boss-counter")
public interface BossCounterRepository extends CrudRepository<BossCounter, Long>,JpaSpecificationExecutor<BossCounter>{

}
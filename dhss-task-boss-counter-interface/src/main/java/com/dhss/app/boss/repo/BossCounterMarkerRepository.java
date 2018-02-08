package com.dhss.app.boss.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.dhss.app.boss.domain.BossCounterMarker;



@RepositoryRestResource(collectionResourceRel = "boss-counter-marker", path = "boss-counter-marker" ,itemResourceRel="boss-counter-marker")
public interface BossCounterMarkerRepository extends CrudRepository<BossCounterMarker, Long>,JpaSpecificationExecutor<BossCounterMarker>{
	public BossCounterMarker findBySoapGwName(String soapGwName);
}
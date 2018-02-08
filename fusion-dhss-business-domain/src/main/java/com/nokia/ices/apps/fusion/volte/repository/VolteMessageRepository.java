package com.nokia.ices.apps.fusion.volte.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.volte.domain.VolteMessage;

@RepositoryRestResource(collectionResourceRel = "volte-message", path = "volte-message" ,itemResourceRel="volte-message")
public interface VolteMessageRepository extends JpaRepository<VolteMessage, Long>,JpaSpecificationExecutor<VolteMessage>{

}

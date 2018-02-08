package com.nokia.ices.apps.fusion.emergency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.emergency.domian.EmergencySecurityState;

@RepositoryRestResource(collectionResourceRel = "emergency-security-state", path = "emergency-security-state" ,itemResourceRel="emergency-security-state")
public interface EmergencySecurityStateRepository extends JpaRepository<EmergencySecurityState, Long>,JpaSpecificationExecutor<EmergencySecurityState>{

}

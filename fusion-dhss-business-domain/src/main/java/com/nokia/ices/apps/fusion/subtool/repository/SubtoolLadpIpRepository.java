package com.nokia.ices.apps.fusion.subtool.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.nokia.ices.apps.fusion.subtool.domain.SubtoolPgwLdapIp;


@RepositoryRestResource(collectionResourceRel = "subtool-ldap", path = "subtool-ldap" ,itemResourceRel="subtool-ldap")
public interface SubtoolLadpIpRepository extends JpaRepository<SubtoolPgwLdapIp, Long>,JpaSpecificationExecutor<SubtoolPgwLdapIp>{

	
}


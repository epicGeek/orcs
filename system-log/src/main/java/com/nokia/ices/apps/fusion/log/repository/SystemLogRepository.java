package com.nokia.ices.apps.fusion.log.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.log.domain.Product;

/**
 * 产品
 * @author Administrator
 */
@RepositoryRestResource(collectionResourceRel = "systemLog", path = "systemLog" ,itemResourceRel="systemLog")
public interface SystemLogRepository extends JpaRepository<Product, Long>,JpaSpecificationExecutor<Product> {

}
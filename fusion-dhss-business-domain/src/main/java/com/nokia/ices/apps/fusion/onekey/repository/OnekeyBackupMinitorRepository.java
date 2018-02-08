package com.nokia.ices.apps.fusion.onekey.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.onekey.domian.OnekeyBackupMinitor;

@RepositoryRestResource(collectionResourceRel = "onekey-backup-minitor", path = "onekey-backup-minitor" ,itemResourceRel="onekey-backup-minitor")
public interface OnekeyBackupMinitorRepository extends CrudRepository<OnekeyBackupMinitor,Long> ,JpaSpecificationExecutor<OnekeyBackupMinitor> {

}

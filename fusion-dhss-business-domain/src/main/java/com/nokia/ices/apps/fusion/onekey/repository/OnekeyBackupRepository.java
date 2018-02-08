package com.nokia.ices.apps.fusion.onekey.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.onekey.domian.OnekeyBackup;

@RepositoryRestResource(collectionResourceRel = "onekey-backup", path = "onekey-backup" ,itemResourceRel="onekey-backup")
public interface OnekeyBackupRepository extends CrudRepository<OnekeyBackup,Long> ,JpaSpecificationExecutor<OnekeyBackup> {

}

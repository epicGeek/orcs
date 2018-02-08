package com.nokia.ices.apps.fusion.console.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.console.domain.ConsoleLog;


@RepositoryRestResource(collectionResourceRel = "console-log", path = "console-log" ,itemResourceRel="console-log")
public interface ConsoleLogRepository extends CrudRepository<ConsoleLog, Long>,JpaSpecificationExecutor<ConsoleLog>{
    Page<ConsoleLog> findConsoleLogByLoginUserNameAndLoginUnitName(@Param("userName") String userName,@Param("unitName") String unitName,Pageable page);
    List<ConsoleLog> findTop500ConsoleLogByLoginUnitName(@Param("unitName") String unitName,Sort sort);

}

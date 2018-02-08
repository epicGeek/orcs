package com.nokia.ices.apps.fusion.log.repository;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nokia.ices.apps.fusion.system.domain.SystemUser;

//import com.nokia.ices.apps.fusion.log.domain.SystemUser;


@RepositoryRestResource(collectionResourceRel = "system-user", path = "system-user", itemResourceRel = "system-user")
public interface UserManagerRepository extends CrudRepository<SystemUser, Long>, JpaSpecificationExecutor<SystemUser>{

	@Modifying
	@Query("update SystemUser s set s.realName=:realName,s.userName=:userName,s.mobile=:mobile,"
	    + "s.email=:email where s.id =:id")
	void updateUserDataName(@Param("realName")String string,@Param("userName")String userName,@Param("mobile")String mobile,
		@Param("email")String email,@Param("id")Long id);

	  /* //@Query("select realName from SystemUser ORDER BY CONVERT( realName USING GBK ) ASC")
		@Query("select realName from SystemUser")
		public List<Map<String,Object>> findUserNames();
		
	    public List<SystemUser> findListByUserName(@Param("q") String userName, Sort sort);

	    public List<SystemUser> findListByRealNameContains(String userName, Sort sort);
	    
	    public SystemUser findOneByUserName(@Param("q") String userName);

	    public Page<SystemUser> findPageByUserName(@Param("user") String userName, Pageable page);

	    public List<SystemUser> findUserBySystemRoleIn(Collection<SystemRole> roleSet);*/

}
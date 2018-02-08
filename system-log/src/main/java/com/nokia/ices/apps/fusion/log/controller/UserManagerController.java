package com.nokia.ices.apps.fusion.log.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.log.domain.HistoryData;
import com.nokia.ices.apps.fusion.log.service.UserManagerService;
import com.nokia.ices.apps.fusion.system.domain.SystemUser;

@RepositoryRestController
@RestController
public class UserManagerController {
	
	public static final Logger logger = LoggerFactory.getLogger(UserManagerController.class);
	
	@Autowired
	UserManagerService userManagerService;

	/**
	 * 用户数据
	 * @param iDisplayStart
	 * @param iDisplayLength
	 * @param userName
	 * @return
	 */
    @RequestMapping(value = "query/userData")
	public Map<String,Object> userManagerData(@RequestParam("start") Integer iDisplayStart, 
			@RequestParam("length") Integer iDisplayLength,@RequestParam("userName") String userName) {
    	
    	List<String> sortSet = new ArrayList<String>();
    	Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("userName", userName);
		sortSet.add("userName,asc");
		iDisplayStart= iDisplayStart/iDisplayLength+1;  //分页
		Map<String, Object> resultData = new HashMap<String, Object>();
		try{
			Page<SystemUser> page =userManagerService.findUserDataPageBySearch(searchParams, null, iDisplayStart, iDisplayLength, sortSet);
			resultData.put("data", page.getContent());
			resultData.put("iTotalRecords", page.getTotalElements());
			resultData.put("iTotalDisplayRecords", page.getTotalElements());
		}catch(Exception e){
    		logger.debug(" query userData is error:"+e.getMessage());
    	}
		return resultData;
	}
    
  /**
   *保存
   * @param systemUser
   * @return
   */
    @RequestMapping(value = "saveOrEditUser")
    public String saveLog(@RequestBody SystemUser []  systemUser){
    	
    	String result = "true";
    	try{
    		for(SystemUser his : systemUser){
    			String id = his.getId()!=null?his.getId() .toString():"";
    		//	his.setStatus(true);
    			if(StringUtils.isNotEmpty(id)){
    				his.setId(Long.parseLong(id));
    				userManagerService.updateSystemUserName(his);
    			}else{
    				userManagerService.addSystemUser(his);
    			}
    		}
    		result = "true";
    	}catch(Exception e){
    		logger.debug("save user  is error:"+e.getMessage());
    	}
    	return result;
    }
    
    /**
     * 删除
     * @param ids
     * @return
     */
    @RequestMapping(value = "deleteUser/{ids}")
    public Long deleteUser(@PathVariable String ids){
    	
    	try{
    		String [] arrIds  = ids.split(",");
    		for(String id: arrIds){
    			userManagerService.deleteByIdUserData(Long.parseLong(id));
    		}
    		return 0L;
    	}catch(Exception e){
    		logger.debug("delete user is error:"+e.getMessage());
    	}
    	return 1L;
    }
	
}

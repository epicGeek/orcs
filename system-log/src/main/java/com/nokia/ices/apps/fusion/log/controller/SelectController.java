package com.nokia.ices.apps.fusion.log.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.log.domain.WorkPackage;
import com.nokia.ices.apps.fusion.log.service.SelectService;

@RequestMapping("/select")
@RestController
public class SelectController {

	public static final Logger logger = LoggerFactory.getLogger(SelectController.class);

	@Autowired
	SelectService selectService;
	
    @RequestMapping(value = "/infoAll")
    public List<?> getinfoAll(@RequestParam("type")String type) {
        
    	try{
    		return  selectService.queryByListAll(type);
		}catch(Exception e){
			logger.debug("select/product is error ........"+e.getMessage());
		}
		return null;
        
    }
    
    @RequestMapping(value = "/workPakageList")
    public List<WorkPackage> getworkPakageList(@RequestParam("workTypeId")Long workTypeId) {
        
    	try{
    		return  selectService.queryByWorkPackageList(workTypeId);
		}catch(Exception e){
			logger.debug("select/workType is error ........"+e.getMessage());
		}
		return null;
        
    }
    
}

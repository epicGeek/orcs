package com.nokia.ices.apps.fusion.console.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.console.domain.ConsoleLog;
import com.nokia.ices.apps.fusion.service.ConsoleLogService;

@RepositoryRestController
@RestController
public class ConsoleLogController {

    @Autowired
    private ConsoleLogService consoleLogService;
    
    @SuppressWarnings("rawtypes")
	@Autowired
    private PagedResourcesAssembler pagedResourcesAssembler;
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "equipment-console-log/search/searchByFilter")
    public PagedResources<ConsoleLog> getConsoleLogByUnitName(
    		@RequestParam(value="unitName",required=false) String unitName,
    		@RequestParam(value="userName",required=false) String userName,
    		Pageable pageable,
            PersistentEntityResourceAssembler assembler) {
//        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        // SystemRole systemRole = shiroUser.getRole();
        /*SystemRole systemRole = new SystemRole();*/
    	@SuppressWarnings("rawtypes")
		Map modelMap = new HashMap();
    	modelMap.put("loginUserName_LIKE", userName);
    	modelMap.put("loginUnitName_EQ", unitName);
        Page<ConsoleLog> page = consoleLogService.findConsoleLogByUnitName(modelMap, pageable);
        return pagedResourcesAssembler.toResource(page, assembler);
    }
}

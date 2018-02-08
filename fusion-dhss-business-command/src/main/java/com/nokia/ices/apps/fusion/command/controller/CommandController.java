package com.nokia.ices.apps.fusion.command.controller;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.command.domain.CommandCheckItem;
import com.nokia.ices.apps.fusion.command.domain.CommandGroup;
import com.nokia.ices.apps.fusion.command.domain.types.CommandCategory;
import com.nokia.ices.apps.fusion.command.domain.types.SubtoolCmdType;
import com.nokia.ices.apps.fusion.command.repository.CommandGroupRepository;
import com.nokia.ices.apps.fusion.command.service.CommandService;
import com.nokia.ices.apps.fusion.monitor.domain.MonitorTable;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.system.domain.SystemOperationLog;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;


@RepositoryRestController
@RestController
public class CommandController {
    public static final Logger logger = LoggerFactory.getLogger(CommandController.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private CommandService commandService;

    @Autowired
    private CommandGroupRepository  commandGroupRepository;

    @SuppressWarnings("rawtypes")
    @Autowired
    PagedResourcesAssembler pagedResourcesAssembler;
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/monitor/table/search/searchByFilter")
    public PagedResources<SystemOperationLog> getSystemOperationLogList(
    		@RequestParam(value="startTime",required=false) String startTime,
    		@RequestParam(value="endTime",required=false) String endTime,
    		@RequestParam(value="fileName",required=false) String fileName,Pageable pageable,
            PersistentEntityResourceAssembler assembler) {
		//        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        // SystemRole systemRole = shiroUser.getRole();
       /* SystemRole systemRole = new SystemRole();*/
    	Map<String, Object> paramMap = new HashMap<String, Object>();

        paramMap.put("name_LIKE", fileName);
        try {
        	if(StringUtils.isNotBlank(startTime)){
    			paramMap.put("createDate_GE",sdf.parse(startTime));
        	}

        	if(StringUtils.isNotBlank(endTime)){
    			paramMap.put("createDate_LE",sdf.parse(endTime));
        	}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Page<MonitorTable> page = commandService.findMonitorTablePageByFilter(paramMap, pageable);
        return pagedResourcesAssembler.toResource(page, assembler);
    }
    
    @RequestMapping("/downloadMonitor")
    public void downloadMonitor(HttpServletRequest request,
			HttpServletResponse response) {
    	String name = request.getParameter("name");
    	String path = request.getParameter("path");
    	try {
			download(request, response, path, "application/octet-stream",name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 下载报表
     * @param request
     * @param response
     * @param downLoadPath
     * @param contentType
     * @param realName
     * @throws Exception
     */
    public static void download(HttpServletRequest request,HttpServletResponse response, String downLoadPath,
			String contentType, String realName) throws Exception {
		request.setCharacterEncoding("UTF-8");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		File file = new File(downLoadPath);
		response.setContentType(contentType);
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String(realName.getBytes("utf-8"), "GBK"));
		response.setHeader("Content-Length", String.valueOf(file.length()));
		bis = new BufferedInputStream(new FileInputStream(file));
		bos = new BufferedOutputStream(response.getOutputStream());
		byte[] buff = new byte[2048];
		int bytesRead;
		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
		bis.close();
		bos.close();
	}
    
    @RequestMapping("command-group/search/findCommandGroup")
    public List<CommandGroup> findCommandGroup() {
        Subject subject = SecurityUtils.getSubject();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        Iterable<CommandGroup> iter =  commandGroupRepository.findAll();
        List<CommandGroup> result = new ArrayList<CommandGroup>();
        for (CommandGroup commandGroup : iter) {
            if(subject.isPermitted("resource:"+commandGroup.getId()) || 
            		shiroUser.getUserName().equals((commandGroup.getCreateUser() == null ? "" : commandGroup.getCreateUser()))){
				result.add(commandGroup);
			}
		}
        return result;
    }



    @RequestMapping(value = "/subtool/check-item/search")
    public List<CommandCheckItem> getCheckItemSubtoolList() {
        List<CommandCheckItem> listItem = commandService.findCheckItemByCreator();
        return listItem;
    }

    @RequestMapping(value = "/check-item/search/list")
    public List<CommandCheckItem> getCheckItemList(Sort sort) {
        return null;
    }
    
    @RequestMapping(value = "/search/commandCategory")
    public Object[] getCommandCategory(Sort sort) {
        return CommandCategory.values();
    }
    
    
    @RequestMapping(value = "/search/commandCategoryMap")
    public List<Map<String,Object>> getCommandCategoryMap(Sort sort) {
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	Object [] array = CommandCategory.values();
    	for (Object object : array) {
    		Map<String,Object> map = new HashMap<String,Object>();
    		map.put("name", object.toString());
    		map.put("value", object);
    		list.add(map);
    	}
        return list;
    }
    
    @RequestMapping(value = "/category/search")
	public List<Map<String,Object>> getSubtoolType() {
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	Object [] array = SubtoolCmdType.values();
    	for (Object object : array) {
    		Map<String,Object> map = new HashMap<String,Object>();
    		map.put("name", object.toString());
    		map.put("value", object);
    		list.add(map);
    	}
        return list;
	}
    
    

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/check-item/search/page")
    public PagedResources<CommandCheckItem> getCheckItemList(
            @RequestParam(value = "searchField", required = false) String searchField,
            @RequestParam(value = "searchField01", required = false) String searchField01,
            Pageable pageable,
            PersistentEntityResourceAssembler assembler) {
    	 
    	 if (StringUtils.isNotEmpty(searchField01)) {
    		 Page<CommandCheckItem> page= commandService.findCommandCheckItemPageBySearch(searchField,searchField01, null, pageable);
    		 return pagedResourcesAssembler.toResource(page, assembler);
    	 }else{
    		 Page<CommandCheckItem> page= commandService.findCommandCheckItemPageBySearch(searchField, null, pageable);
    		 return pagedResourcesAssembler.toResource(page, assembler);
    	  }
        
    }


    @SuppressWarnings({"unused","unchecked"})
    @RequestMapping(value = "/command-group/search/page")
    public PagedResources<CommandGroup> getCommandGroupList(
            @RequestParam(value = "searchField", required = false) String searchField, Pageable pageable,
            PersistentEntityResourceAssembler assembler) {
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        // TODO SystemRole systemRole = shiroUser.getRole();
        List<String> sortSet = new ArrayList<String>();

        Page<CommandGroup> page = commandService.findCommandGroupPageBySearch(searchField, null, pageable);
        return pagedResourcesAssembler.toResource(page, assembler);
    }

    @RequestMapping(value = "/command-group/search/list")
    public List<CommandGroup> getCommandGroupByList(
            @RequestParam(value = "searchField", required = false) String searchField, Pageable pageable,
            PersistentEntityResourceAssembler assembler) {
//        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        SystemRole systemRole = null;
        List<CommandGroup> list = commandService.findCommandGroupListBySearch(searchField, systemRole, null);
        return list;
    }

}

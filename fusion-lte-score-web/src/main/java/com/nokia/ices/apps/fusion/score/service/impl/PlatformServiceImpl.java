package com.nokia.ices.apps.fusion.score.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.jpa.SearchFilter.Operator;
import com.nokia.ices.apps.fusion.score.domain.TreeNode;
import com.nokia.ices.apps.fusion.score.service.PlatformService;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.system.domain.SystemArea;
import com.nokia.ices.apps.fusion.system.domain.SystemMenu;
import com.nokia.ices.apps.fusion.system.domain.SystemResource;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.system.domain.SystemUser;
import com.nokia.ices.apps.fusion.system.repository.SystemAreaRepository;
import com.nokia.ices.apps.fusion.system.repository.SystemMenuRepository;
import com.nokia.ices.apps.fusion.system.repository.SystemResourceRepository;
import com.nokia.ices.apps.fusion.system.repository.SystemRoleRepository;
import com.nokia.ices.apps.fusion.system.repository.SystemUserRepository;

@Service("platformService")
public class PlatformServiceImpl implements PlatformService {

	@Autowired
	private SystemUserRepository systemUserRepository;

	@Autowired
	private SystemRoleRepository systemRoleRepository;
	
	@Autowired
	private SystemAreaRepository systemAreaRepository;
	
    @Autowired
    private SystemMenuRepository systemMenuRepository;
	
	@Autowired
    private SystemResourceRepository systemResourceRepository;

	@Override
	public Page<SystemUser> findSystemUserPageBySearch(Map<String, Object> searchParams, ShiroUser shiroUser,Pageable pageable) {
		
		String searchStr = searchParams.get("searchField")!=null?searchParams.get("searchField").toString():"";
		List<SearchFilter> searchFilter = new ArrayList<SearchFilter>(); 
 	    
 	    if(StringUtils.isNotEmpty(searchStr)){
 	    	 searchFilter.add(new SearchFilter("userName", Operator.LIKE, searchStr));
 	    	 searchFilter.add(new SearchFilter("realName", Operator.LIKE, searchStr));
 	    } 
         Specification<SystemUser> specUserItem = DynamicSpecifications.bySearchFilter(searchFilter,BooleanOperator.OR,SystemUser.class);  
         
         Page<SystemUser> pageList = systemUserRepository.findAll(specUserItem, pageable);
		
		return pageList;
	}


	@Override
	public List<SystemArea> findSystemAreaListByCreator(Map<String, Object> searchParams) {
		// TODO Auto-generated method stub
		String searchStr = searchParams.get("searchField")!=null?searchParams.get("searchField").toString():"";
        List<SearchFilter> searchFilter = new ArrayList<SearchFilter>(); 
 	    
 	    if(StringUtils.isNotEmpty(searchStr)){
 	    	 searchFilter.add(new SearchFilter("areaName", Operator.LIKE, searchStr));
 	    	 searchFilter.add(new SearchFilter("areaCode", Operator.LIKE, searchStr));
 	    } 
 	   Specification<SystemArea> specUserItem = DynamicSpecifications.bySearchFilter(searchFilter,BooleanOperator.OR,SystemArea.class);  
 	   return systemAreaRepository.findAll(specUserItem);
	}
	
	
	@Override
	public List<SystemUser> findSystemUserManageListByCreator(Map<String, Object> searchParams) {
		// TODO Auto-generated method stub
		
		return null;
		
	}

	/**
	 * 根据当前登录用户名获取系统权限
	 */
	   public List<String>  getCurrentUserArea(ShiroUser shiroUser) {
	        
	        List<String> resultList = new ArrayList<String>();

	        Collection<SystemRole> roleSet = systemRoleRepository.findRoleBySystemUserUserName(shiroUser.getUserName());
	        Assert.notNull(roleSet, "登录角色为空");
	        
	        Collection<SystemArea> areaList = systemAreaRepository.findAreaBySystemRoleIn(roleSet);
	        for (SystemArea systemArea : areaList) {
	            String code = systemArea.getAreaCode();
	            resultList.add(code);
	        }
	        return resultList;

	    }
	
	
	@SuppressWarnings("unchecked")
	public Map<String,List<SystemMenu>>  getcurrentUserAuthority(ShiroUser shiroUser) {
        
		//List<String> resultList = new ArrayList<String>();

        Collection<SystemRole> roleSet = systemRoleRepository.findRoleBySystemUserUserName(shiroUser.getUserName());
        Assert.notNull(roleSet, "登录角色为空");
        
    	Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		Map<String,List<SystemMenu>> menuResult = null;
		
		String key = shiroUser.getUserName()+"_menu";
		
		if(session.getAttribute(key)!=null){
			menuResult=  (Map<String,List<SystemMenu>>)session.getAttribute(key);
		}else{
			List<SystemMenu>  menuList = systemMenuRepository.findMenuBySystemRoleIn(roleSet);
			menuResult = new HashMap<String,List<SystemMenu>>();
			for(SystemMenu menu :menuList){
				  String menuStr =  menu.getMenuName();
				  String parentName = menuStr.split("-")[0];
				  //String nodeName = menuStr.split("-")[1];
				  if(menuResult.containsKey(parentName)){
					  menuResult.get(parentName).add(menu);
				  }else{
					  //不存在
					  List<SystemMenu> list = new ArrayList<>();
					  list.add(menu);
					  menuResult.put(parentName, list);
				  }
				  
			}
			
			
			session.setAttribute(key, menuResult);
		}
        
		return menuResult;

	}
		
	
	public void resourceByMenuList(SystemMenu systemMenu,Map<String,List<String>> resultMap) {
		    
		
		    String menu =  systemMenu.getMenuName();
		    String parentName = menu.split("-")[0];
		    String nodeName = menu.split("-")[1];
			String value = nodeName+":"+systemMenu.getId();
			if(resultMap.containsKey(parentName)){
				List<String>  oldList =  resultMap.get(parentName);
				oldList.add(value);
			}else{
				List<String>  menuList = new ArrayList<String>();
				menuList.add(value);
				resultMap.put(parentName, menuList);
			}
	}
			
		

	@Override
	public List<TreeNode> getMenuTree(String roleId, String id) {
		
		//返回数据树
		List<TreeNode>  resultTree = new ArrayList<>();
		
		//获取全部权限项目
		Map<String,List<String>> treeMap = new HashMap<String,List<String>>();
		Iterable<SystemResource>  resources =  systemResourceRepository.findAll();
		for (SystemResource rsource : resources) {
			if (rsource instanceof SystemMenu) {
                SystemMenu menuItem = (SystemMenu) rsource;
                resourceByMenuList(menuItem,treeMap);
			}
		}
		
		//获取当前角色已经选好的菜单项
		Map<String,List<String>> checkedTree = new HashMap<String,List<String>>();
		SystemRole role = systemRoleRepository.findOne(Long.parseLong(roleId));
		if(null!=role){
			Set<SystemResource> setResource = role.getSystemResource();
			for (SystemResource rsource : setResource) {
				if (rsource instanceof SystemMenu) {
	                SystemMenu menuItem = (SystemMenu) rsource;
	                resourceByMenuList(menuItem,checkedTree);
				}
			}
		}
		
		//根据节点异步加载权限树
		if(null!=id){
			
			List<String> list = treeMap.get(id);
			for(String nodeName:list){
				String menuName = nodeName.split(":")[0];
				String treeId = nodeName.split(":")[1];
				TreeNode node = new TreeNode();
				node.setId(treeId);
				node.setText(menuName);
				node.setExpanded(false);
				node.setHasChildren(false);
				if(checkedTree.containsKey(id)){
					List<String> checkedList = checkedTree.get(id);
					if(checkedList.contains(nodeName)){
						node.setChecked(true);
					}else{
						node.setChecked(false);
					}
				}
				
				resultTree.add(node);
			}
		}else{
			//初始化树的根节点
			Set<String> its = treeMap.keySet();
			for(String key :its){
				TreeNode parentNode = new TreeNode();
				parentNode.setId(key);
				parentNode.setText(key);
				parentNode.setHasChildren(true);
				parentNode.setExpanded(true);//默认节点打开
				if(checkedTree.containsKey(key)){
					parentNode.setChecked(true);
				}else{
					parentNode.setChecked(false);
				}
				resultTree.add(parentNode);
			}
		}
		return resultTree;
	}


	@Override
	public Integer validataUserNmaeExists(String valiDateField,String type) {
		
		 List<SearchFilter> searchFilter = new ArrayList<SearchFilter>(); 
 	    if(StringUtils.isNotEmpty(valiDateField)){
 	    	if("loginName".equalsIgnoreCase(type)){
 	    		searchFilter.add(new SearchFilter("userName", Operator.EQ, valiDateField));
 	    		Specification<SystemUser> specUserNameAnd = DynamicSpecifications.bySearchFilter(searchFilter,BooleanOperator.OR,SystemUser.class);       
 	    		return systemUserRepository.findAll(Specifications.where(specUserNameAnd)).size();
 	    	}else if("roleName".equalsIgnoreCase(type)){
 	    		searchFilter.add(new SearchFilter("roleName", Operator.EQ, valiDateField));
 	    		Specification<SystemRole> specRoleNameAnd = DynamicSpecifications.bySearchFilter(searchFilter,BooleanOperator.OR,SystemRole.class);  
 	    		return systemRoleRepository.findAll(Specifications.where(specRoleNameAnd)).size();
 	    	}
		
 	    }
		  return null;
	}

}

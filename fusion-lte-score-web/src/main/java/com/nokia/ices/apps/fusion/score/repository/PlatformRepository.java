package com.nokia.ices.apps.fusion.score.repository;

import java.util.List;
import java.util.Map;

public interface PlatformRepository {
	
	//登录获取当前用户
	public Map<String,Object> validateLoginDao(String userName,String passWord);
	
	//初始化地区权限树
	public List<Map<String,Object>> getAreaListDao();
	
	//设置地区权限
	public void setAreaAuthority(Long userId,List<Long> areaIds);
	
	//设置菜单权限
	public void setMenuAuthority(Long userId,List<Long> areaIds);
	
	//初始化菜单权限树
	public List<Map<String,Object>> getMenuListDao();
	
	//获取当前用户拥有的地区权限
	public List<Long> getUserInArea(Long UserId);
	
	//根据用户查询角色
	/*SELECT a.id,a.role_name from system_role a JOIN system_role_system_user b ON
	a.id = b.system_role_id JOIN system_user c on c.id = 1*/
	public List<Map<String,Object>> findRole(Long userId);
	
	//根据当前角色查询菜单权限
	public List<Map<String,Object>> getCurrentUserInMenuList(Long roleId);
	/*SELECT d.`name`,c.resource_code,c.resource_name from system_role a JOIN system_role_resource b ON
	a.id = b.system_role_id JOIN resource c ON c.id=b.resource_id
	JOIN system_resource_type d on d.id = c.resource_type WHERE a.role_name='管理员'*/
	

}

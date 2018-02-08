package com.nokia.ices.apps.fusion.system.controller;

import java.io.File;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.jpa.SearchFilter.Operator;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.security.external.ExternalLoginEntity;
import com.nokia.ices.apps.fusion.security.external.ExternalLoginHolder;
import com.nokia.ices.apps.fusion.security.realm.ShiroDBRealm;
import com.nokia.ices.apps.fusion.system.domain.SystemArea;
import com.nokia.ices.apps.fusion.system.domain.SystemMenu;
import com.nokia.ices.apps.fusion.system.domain.SystemOperationLog;
import com.nokia.ices.apps.fusion.system.domain.SystemResource;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.system.domain.SystemUser;
import com.nokia.ices.apps.fusion.system.repository.SystemAreaRepository;
import com.nokia.ices.apps.fusion.system.repository.SystemMenuRepository;
import com.nokia.ices.apps.fusion.system.repository.SystemRoleRepository;
import com.nokia.ices.apps.fusion.system.repository.SystemUserRepository;
import com.nokia.ices.apps.fusion.system.repository.spec.SystemRoleSpecifications;
import com.nokia.ices.apps.fusion.system.repository.types.SystemOperationLogOpType;
import com.nokia.ices.apps.fusion.system.service.SystemService;

import app.LicenseCheck;

@RestController
@RepositoryRestController
public class SystemRestController {
	
	public static final Logger logger = LoggerFactory.getLogger(SystemRestController.class);

	@Autowired
	private SystemService systemService;

	@Autowired
	private ShiroDBRealm shiroDBRealm;

	public ShiroDBRealm getShiroDBRealm() {
		return shiroDBRealm;
	}

	public void setShiroDBRealm(ShiroDBRealm shiroDBRealm) {
		this.shiroDBRealm = shiroDBRealm;
	}

	@Autowired
	private SystemMenuRepository systemMenuRepository;
	@Autowired
	private SystemRoleRepository systemRoleRepository;
	@Autowired
	private SystemUserRepository systemUserRepository;
	@Autowired
	private SystemAreaRepository systemAreaRepository;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@SuppressWarnings("rawtypes")
	@Autowired
	PagedResourcesAssembler pagedResourcesAssembler;

	@SuppressWarnings("deprecation")
	@RequestMapping("system-license/isExpired")
	public Map<String, String> isExpired(@RequestParam(value = "menuName", required = false) String menuName) {
		try {
			menuName = menuName.trim();
			LicenseCheck licenseCheck = new LicenseCheck();
			String private_key_path = ProjectProperties.getPrivateLicensePath() + File.separator + menuName + ".key";
			String public_key_path = ProjectProperties.getPublicLicensePath();
			private_key_path = URLDecoder.decode(private_key_path);
			public_key_path = URLDecoder.decode(public_key_path);
			System.out.println(private_key_path);
			Map<String, String> res = licenseCheck.checkPrivateKey(private_key_path, public_key_path);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, String>();
		}
	}

	@RequestMapping("equipment-unit/login")
	public void login(@RequestParam(value = "unitName", required = false) String unitName) {
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		systemService.saveSystemOperationLog(shiroUser, "集中登录", "单元名称:" + unitName, SystemOperationLogOpType.Other);
	}

    //外部用户登录验证
    @RequestMapping("system-manager/ssoValidToken")
	public ExternalLoginEntity ssoValidToken(@RequestParam("token") String ssoToken) {
    	try {
    		logger.info("ssoToken:{}",ssoToken);
    		return ExternalLoginHolder.checkToken(ssoToken);
		} catch (Exception e) {
            throw new RuntimeException(e.getMessage());			
		}
	}
    //外部用户登出
	@RequestMapping("system-manager/removeToken")
	public void removeSSOValidToken(@RequestParam("token") String ssoToken) {
		ExternalLoginHolder.removeToken(ssoToken);
	}
	
	@RequestMapping("system-menu/search/all")
	public List<SystemMenu> findSystemMenuAll() {
		Subject subject = SecurityUtils.getSubject();
		Iterable<SystemMenu> iter = systemMenuRepository.findAll();
		List<SystemMenu> result = new ArrayList<SystemMenu>();
		for (SystemMenu systemMenu : iter) {
			if (subject.isPermitted("menu:" + systemMenu.getMenuCode())) {
				result.add(systemMenu);
			}
		}
		return result;
	}

	@RequestMapping("system-area/search/all")
	public List<SystemArea> findSystemAreaAll() {
		Subject subject = SecurityUtils.getSubject();
		Iterable<SystemArea> iter = systemAreaRepository.findAll();
		List<SystemArea> result = new ArrayList<SystemArea>();
		for (SystemArea systemArea : iter) {
			if (subject.isPermitted("area:" + systemArea.getAreaCode())) {
				result.add(systemArea);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/system-operation-log/search/searchByFilter")
	public PagedResources<SystemOperationLog> getSystemOperationLogList(
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "userName", required = false) String userName, Pageable pageable,
			PersistentEntityResourceAssembler assembler) {
		// ShiroUser shiroUser = (ShiroUser)
		// SecurityUtils.getSubject().getPrincipal();
		// SystemRole systemRole = shiroUser.getRole();
		/* SystemRole systemRole = new SystemRole(); */
		Map<String, Object> paramMap = new HashMap<String, Object>();

		paramMap.put("loginUserName_LIKE", userName);
		try {
			if (StringUtils.isNotBlank(startTime)) {
				paramMap.put("LogTime_GE", sdf.parse(startTime));
			}

			if (StringUtils.isNotBlank(endTime)) {
				paramMap.put("LogTime_LE", sdf.parse(endTime));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Page<SystemOperationLog> page = systemService.findSystemOperationLogPageByFilter(paramMap, pageable);
		return pagedResourcesAssembler.toResource(page, assembler);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("system-user/search/findPageByFilter")
	public PagedResources<PersistentEntityResource> findSystemUserByFilter(
			@RequestParam(value = "searchField", required = false) String searchField, Pageable pageable,
			PersistentEntityResourceAssembler assembler) {
		try {
			ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			Assert.notNull(shiroUser, "没有登录");
			Collection<SystemRole> roleSet = systemRoleRepository.findRoleBySystemUserUserName(shiroUser.getUserName());
			Assert.notNull(roleSet, "登录角色为空");
			Page<SystemUser> page = systemService.findUserPageByFilter(searchField, roleSet, pageable,
					"admin".equals(shiroUser.getUserName()) ? "" : shiroUser.getUserName());
			return pagedResourcesAssembler.toResource(page, assembler);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	/*
	 * @RequestMapping("system-user/search/findPageByUser") public String
	 * findSystemUserListFilter() { try { ShiroUser shiroUser = (ShiroUser)
	 * SecurityUtils.getSubject().getPrincipal(); Specification<SystemUser>
	 * specSearchByWord =
	 * SystemUserSpecifications.creatorLike(shiroUser.getUserName());
	 * Collection<SystemRole> roleSet =
	 * systemRoleRepository.findRoleBySystemUserUserName(shiroUser.getUserName()
	 * ); Set<SystemRole> roleSetQuery = new HashSet<SystemRole>(roleSet);
	 * Specification<SystemUser> specSearchByRoleSet =
	 * SystemUserSpecifications.roleIn(roleSetQuery); List<SystemUser> page =
	 * systemUserRepository.findAll(Specifications.where(specSearchByWord).and(
	 * specSearchByRoleSet)); return page; } catch (Exception e) {
	 * logger.error(e.getMessage()); return null; }
	 * 
	 * }
	 */

	@RequestMapping("system-user/search/findListByFilter")
	public Resources<Resource<Object>> findSystemUserListByFilter(Sort sort,
			PersistentEntityResourceAssembler assembler) {
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Assert.notNull(shiroUser, "没有登录");
		Collection<SystemRole> roleSet = systemRoleRepository.findRoleBySystemUserUserName(shiroUser.getUserName());
		Assert.notEmpty(roleSet, "登录角色为空");
		List<SystemUser> list = systemUserRepository.findUserBySystemRoleIn(roleSet);
		List<Resource<Object>> resources = new ArrayList<Resource<Object>>();
		for (Object obj : list) {
			resources.add(obj == null ? null : assembler.toResource(obj));
		}
		return new Resources<Resource<Object>>(resources);
	}

	@RequestMapping("system-user/search/list")
	public List<SystemUser> findSystemUserList(Sort sort) {
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Assert.notNull(shiroUser, "没有登录");
		Collection<SystemRole> roleSet = systemRoleRepository.findRoleBySystemUserUserName(shiroUser.getUserName());
		Assert.notEmpty(roleSet, "登录角色为空");

		List<SystemUser> list = systemUserRepository.findUserBySystemRoleIn(roleSet);
		return list;
	}

	@RequestMapping("system-area/search/page")
	public PagedResources<SystemArea> findSystemAreaBySearch(
			@RequestParam(value = "searchField", required = false) String searchField, Pageable pageable,
			PersistentEntityResourceAssembler assembler) {
		return null;
	}

	@RequestMapping("system-area/search/list")
	public Resources<Resource<Object>> findSystemAreaListByFilter(
			@RequestParam(value = "searchField", required = false) String searchField, Sort sort,
			PersistentEntityResourceAssembler assembler) {
		// ShiroUser shiroUser = (ShiroUser)
		// SecurityUtils.getSubject().getPrincipal();
		// SystemRole systemRole = shiroUser.getRole();
		// Assert.notNull(systemRole, "登录角色为空");
		List<SystemArea> list = systemService.findAreaListByFilter(searchField, null, sort);
		List<Resource<Object>> resources = new ArrayList<Resource<Object>>();

		for (Object obj : list) {
			resources.add(obj == null ? null : assembler.toResource(obj));
		}
		return new Resources<Resource<Object>>(resources);
	}

	@RequestMapping("resource/{id}")
	public boolean addResourceID(@PathVariable Long id) {
		try {
			shiroDBRealm.clearCachedAuthorizationInfoForChange(SecurityUtils.getSubject().getPrincipals());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@RequestMapping("system-menu/search/findByLink/{link}")
	public List<SystemMenu> findMenu(@PathVariable String link) {
		Subject subject = SecurityUtils.getSubject();
		SystemMenu thisMenu = systemMenuRepository.findOneByMenuCode(link);
		Assert.isInstanceOf(SystemMenu.class, thisMenu);
		String parentMenu = thisMenu.getParentMenuName();
		Assert.notNull(parentMenu);
		List<SystemMenu> list = systemMenuRepository.findByParentMenuName(parentMenu);
		List<SystemMenu> result = new ArrayList<SystemMenu>();
		for (SystemMenu systemMenu : list) {
			if (subject.isPermitted("menu:" + systemMenu.getMenuCode())) {
				result.add(systemMenu);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("system-role/search/findPageByFilter")
	public PagedResources<PersistentEntityResource> findSystemRoleByFilter(
			@RequestParam(value = "searchField", required = false) String searchField,
			@RequestParam(value = "searchField01", required = false) String searchField01, Pageable pageable,
			PersistentEntityResourceAssembler assembler) {
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();

		// 页面传递条件
		List<SearchFilter> nameFilter = new ArrayList<SearchFilter>();
		if (StringUtils.isNotEmpty(searchField)) {
			nameFilter.add(new SearchFilter("roleName", Operator.LIKE, searchField));
			nameFilter.add(new SearchFilter("roleDesc", Operator.LIKE, searchField));
			nameFilter.add(new SearchFilter("notifiType", Operator.LIKE, searchField));
			nameFilter.add(new SearchFilter("notifi", Operator.LIKE, searchField));
		}
		List<SearchFilter> nameFilter01 = new ArrayList<SearchFilter>();
		if (StringUtils.isNotEmpty(searchField01)) {
			// nameFilter01.add(new SearchFilter("roleName", Operator.LIKE,
			// searchField01));
			// nameFilter01.add(new SearchFilter("roleDesc", Operator.LIKE,
			// searchField01));
			nameFilter01.add(new SearchFilter("notifiType", Operator.LIKE, searchField01));
			nameFilter01.add(new SearchFilter("notifi", Operator.LIKE, searchField01));
		}

		List<SearchFilter> FilterAND = new ArrayList<SearchFilter>();
		FilterAND.add(new SearchFilter("creator", Operator.LIKE, shiroUser.getUserName() + "@"));

		Specification<SystemRole> nameLike = DynamicSpecifications.bySearchFilter(nameFilter, BooleanOperator.OR,
				SystemRole.class);
		Specification<SystemRole> nameLike01 = DynamicSpecifications.bySearchFilter(nameFilter01, BooleanOperator.OR,
				SystemRole.class);
		Specification<SystemRole> FilterAND01 = DynamicSpecifications.bySearchFilter(FilterAND, BooleanOperator.AND,
				SystemRole.class);

		Collection<SystemRole> roleSet = systemRoleRepository.findRoleBySystemUserUserName(shiroUser.getUserName());

		Specification<SystemRole> createdByMe = SystemRoleSpecifications.createdByMe(shiroUser.getRoleCreatorPath());

		Specification<SystemRole> createdByMyGroup = SystemRoleSpecifications.createdByMyGroup(roleSet);

		Specifications<SystemRole> privilegeSpec = Specifications.where(createdByMe).or(createdByMyGroup);
		Page<SystemRole> page = systemRoleRepository
				.findAll(Specifications.where(nameLike).and(privilegeSpec).and(nameLike01), pageable);

		return pagedResourcesAssembler.toResource(page, assembler);
	}

	@RequestMapping("system-resource/searchByFilter")
	public Page<SystemResource> findSystemResourceBySearch(
			@RequestParam(value = "searchField", required = false) String searchField, Pageable pageable) {
		// ShiroUser shiroUser = (ShiroUser)
		// SecurityUtils.getSubject().getPrincipal();

		Page<SystemResource> page = systemService.findResourcePageByFilter(searchField, null, pageable);
		return page;
	}
}

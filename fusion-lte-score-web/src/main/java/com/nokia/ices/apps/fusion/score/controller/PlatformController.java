package com.nokia.ices.apps.fusion.score.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.score.domain.TreeNode;
import com.nokia.ices.apps.fusion.score.service.PlatformService;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.system.domain.SystemArea;
import com.nokia.ices.apps.fusion.system.domain.SystemMenu;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.system.domain.SystemUser;
import com.nokia.ices.apps.fusion.system.repository.SystemRoleRepository;
import com.nokia.ices.apps.fusion.system.repository.SystemUserRepository;
import com.nokia.ices.core.utils.Digests;
import com.nokia.ices.core.utils.Encodes;
import com.nokia.ices.core.utils.JsonMapper;

@RequestMapping("/platform")
@RepositoryRestController
@RestController
public class PlatformController {

	public static final Logger logger = LoggerFactory.getLogger(PlatformController.class);

	@Autowired
	PlatformService platformService;

	@Autowired
	SystemRoleRepository systemRoleRepository;

	@Autowired
	SystemUserRepository systemUserRepository;

	@SuppressWarnings("rawtypes")
	@Autowired
	PagedResourcesAssembler pagedResourcesAssembler;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/user/search")
	public PagedResources getSystemUserList(@RequestParam(value = "searchField", required = false) String searchField, Pageable pageable, PersistentEntityResourceAssembler assembler) {
		Map<String, Object> searchParams = new HashMap<String, Object>();
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		searchParams.put("searchField", searchField);
		Page<SystemUser> page = platformService.findSystemUserPageBySearch(searchParams, shiroUser, pageable);

		return pagedResourcesAssembler.toResource(page, assembler);
	}

	@RequestMapping(value = "/area/search")
	public List<SystemArea> getSystemAreaList(@RequestParam(value = "searchField", required = false) String searchField, Pageable pageable, PersistentEntityResourceAssembler assembler) {
		Map<String, Object> searchParams = new HashMap<String, Object>();
		//ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		//请求此操作时清理掉session中的地区
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		if(session.getAttribute("areaList") != null) {
			session.removeAttribute("areaList");
		}
		searchParams.put("searchField", searchField);
		List<SystemArea> areaList = platformService.findSystemAreaListByCreator(searchParams);
		return areaList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/role/search")
	public PagedResources getSystemRoleList(@RequestParam(value = "searchField", required = false) String searchField, Pageable pageable, PersistentEntityResourceAssembler assembler) {

		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("searchField", searchField);
		Page<SystemRole> page = systemRoleRepository.findPageByRoleNameContains(searchField, pageable);
		return pagedResourcesAssembler.toResource(page, assembler);
	}

	/**
	 * 用户获取权限
	 * @return
	 */
	@RequestMapping(value = "/authority/search")
	public Map<String, List<SystemMenu>> getcurrentUserAuthority() {

		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Map<String, List<SystemMenu>> resultList = platformService.getcurrentUserAuthority(shiroUser);
		// System.out.println(new JsonMapper().toJson(resultList));
		return resultList;
	}

	@RequestMapping(value = "/menuTree/{roleId}")
	public List<TreeNode> getMenuTree(@PathVariable String roleId, @RequestParam(value = "id", required = false) String id) {
		List<TreeNode> treeNode = platformService.getMenuTree(roleId, id);
		System.out.println(new JsonMapper().toJson(treeNode));
		return treeNode;
	}

	/**
	 * 根据属性值查询记录，=0不重复，>0重复
	 * 
	 * @return
	 */
	@RequestMapping(value = "/VerificationRepeat")
	public Integer Repeat(@RequestParam("valiDateField") String valiDateField, @RequestParam("type") String type) {
		return platformService.validataUserNmaeExists(valiDateField, type);
	}

	@RequestMapping(value = "/changeOwnerPassword", method = RequestMethod.POST)
	public String changeOwnerPassword(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		String passwordNew = null == parameterMap.get("passwordNew") ? "" : parameterMap.get("passwordNew")[0];
		String passwordOld = null == parameterMap.get("passwordOld") ? "" : parameterMap.get("passwordOld")[0];
		try {
			ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			SystemUser currentUser = systemUserRepository.findOne(shiroUser.getUserId());

			byte[] salt = Encodes.decodeHex(currentUser.getSalt());
			byte[] hashPassword = Digests.sha1(passwordOld.getBytes(), salt, SystemUser.HASH_INTERATIONS);
			if (!currentUser.getEncryptedPassword().equals(Encodes.encodeHex(hashPassword))) {
				return "原密码不正确";
			}
			salt = Digests.generateSalt(SystemUser.SALT_SIZE);
			currentUser.setSalt(Encodes.encodeHex(salt));
			currentUser.setPlainPassword(passwordNew);
			hashPassword = Digests.sha1(passwordNew.getBytes(), salt, SystemUser.HASH_INTERATIONS);
			currentUser.setEncryptedPassword(Encodes.encodeHex(hashPassword));
			systemUserRepository.save(currentUser);
			return "";
		} catch (Exception e) {
			return e.getMessage();
		}

	}
}

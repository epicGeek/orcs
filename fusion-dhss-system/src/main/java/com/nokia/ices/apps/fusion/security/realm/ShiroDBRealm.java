package com.nokia.ices.apps.fusion.security.realm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.subject.WebSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.security.external.ExternalLoginEntity;
import com.nokia.ices.apps.fusion.security.external.ExternalLoginHolder;
import com.nokia.ices.apps.fusion.security.matcher.FujianSSOCredentialsMatcher;
import com.nokia.ices.apps.fusion.system.domain.SystemArea;
import com.nokia.ices.apps.fusion.system.domain.SystemManage;
import com.nokia.ices.apps.fusion.system.domain.SystemMenu;
import com.nokia.ices.apps.fusion.system.domain.SystemResource;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.system.domain.SystemUser;
import com.nokia.ices.apps.fusion.system.repository.SystemManageRepository;
import com.nokia.ices.apps.fusion.system.repository.SystemResourceRepository;
import com.nokia.ices.apps.fusion.system.repository.SystemRoleRepository;
import com.nokia.ices.apps.fusion.system.repository.SystemUserRepository;
import com.nokia.ices.apps.fusion.system.repository.types.SystemOperationLogOpType;
import com.nokia.ices.apps.fusion.system.service.SystemService;
import com.nokia.ices.core.utils.Encodes;

public class ShiroDBRealm extends AuthorizingRealm {

	private final static Logger logger = LoggerFactory.getLogger(ShiroDBRealm.class);

	// @Value("${dhlr.authentication.mode}")
	private String authenticationMode = "local";

	@Autowired
	private SystemUserRepository systemUserRepository;

	@Autowired
	private SystemRoleRepository systemRoleRepository;

	@Autowired
	private SystemResourceRepository systemResourceRepository;

	@Autowired
	private SystemManageRepository systemManageRepository;

	@Autowired
	private SystemService systemService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		logger.debug("进入授权模块");
		ShiroUser loginUser = (ShiroUser) principals.getPrimaryPrincipal();
		Iterable<SystemRole> roleList = new ArrayList<SystemRole>();
		Iterable<SystemResource> resourceList = new ArrayList<SystemResource>();

		if ("admin".equalsIgnoreCase(loginUser.getUserName())) {
			roleList = systemRoleRepository.findAll();
			resourceList = systemResourceRepository.findAll();
		} else {
			roleList = systemRoleRepository.findRoleBySystemUserUserName(loginUser.getUserName());
			resourceList = systemResourceRepository.findSystemResourceBySystemRoleIn(roleList);
		}

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

		for (SystemRole role : roleList) {
			logger.debug("Role {} added", role.getRoleName());
			info.addRole(role.getRoleName());
		}
//		Set<String> externalModules = new HashSet<String>();
		for (SystemResource systemResource : resourceList) {
			if (systemResource instanceof SystemMenu) {
				SystemMenu menuItem = (SystemMenu) systemResource;
				logger.debug("Menu {} added", menuItem.getMenuCode());
				info.addStringPermission("menu:" + menuItem.getMenuCode());
//				if(menuItem.getMenuCode().startsWith("external:")){
//					externalModules.add(menuItem.getMenuCode());
//				}
			} else if (systemResource instanceof SystemArea) {
				SystemArea areaItem = (SystemArea) systemResource;
				logger.debug("Area {} added", areaItem.getAreaCode());
				info.addStringPermission("area:" + areaItem.getAreaCode());
			} else {
				logger.debug("resource:{} added", systemResource.getId());

				info.addStringPermission("resource" + ":" + systemResource.getId());
			}
		}
		//外部用户登录验证 只有权限包含external菜单可以加验证 external:onekeybackup
//		String moduleNames = "";
//		for (String moduleName : externalModules) {
//			moduleNames += moduleName +";";
//		}
//        if(!externalModules.isEmpty()){
//            ExternalLoginEntity externalLogin = new ExternalLoginEntity();
//    		externalLogin.setUsername(loginUser.getUserName());
//    		externalLogin.setDisplayName(loginUser.getRealName());
//    		externalLogin.setModuleName(moduleNames);
////    		ExternalLoginHolder.getExternalLoginMap().put(loginUser.getToken(), externalLogin);
//    		logger.info("login token {}",loginUser.getToken());
//    		ExternalLoginHolder.putExternalLoginMap(loginUser.getToken(), externalLogin);
//        }
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		SystemUser user = new SystemUser();
		SystemRole role = new SystemRole();

		String plainPassword = "flzx3qcysyhl9t";
		String salts = "ca6446e1338be9ce";
		String EncryptedPassword = "2fda4e64b28be1bb7281ce4a6b02835559cf5e55";
		List<SystemManage> manage = (List<SystemManage>) systemManageRepository.findAll();
		if (manage.size() != 0) {
			SystemManage sm = manage.get(0);
			try {
				plainPassword = uncompress(sm.getPlainPassword());
			} catch (Exception e) {
				e.printStackTrace();
			}
			salts = sm.getSalt();
			EncryptedPassword = sm.getEncryptedPassword();
		}

		if (token.getUsername().equalsIgnoreCase("admin")
				&& String.valueOf(token.getPassword()).equalsIgnoreCase(plainPassword)) {
			logger.info(token.getUsername() + "登录");
			user.setUserName("admin");
			user.setRealName("超级管理员");
			user.setId(0l);
			user.setInUse(true);
			user.setSalt(salts);
			user.setPlainPassword(plainPassword);
			user.setEncryptedPassword(EncryptedPassword);
			user.setExpireDate(DateUtils.addDays(new Date(), 90));
			role.setId(0l);
			role.setRoleName("root");
			role.setPath("/");
		} else {
			user = systemUserRepository.findOneByUserName(token.getUsername());
			// 未知原因
			if (null == user) {
				logger.info("查无此人");
				throw new UnknownAccountException(token.getUsername() + " 查无此人");
			}
			if (user.isInUse() == null || !user.isInUse()) {
				logger.info("已被禁用");
				throw new DisabledAccountException(token.getUsername() + " 已被禁用");
			}
			List<SystemRole> _roleList = systemRoleRepository.findRoleBySystemUserUserName(token.getUsername());
			if (_roleList.size() > 0) {
				role = _roleList.get(0);
			}
			// if (user.getExpireDate()==null||(new
			// Date()).after(user.getExpireDate())) {
			// throw new ExpiredCredentialsException(token.getUsername() + "
			// 超过时限，请联系管理员");
			// }
		}
		ShiroUser loginUser = new ShiroUser();
		//设置外部用户token 外部用户登录验证
		String externalToken = UUID.randomUUID().toString();
		loginUser.setToken(externalToken);

		
		
		// 获得远端IP
		String remoteAddress = getRemoteAddress();
		loginUser.setRemoteAddress(remoteAddress);

		byte[] salt = Encodes.decodeHex(user.getSalt());
		String loginUserName = user.getUserName();
		loginUser.setUserId(user.getId());
		loginUser.setUserName(loginUserName);
		loginUser.setRealName(user.getRealName());
		loginUser.setDelayPass(user.getExpireDate().getTime() < new Date().getTime());
		ExternalLoginEntity externalLogin = new ExternalLoginEntity();
		externalLogin.setUsername(loginUser.getUserName());
		externalLogin.setDisplayName(loginUser.getRealName());
//		externalLogin.setModuleName(moduleNames);
//		ExternalLoginHolder.getExternalLoginMap().put(loginUser.getToken(), externalLogin);
		ExternalLoginHolder.putExternalLoginMap(loginUser.getToken(), externalLogin);
		
		if (role != null) {
			loginUser.setRoleId(role.getId());
			loginUser.setRoleName(role.getRoleName());
			loginUser.setSelfLink(loginUserName + "@" + remoteAddress);
			loginUser.setRoleCreatorPath(role.getPath());
		}
		// Iterable<SystemResource> resourceEntityList = new
		// ArrayList<SystemResource>();
		// if(role.getId() != 0){
		// resourceEntityList =
		// systemResourceRepository.findSystemResourceBySystemRoleIdEquals(role.getId());
		// }else{
		// resourceEntityList = systemResourceRepository.findAll();
		// }
		// List<Long> resourceList = new ArrayList<Long>();
		// for (SystemResource systemResource : resourceEntityList) {
		// resourceList.add(systemResource.getId());
		// }
		systemService.saveSystemOperationLog(loginUser, "登录", "", SystemOperationLogOpType.Login);

		AuthenticationInfo loginInfo = new SimpleAuthenticationInfo(loginUser, user.getEncryptedPassword(),
				ByteSource.Util.bytes(salt), getName());

		return loginInfo;
	}

	public String uncompress(String str) throws Exception {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
			GZIPInputStream gunzip = new GZIPInputStream(in);

			byte[] buffer = new byte[1024];
			int n;
			while ((n = gunzip.read(buffer)) != 0) {
				if (n >= 0) {
					out.write(buffer, 0, n);
				} else {
					break;
				}
			}
		} catch (IOException e) {
			// test ok
			throw new Exception(e.getMessage());
		}
		// toString()使用平台默认编码，也可以显式的指定如toString(&quot;GBK&quot;)
		return out.toString();
	}

	/**
	 * 设定Password校验的Hash算法与迭代次数.
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		if ("FuJian".equalsIgnoreCase(authenticationMode)) {
			setCredentialsMatcher(new FujianSSOCredentialsMatcher());
		} else {
			HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(SystemUser.HASH_ALGORITHM);
			matcher.setHashIterations(SystemUser.HASH_INTERATIONS);
			setCredentialsMatcher(matcher);
		}

	}

	public void clearCachedAuthorizationInfoForChange(PrincipalCollection pc) {
		super.doClearCache(pc);
		super.clearCachedAuthorizationInfo(pc);
	}

	// 获得远端IP
	private String getRemoteAddress() {
		HttpServletRequest request = (HttpServletRequest) ((WebSubject) SecurityUtils.getSubject()).getServletRequest();
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}

	private static final String OR_OPERATOR = " or ";
	private static final String AND_OPERATOR = " and ";
	private static final String NOT_OPERATOR = "not ";

	/**
	 * 支持or and not 关键词 不支持and or混用
	 * 
	 * @param principals
	 * @param permission
	 * @return
	 */
	@Override
	public boolean isPermitted(PrincipalCollection principals, String permission) {
		if (permission.contains(OR_OPERATOR)) {
			String[] permissions = permission.split(OR_OPERATOR);
			for (String orPermission : permissions) {
				if (isPermittedWithNotOperator(principals, orPermission)) {
					return true;
				}
			}
			return false;
		} else if (permission.contains(AND_OPERATOR)) {
			String[] permissions = permission.split(AND_OPERATOR);
			for (String orPermission : permissions) {
				if (!isPermittedWithNotOperator(principals, orPermission)) {
					return false;
				}
			}
			return true;
		} else {
			return isPermittedWithNotOperator(principals, permission);
		}
	}

	private boolean isPermittedWithNotOperator(PrincipalCollection principals, String permission) {
		if (permission.startsWith(NOT_OPERATOR)) {
			return !super.isPermitted(principals, permission.substring(NOT_OPERATOR.length()));
		} else {
			return super.isPermitted(principals, permission);
		}
	}

}
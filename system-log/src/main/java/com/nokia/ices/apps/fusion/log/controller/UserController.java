package com.nokia.ices.apps.fusion.log.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.system.domain.SystemUser;
import com.nokia.ices.apps.fusion.system.repository.SystemRoleRepository;
import com.nokia.ices.apps.fusion.system.repository.SystemUserRepository;
import com.nokia.ices.core.utils.Digests;
import com.nokia.ices.core.utils.Encodes;

@RequestMapping("/user")
@RestController
public class UserController {

	public static final Logger logger = LoggerFactory.getLogger(UserController.class);


	@Autowired
	SystemRoleRepository systemRoleRepository;

	@Autowired
	SystemUserRepository systemUserRepository;

    
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
	
	/**
	 * 首次登陆重置密码
	 * @param userName
	 * @param passwordNew
	 * @return
	 */
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public String changeOwnerPassword(@RequestParam("userName") String  userName,
            @RequestParam("passwordNew") String  passwordNew) {
		
			try {
				SystemUser currentUser = systemUserRepository.findOneByUserName(userName);
				currentUser.setPlainPassword(passwordNew);
				entryptPassword(currentUser);
				systemUserRepository.save(currentUser);
			} catch (Exception e) {
			    throw new RuntimeException(e.getMessage());
			}
		return "ok";
		
	}

    private void entryptPassword(SystemUser user) {
        byte[] salt = Digests.generateSalt(SystemUser.SALT_SIZE);
        user.setSalt(Encodes.encodeHex(salt));
        byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(), salt, SystemUser.HASH_INTERATIONS);
        user.setEncryptedPassword(Encodes.encodeHex(hashPassword));
    }
    
    
	
}

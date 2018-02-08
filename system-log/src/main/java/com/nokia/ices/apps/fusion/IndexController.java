package com.nokia.ices.apps.fusion;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.system.domain.SystemUser;
import com.nokia.ices.apps.fusion.system.repository.SystemUserRepository;

@Controller
public class IndexController {
	
	 @Autowired
	 private SystemUserRepository systemUserRepository;

	@RequestMapping(value = "welcome")
	public String welcome(Model model) {
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getUserName());
			model.addAttribute("realName", shiroUser.getRealName());
			return "createLog";
		} else {
			return "login";
		}

	}

	@RequestMapping(value = "")
	public String login(Model model) {
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getUserName());
			model.addAttribute("realName", shiroUser.getRealName());
			return "createLog";
		} else {
			return "login";
		}
	}
	
	// 日志记录
	@RequestMapping(value = "/createLog")
	public String createLog(Model model) {
		/*if (!checkPermission("menu:createLog")) {
			return "redirect:/welcome";
		}*/
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getUserName());
			model.addAttribute("realName", shiroUser.getRealName());
		}
		return "createLog";
	}
		
	// 历史记录
	@RequestMapping(value = "/historyLog")
	public String historyLog(Model model) {
		/*if (!checkPermission("menu:historyLog")) {
			return "redirect:/welcome";
		}*/
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getUserName());
			model.addAttribute("realName", shiroUser.getRealName());
		}
		return "historyLog";
	}
	
	// 用户管理
	@RequestMapping(value = "/userManage")
	public String userManage(Model model) {
		/*if (!checkPermission("menu:userManage")) {
			return "redirect:/welcome";
		}*/
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getUserName());
			model.addAttribute("realName", shiroUser.getRealName());
		}
		return "userManager";
	}
	
	// 基础数据维护
	@RequestMapping(value = "/baseDataOperation")
	public String baseDataOperation(Model model) {
		/*if (!checkPermission("menu:baseDataOperation")) {
			return "redirect:/welcome";
		}*/
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getUserName());
			model.addAttribute("realName", shiroUser.getRealName());
		}
		return "baseDataOperation";
	}
	
	//重置密码
	@RequestMapping(value = "/resetPwd")
	public String changePassword(Model model,@RequestParam(value="userName",required=false) String  userName) {
		
		if(StringUtils.isNotEmpty(userName)){
			//首次登陆修改密码
			model.addAttribute("userName",userName);
			model.addAttribute("flag", "true");
		}else{
			//二次以上修改密码
			ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			if (shiroUser != null) {
				model.addAttribute("userName", shiroUser.getUserName());
				model.addAttribute("flag", "false");
			}
		}
		return "changePW";
	}	

	@RequestMapping(value = "/logout")
	public String logout(Model model) {
		return "redirect:/logout";
	}
	
	@RequestMapping(value = "/checkPwd")
	@ResponseBody
	public String findByName(@RequestParam("userName") String  userName) {
		SystemUser user = systemUserRepository.findOneByUserName(userName);
		if(StringUtils.isNotEmpty(user.getEncryptedPassword())||StringUtils.isNotEmpty(user.getSalt())){
			return "no";
		}else{
			return "yes";
		}
	}
	private boolean checkPermission(String resourceName) {
		Subject subject = SecurityUtils.getSubject();
		return subject.isPermitted(resourceName);
	}

}


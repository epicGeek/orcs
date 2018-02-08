package com.nokia.ices.apps.fusion.system.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.system.domain.SystemManage;
import com.nokia.ices.apps.fusion.system.domain.SystemUser;
import com.nokia.ices.apps.fusion.system.domain.UserHistoryAccount;
import com.nokia.ices.apps.fusion.system.repository.SystemManageRepository;
import com.nokia.ices.apps.fusion.system.repository.SystemUserRepository;
import com.nokia.ices.apps.fusion.system.repository.UserHistoryAccountRepository;
import com.nokia.ices.apps.fusion.system.service.UserHistoryAccountService;
import com.nokia.ices.core.utils.Digests;
import com.nokia.ices.core.utils.Encodes;

@Controller
public class SystemLoginController {

    private static final Logger logger = LoggerFactory.getLogger(SystemLoginController.class);
    
    @Autowired
    private SystemUserRepository systemUserRepository;
    
    @Autowired
    private UserHistoryAccountService userHistoryAccountService;
    
    @Autowired
    private UserHistoryAccountRepository userHistoryAccountRepository;
    

    @Autowired
    private SystemManageRepository systemManageRepository;
    
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login() {
        if(getCurrentLoginUser()!=null){
            return "redirect:/welcome";
        }
        return "login";

    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model) {

        if(getCurrentLoginUser()!=null){
            return "redirect:/welcome";
        }
        model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);
        return "login";
    }

    @ResponseBody
    @RequestMapping("my/profile")
    public ShiroUser greeting() {
        ShiroUser shiroUser;
        ShiroUser shiroUserToPage = new ShiroUser();

        try {
            shiroUser = getCurrentLoginUser();
            if(shiroUser.getUserId()!=null){
            	shiroUserToPage.setUserName(shiroUser.getUserName());
                shiroUserToPage.setRealName(shiroUser.getRealName());
                shiroUserToPage.setUserId(shiroUser.getUserId());
                shiroUserToPage.setDelayPass(shiroUser.isDelayPass());
            }else{
                logger.debug("登录超时");
                shiroUserToPage.setErrorCode("登录超时");
            }
        } catch (Exception e) {
            shiroUserToPage.setErrorCode(e.getMessage());
        }
        return shiroUserToPage;
    }

    private ShiroUser getCurrentLoginUser(){
        try {
            Subject subject = SecurityUtils.getSubject();
            if(subject.isAuthenticated() || subject.isRemembered()){
                ShiroUser shiroUser = (ShiroUser) subject.getPrincipal();
                return shiroUser;
            }else{
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    // 修改自己密码
    @ResponseBody
    @RequestMapping(value = "my/password", name = "changeMyPassword", method = RequestMethod.PATCH)
    public Boolean changeMyPassword(@RequestBody Map<String,Object> params) {
        ShiroUser shiroUser;
        try {
            shiroUser = getCurrentLoginUser();
        } catch (Exception e) {
            throw new RuntimeException("没有登录");
        }
        if(shiroUser.getRoleId() != null && shiroUser.getRoleId() == 0L){
        	SystemManage sm = new SystemManage();
            String salts = "ca6446e1338be9ce";
            String EncryptedPassword = "2fda4e64b28be1bb7281ce4a6b02835559cf5e55";
            List<SystemManage> manage = (List<SystemManage>)systemManageRepository.findAll();
            if(manage.size() != 0){
            	sm = manage.get(0);
            	salts = sm.getSalt();
                EncryptedPassword = sm.getEncryptedPassword();
            }
        	
        	String passwordOld = (String)params.get("my_password_old");
            String passwordNew = (String)params.get("passwordNew");
            byte[] salt = Encodes.decodeHex(salts);
            byte[] hashPassword = Digests.sha1(passwordOld.getBytes(), salt, SystemUser.HASH_INTERATIONS);
            if (!EncryptedPassword.equals(Encodes.encodeHex(hashPassword))) {
                throw new RuntimeException("原密码不正确");
            }
           
            byte[] saltp = Digests.generateSalt(SystemUser.SALT_SIZE);
            sm.setSalt(Encodes.encodeHex(saltp));
            byte[] hashPassword1 = Digests.sha1(passwordNew.getBytes(), saltp, SystemUser.HASH_INTERATIONS);
            sm.setEncryptedPassword(Encodes.encodeHex(hashPassword1));
            sm.setPlainPassword(compress(passwordNew));
            systemManageRepository.save(sm);
            return true;
            
        }
        SystemUser currentUser = systemUserRepository.findOne(shiroUser.getUserId());
        Assert.notNull(currentUser, "查无此人");
        Assert.isTrue(params.containsKey("my_password_old"));
        Assert.isTrue(params.containsKey("passwordNew"));
        
        String passwordOld = (String)params.get("my_password_old");
        String passwordNew = (String)params.get("passwordNew");
        byte[] salt = Encodes.decodeHex(currentUser.getSalt());
        byte[] hashPassword = Digests.sha1(passwordOld.getBytes(), salt, SystemUser.HASH_INTERATIONS);
        if (!currentUser.getEncryptedPassword().equals(Encodes.encodeHex(hashPassword))) {
            throw new RuntimeException("原密码不正确");
        }
        UserHistoryAccount userHistoryAccount = userHistoryAccountService.whetherHasRepeatAccount(currentUser.getId(), passwordNew);
        if (userHistoryAccount != null && 
        		userHistoryAccount.getPasswd() != null &&
        		userHistoryAccount.getPasswd().indexOf(passwordNew+";") != -1) {
            throw new RuntimeException("新密码不能与前5次历史密码一致。");
        }
        currentUser.setPlainPassword(passwordNew);
        entryptPassword(currentUser);


        currentUser.setInUse(true);
        currentUser.setExpireDate(DateUtils.addDays(new Date(), 90));
    
        try {
        	if(userHistoryAccount == null){
        		userHistoryAccount = new UserHistoryAccount();
        		userHistoryAccount.setUser(currentUser);
        		userHistoryAccount.setPasswd(passwordNew + ";");
        	}else{
        		if(userHistoryAccount.getPasswd().split(";").length >= 5){
        			String pwd = userHistoryAccount.getPasswd();
        			userHistoryAccount.setPasswd( pwd.substring(pwd.indexOf(";")+1) + passwordNew+";");
        		}else{
        			userHistoryAccount.setPasswd(userHistoryAccount.getPasswd() + passwordNew+";");
        		}
        	}
        	userHistoryAccountRepository.save(userHistoryAccount);
        	systemUserRepository.save(currentUser);
        	((ShiroUser)SecurityUtils.getSubject().getPrincipal()).setDelayPass(false);
        	return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
    
    
    
    public  String compress(String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		String result = "";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = null;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != gzip) {
				try {
					gzip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			result = out.toString("ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
    
    
    @SuppressWarnings("static-access")
	@ResponseBody
    // 修改别人密码
    @RequestMapping(value = "system-user/delay-password/{userId:\\d+}",method = RequestMethod.PATCH)
    public boolean delayUserPassword(@PathVariable Long userId) {
        SystemUser systemUser = new SystemUser();
        systemUser = systemUserRepository.findOne(userId);
        try {
        	Calendar nowDate = Calendar.getInstance();
        	nowDate.add(nowDate.DATE,90);
        	systemUser.setExpireDate(nowDate.getTime());
            systemUserRepository.save(systemUser);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

	@ResponseBody
    // 修改别人密码
    @RequestMapping(value = "system-user/reset-password/{userId:\\d+}", name = "resetPassword",method = RequestMethod.PATCH)
    public HttpEntity<String> changeUserPassword(@PathVariable Long userId) {
        SystemUser systemUser = new SystemUser();
        systemUser = systemUserRepository.findOne(userId);
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd"); 
        logger.debug(systemUser.getUserName()+"_Cz"+now.format(formatter));
        systemUser.setPlainPassword(systemUser.getUserName()+"_Cz"+now.format(formatter));
        try {
//        	Calendar nowDate = Calendar.getInstance();
//        	nowDate.add(nowDate.DATE,90);
//        	systemUser.setExpireDate(nowDate.getTime());
            entryptPassword(systemUser);
            systemUserRepository.save(systemUser);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return new HttpEntity<String>("修改成功");
    }
    
    /*public static void main(String[] args) {
    	
    	 
		  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 
		  String dateStr=sdf.format(now.getTimeInMillis());
		 
		  System.out.println(dateStr);
	}*/

    @RequestMapping("system-user/test")
    public String test() {
        return "admin";
    }

    private void entryptPassword(SystemUser user) {
        byte[] salt = Digests.generateSalt(SystemUser.SALT_SIZE);
        user.setSalt(Encodes.encodeHex(salt));
        byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(), salt, SystemUser.HASH_INTERATIONS);
        user.setEncryptedPassword(Encodes.encodeHex(hashPassword));
    }

}

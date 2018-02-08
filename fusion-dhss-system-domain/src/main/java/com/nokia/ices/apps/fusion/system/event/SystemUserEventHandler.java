package com.nokia.ices.apps.fusion.system.event;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import com.nokia.ices.apps.fusion.system.domain.SystemOperationLog;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.system.domain.SystemUser;
import com.nokia.ices.apps.fusion.system.domain.UserHistoryAccount;
import com.nokia.ices.apps.fusion.system.repository.SystemOperationLogRepository;
import com.nokia.ices.apps.fusion.system.repository.SystemRoleRepository;
import com.nokia.ices.apps.fusion.system.repository.UserHistoryAccountRepository;
import com.nokia.ices.apps.fusion.system.repository.types.SystemOperationLogOpType;
import com.nokia.ices.core.utils.Digests;
import com.nokia.ices.core.utils.Encodes;

@RepositoryEventHandler(SystemUser.class)
public class SystemUserEventHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(SystemUserEventHandler.class);
    @Autowired
    private SystemOperationLogRepository systemOperationLogRepository;
    @Autowired
    private SystemRoleRepository systemRoleRepository;
    
    @Autowired
    private UserHistoryAccountRepository userHistoryAccountRepository;
    
    @HandleBeforeCreate
    public void handleBeforeCreate(SystemUser user) {
        user.setInUse(true);
        user.setPlainPassword(user.getUserName());
        user.setExpireDate(DateUtils.addDays(new Date(), 90));
        entryptPassword(user);
    }
    @HandleAfterCreate
    public void handleAfterCreate(SystemUser user){
        saveLog(user,SystemOperationLogOpType.AddPrivilege,"新增用户");
    }
    private void saveLog(SystemUser user,SystemOperationLogOpType opType,String opTextPrefix) {
        SystemOperationLog systemOperationLogItem = new SystemOperationLog();
        systemOperationLogItem.setApp("DHSS");
        systemOperationLogItem.setAppModule("用户管理");
        systemOperationLogItem.setOpType(opType);
        systemOperationLogItem.setLogTime(new Date());
        systemOperationLogItem.setOpText(opTextPrefix+user.getUserName());
        systemOperationLogItem.setLoginUserName(user.getCreator());
        try {
            systemOperationLogRepository.save(systemOperationLogItem);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @SuppressWarnings("static-access")
	@HandleAfterSave
	public void handleBeforeSave(SystemUser user) {
    	Calendar nowDate = Calendar.getInstance();
//    	nowDate.add(nowDate.DATE,90);
//    	user.setExpireDate(nowDate.getTime());
		saveLog(user, SystemOperationLogOpType.UpdatePrivilege,"更新用户");
	}

    @HandleBeforeDelete
    public void handleBeforeDelete(SystemUser user) {
    	
    	Set<SystemRole> roleSet = user.getSystemRole();
    	for (SystemRole systemRole : roleSet) {
    		systemRole.getSystemUser().remove(user);
		}
    	UserHistoryAccount account = userHistoryAccountRepository.findByUser(user);
    	if(account != null){
    		userHistoryAccountRepository.delete(account);
    	}
    	
    	systemRoleRepository.save(roleSet);
//    	user.setSystemRole(null);
//    	systemUserRepository.save(user);
    	
		saveLog(user, SystemOperationLogOpType.DelPrivilege,"删除用户");
    }

    /**
     * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
     */
    private void entryptPassword(SystemUser user) {
        byte[] salt = Digests.generateSalt(SystemUser.SALT_SIZE);
        user.setSalt(Encodes.encodeHex(salt));
        byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(), salt, SystemUser.HASH_INTERATIONS);
        user.setEncryptedPassword(Encodes.encodeHex(hashPassword));
    }

}

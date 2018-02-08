package com.nokia.ices.apps.fusion;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.data.domain.AuditorAware;

import com.nokia.ices.apps.fusion.security.domain.ShiroUser;

public class AuditorAwareImpl implements AuditorAware<String> {
    
    @Override
    public String getCurrentAuditor() {
        try {
            Subject subject = SecurityUtils.getSubject();
            ShiroUser currentUser = (ShiroUser) subject.getPrincipal();
            return currentUser.getSelfLink()+":"+currentUser.getRoleCreatorPath();
        } catch (Exception e) {
            return "admin@anyhost:/";
        }
    }
}

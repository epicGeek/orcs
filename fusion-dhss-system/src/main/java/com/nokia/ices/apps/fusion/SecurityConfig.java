package com.nokia.ices.apps.fusion;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import com.nokia.ices.apps.fusion.security.realm.ShiroDBRealm;
@Configuration
@ComponentScan(basePackages={"com.nokia.ices.apps.fusion.*.controller"})
public class SecurityConfig {
    private static Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    @Bean
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        Map<String, String> filterChainDefinitionMapping = new HashMap<String, String>();
        filterChainDefinitionMapping.put("/favicon.ico", "anon");
        filterChainDefinitionMapping.put("/custom/**", "anon");
        filterChainDefinitionMapping.put("/bower_components/**", "anon");
        filterChainDefinitionMapping.put("/assets/**", "anon");
        filterChainDefinitionMapping.put("/login", "authc");
        filterChainDefinitionMapping.put("/logout", "logout");
        filterChainDefinitionMapping.put("/rest/api/**", "anon");
        filterChainDefinitionMapping.put("/system-manager/ssoValidToken", "anon");
        filterChainDefinitionMapping.put("/system-manager/removeToken", "anon");
        filterChainDefinitionMapping.put("/rest/**", "anon");
        filterChainDefinitionMapping.put("/**", "user");
        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMapping);
        shiroFilter.setSecurityManager(securityManager());
        shiroFilter.setLoginUrl("/login");
        shiroFilter.setSuccessUrl("/welcome");
        shiroFilter.setUnauthorizedUrl("/forbidden");
        Map<String, Filter> filters = new HashMap<String, Filter>();
        filters.put("anon", new AnonymousFilter());
        filters.put("authc", new FormAuthenticationFilter());
        filters.put("logout", new LogoutFilter());
        filters.put("roles", new RolesAuthorizationFilter());
        filters.put("user", new UserFilter());
        shiroFilter.setFilters(filters);
        logger.info("init filters"+ filters.toString());
        return shiroFilter;
    }
    @Bean(name = "securityManager")
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm());
        securityManager.setCacheManager(cacheManager());
        return securityManager;
    }
    @Bean(name = "cacheManager")
    public CacheManager cacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:ehcache/ehcache-shiro.xml");
        return cacheManager;
    }
    @Bean(name = "realm")
    @DependsOn("lifecycleBeanPostProcessor")
    public Realm realm() {
        ShiroDBRealm shiroDBRealm = new ShiroDBRealm();
        shiroDBRealm.init();
        return shiroDBRealm;
    }
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
    @Bean
    @DependsOn(value="lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        return new DefaultAdvisorAutoProxyCreator();
    }
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }
}
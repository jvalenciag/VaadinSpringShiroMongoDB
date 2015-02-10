package com.jvg.samples.backend.security.config;

import com.jvg.samples.backend.security.SpringRealm;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * Created by COVICEL on 29/01/2015.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public AuthorizingRealm authorizingRealm(){
        return new SpringRealm();
    }

    @Bean
    public WebSecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setCacheManager(new MemoryConstrainedCacheManager());
        return securityManager;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    //https://github.com/vaadin/atmosphere/blob/master/modules/cpr/src/main/java/org/atmosphere/interceptor/ShiroInterceptor.java
    //http://mikepilone.blogspot.com/2013/07/vaadin-shiro-and-push.html
    /*@Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean(){
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        methodInvokingFactoryBean.setArguments(new Object[]{securityManager()});
        return methodInvokingFactoryBean;
    }*/


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

    @Bean
    public PasswordService passwordService(){
        DefaultPasswordService passwordService = new DefaultPasswordService();
        DefaultHashService hashService = new DefaultHashService();
        hashService.setHashAlgorithmName(Sha512Hash.ALGORITHM_NAME);

        //TODO Fix from app config for preloaded data;
        ByteSource salt = new SecureRandomNumberGenerator().nextBytes(32);
        hashService.setPrivateSalt(salt);
        hashService.setHashIterations(1024);

        passwordService.setHashService(hashService);
        return passwordService;
    }

}

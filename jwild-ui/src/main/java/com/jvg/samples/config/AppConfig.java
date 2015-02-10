package com.jvg.samples.config;

import com.jvg.samples.authentication.AccessControl;
import com.jvg.samples.authentication.ShiroAccessControl;
import com.jvg.samples.backend.DataService;
import com.jvg.samples.backend.config.SpringMongoConfig;
import com.jvg.samples.backend.mock.MockDataService;
import com.jvg.samples.backend.security.config.SecurityConfig;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jose on 25/01/15.
 */
@Configuration
@ComponentScan(basePackages = {"com.jvg"})
@Import({SpringMongoConfig.class, SecurityConfig.class})
public class AppConfig {
    //http://beegor.blogspot.com/2013/10/apache-shiro-with-spring-framework-java.html
    //http://svn.apache.org/repos/asf/shiro/trunk/samples/spring-hibernate/src/main/webapp/WEB-INF/applicationContext.xml

    @Autowired
    private WebSecurityManager securityManager;

    @Bean
    DataService dataService()
    {
        return new MockDataService();
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(){

        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        Map<String, String> definitionsMap = new HashMap<>();
        definitionsMap.put("/", "authc");
        definitionsMap.put("/favicon.ico", "noSessionCreation, anon");
        //definitionsMap.put("/admin/**", "authc, roles[admin]");
        definitionsMap.put("/VAADIN/**","anon");
        definitionsMap.put("/UIDL/**","anon");
        definitionsMap.put("/PUSH/**","anon");
        definitionsMap.put("/**", "authc");
        shiroFilter.setFilterChainDefinitionMap(definitionsMap);
        shiroFilter.setLoginUrl("/");
        shiroFilter.setSuccessUrl("/#!Inventory");
        shiroFilter.setSecurityManager(securityManager);
        return shiroFilter;
    }

    @Bean
    @Scope("prototype")
    AccessControl accessControl()
    {
        return new ShiroAccessControl();
    }
}

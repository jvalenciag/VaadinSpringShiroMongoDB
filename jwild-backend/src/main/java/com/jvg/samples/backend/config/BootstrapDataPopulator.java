package com.jvg.samples.backend.config;

import com.jvg.samples.backend.data.Category;
import com.jvg.samples.backend.data.Product;
import com.jvg.samples.backend.mock.MockDataGenerator;
import com.jvg.samples.backend.security.model.Role;
import com.jvg.samples.backend.security.model.User;
import com.jvg.samples.backend.utils.shiro.SpringShiroInterceptor;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by COVICEL on 28/01/2015.
 */
@Component
public class BootstrapDataPopulator implements InitializingBean {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private WebSecurityManager securityManager;

    @Autowired
    private AuthorizingRealm authorizingRealm;

    @Autowired
    private PasswordService passwordService;

    @Override
    public void afterPropertiesSet() throws Exception {
        ((DefaultWebSecurityManager)securityManager).setRealm(authorizingRealm);
        SpringShiroInterceptor.securityManager = securityManager;

        mongoTemplate.remove(new Query(), Product.COLLECTION_NAME);
        mongoTemplate.remove(new Query(), "category");
        mongoTemplate.remove(new Query(), User.COLLECTION_NAME);
        mongoTemplate.remove(new Query(), Role.COLLECTION_NAME);

        List<Category> categories = MockDataGenerator.createCategories(mongoTemplate);
        MockDataGenerator.createProducts(categories, mongoTemplate);

        Role adminRole = new Role();
        adminRole.setName("admin");
        Set<String> adminPermisions = new HashSet<>();

        adminPermisions.add("product:create");
        adminPermisions.add("product:edit");
        adminRole.setPermissions(adminPermisions);

        mongoTemplate.save(adminRole);

        String defaultPass = "123";

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordService.encryptPassword(defaultPass));
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        admin.setRoles(roles);
        mongoTemplate.save(admin);

        User seller = new User();
        seller.setUsername("seller");
        seller.setPassword(passwordService.encryptPassword(defaultPass));
        mongoTemplate.save(seller);

        assert passwordService.passwordsMatch(defaultPass,admin.getPassword());
    }
}

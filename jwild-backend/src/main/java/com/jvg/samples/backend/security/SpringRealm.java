package com.jvg.samples.backend.security;

import com.jvg.samples.backend.security.dao.UserDAO;
import com.jvg.samples.backend.security.model.Role;
import com.jvg.samples.backend.security.model.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by COVICEL on 28/01/2015.
 */
@Component
public class SpringRealm extends AuthorizingRealm
{
    protected UserDAO userDAO = null;

    @Autowired
    public PasswordService passwordService;

    public SpringRealm() {
        setName("SpringRealm"); //This name must match the name in the User class's getPrincipals() method
        //.setHashIterations(1024)
    }

    @PostConstruct
    private void Init()
    {
        PasswordMatcher passwordMatcher = new PasswordMatcher();
        passwordMatcher.setPasswordService(passwordService);
        setCredentialsMatcher(passwordMatcher);
    }

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ObjectId userId =  (ObjectId)principals.fromRealm(getName()).iterator().next();
        User user = userDAO.findOne(userId);
        if( user != null ) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            for( Role role : user.getRoles() ) {
                info.addRole(role.getName());
                info.addStringPermissions( role.getPermissions() );
            }
            return info;
        } else {
            return null;
        }
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        User user = userDAO.findByUsername(token.getUsername());
        if( user != null ) {
            return new SimpleAuthenticationInfo(user.getId(), user.getPassword(), getName());
        } else {
            return null;
        }
    }
}

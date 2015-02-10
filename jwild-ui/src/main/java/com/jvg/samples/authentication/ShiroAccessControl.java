package com.jvg.samples.authentication;

import com.vaadin.server.VaadinService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.atmosphere.cpr.FrameworkConfig;

/**
 * Created by COVICEL on 28/01/2015.
 */
public class ShiroAccessControl implements AccessControl {

    public static Subject getCurrentSubject()
    {
        Subject subject = (Subject) VaadinService.getCurrentRequest()
                .getAttribute(FrameworkConfig.SECURITY_SUBJECT);
        return subject==null? SecurityUtils.getSubject():subject;

    }

    @Override
    public boolean signIn(String username, String password) throws Exception {
        Subject currentUser = getCurrentSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        //this is all you have to do to support 'remember me' (no config - built in!):
        token.setRememberMe(true);
        String errorMessage;
        try {
            currentUser.login( token );
            return true;
        ///TODO multi-language messages
        } catch ( UnknownAccountException uae ) {
            //username wasn't in the system, show them an error message?
            errorMessage = "El nombre de usuario no existe";
        } catch ( IncorrectCredentialsException ice ) {
            //password didn't match, try again?
            errorMessage = "Contraseña incorrecta";
        } catch ( LockedAccountException lae ) {
            //account for that username is locked - can't login.  Show them a message?
            errorMessage = "Cuenta bloqueada";
        } catch ( AuthenticationException ae ) {
            //unexpected condition - error?
            errorMessage = ae.getLocalizedMessage();
        }
        throw new Exception(errorMessage);

    }

    @Override
    public boolean isUserSignedIn() {
        Subject currentUser = getCurrentSubject();
        return currentUser.isAuthenticated();
    }

    @Override
    public boolean isUserInRole(String role) {
        return false;
    }

    @Override
    public boolean isUserPermitted(String permission) {
        return getCurrentSubject().isPermitted(permission);
    }

    @Override
    public String getPrincipalName() {
        return null;
    }

    @Override
    public void logout() {
        getCurrentSubject().logout();
    }
}

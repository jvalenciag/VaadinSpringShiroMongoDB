package com.jvg.samples.backend.utils.shiro;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.subject.WebSubject;
import org.atmosphere.cpr.Action;
import org.atmosphere.cpr.AtmosphereInterceptorAdapter;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.FrameworkConfig;
import org.atmosphere.util.Utils;

/**
 * Created by jose on 02/02/15.
 */
public class SpringShiroInterceptor extends AtmosphereInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SpringShiroInterceptor.class);


    public static WebSecurityManager securityManager;

    @Override
    public Action inspect(AtmosphereResource r) {

        if (Utils.webSocketMessage(r)) return Action.CONTINUE;

        if (r.getRequest().attributes().containsKey(FrameworkConfig.SECURITY_SUBJECT) == false) {
            try {
                Subject currentUser = null;
                if (r.transport().equals(AtmosphereResource.TRANSPORT.WEBSOCKET)) {
                    //WebEnvironment env = WebUtils.getRequiredWebEnvironment(r.getAtmosphereConfig().getServletContext());
                    //currentUser = new WebSubject.Builder(env.getSecurityManager(), r.getRequest(), r.getResponse()).buildWebSubject();
                    currentUser = new WebSubject.Builder(securityManager, r.getRequest(), r.getResponse()).buildWebSubject();
                } else {
                    currentUser = SecurityUtils.getSubject();
                }
                if (currentUser != null) {
                    r.getRequest().setAttribute(FrameworkConfig.SECURITY_SUBJECT, currentUser);
                }
            } catch (UnavailableSecurityManagerException ex) {
                logger.info("Shiro Web Security : {}", ex.getMessage());
            } catch (IllegalStateException ex) {
                logger.info("Shiro Web Environment : {}", ex.getMessage());
            }
        }

        return Action.CONTINUE;
    }
}

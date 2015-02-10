package com.jvg.samples.config;

import org.atmosphere.cpr.SessionSupport;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.EnumSet;

/**
 * Created by jose on 01/02/15.
 */
public class CustomWebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();
        rootContext.register(AppConfig.class);

        container.addListener(new ContextLoaderListener(rootContext));

        //container.addListener(EnvironmentLoaderListener.class);
        //container.setInitParameter("shiroEnvironmentClass","org.apache.shiro.web.env.DefaultWebEnvironment");


        container.addListener(SessionSupport.class);
        container.setInitParameter("org.atmosphere.cpr.sessionSupport","true");

        EnumSet<DispatcherType> d = EnumSet.of(DispatcherType.REQUEST
                ,DispatcherType.ASYNC
                ,DispatcherType.ERROR,DispatcherType.FORWARD,DispatcherType.INCLUDE);

        FilterRegistration.Dynamic shiroFilter = container.addFilter("shiroFilter", DelegatingFilterProxy.class);
            shiroFilter.setInitParameter("targetFilterLifecycle","true");
            shiroFilter.addMappingForUrlPatterns(d, false, "/*");
            shiroFilter.setAsyncSupported(true);
    }
}

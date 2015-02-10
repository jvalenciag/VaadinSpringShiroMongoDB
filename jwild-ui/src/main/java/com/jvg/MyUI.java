package com.jvg;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import com.jvg.samples.MainScreen;
import com.jvg.samples.authentication.AccessControl;
import com.jvg.samples.authentication.BasicAccessControl;
import com.jvg.samples.authentication.LoginScreen;
import com.jvg.samples.authentication.LoginScreen.LoginListener;

import com.vaadin.annotations.*;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.xpoft.vaadin.SpringVaadinServlet;

/**
 * Main UI class of the application that shows either the login screen or the
 * main view of the application depending on whether a user is signed in.
 *
 * The @Viewport annotation configures the viewport meta tags appropriately on
 * mobile devices. Instead of device based scaling (default), using responsive
 * layouts.
 */
@Component("ui")
@Scope("prototype")
@Viewport("user-scalable=no,initial-scale=1.0")
@Theme("mytheme")
@Widgetset("com.jvg.MyAppWidgetset")
//@Push
public class MyUI extends UI {

    private AccessControl accessControl = new BasicAccessControl();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Responsive.makeResponsive(this);
        setLocale(vaadinRequest.getLocale());
        getPage().setTitle("My");
        if (!accessControl.isUserSignedIn()) {
            setContent(new LoginScreen(accessControl, this::showMainView));
        } else {
            showMainView();
        }
    }

    protected void showMainView() {
        addStyleName(ValoTheme.UI_WITH_MENU);
        setContent(new MainScreen(MyUI.this));
        getNavigator().navigateTo(getNavigator().getState());
    }

    public static MyUI get() {
        return (MyUI) UI.getCurrent();
    }

    public AccessControl getAccessControl() {
        return accessControl;
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true,
            initParams = {@WebInitParam(name = "systemMessagesBeanName",value = "DEFAULT")
                    ,@WebInitParam(name = "org.atmosphere.cpr.AtmosphereInterceptor"
                    ,value = "com.jvg.utils.shiro.SpringShiroInterceptor")})
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends SpringVaadinServlet {
    }
}

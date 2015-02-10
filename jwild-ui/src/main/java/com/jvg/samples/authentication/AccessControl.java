package com.jvg.samples.authentication;

/**
 * Simple interface for authentication and authorization checks.
 */
public interface AccessControl {

    public boolean signIn(String username, String password) throws Exception;

    public boolean isUserSignedIn();

    public boolean isUserInRole(String role);

    public String getPrincipalName();

    public boolean isUserPermitted(String permission);

    public void logout();
}

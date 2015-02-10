package com.jvg.samples.authentication;

/**
 * Simple interface for authentication and authorization checks.
 */
public interface AccessControl {

    public boolean signIn(String username, String password);

    public boolean isUserSignedIn();

    public boolean isUserInRole(String role);

    public String getPrincipalName();
}

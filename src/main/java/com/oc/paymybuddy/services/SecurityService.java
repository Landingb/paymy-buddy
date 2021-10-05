package com.oc.paymybuddy.services;

public interface SecurityService {
    boolean isAuthenticated();
    void autoLogin(String username, String password);
    String getCurrentUserDetailsUserName();
}

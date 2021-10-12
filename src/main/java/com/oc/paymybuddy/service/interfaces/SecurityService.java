package com.oc.paymybuddy.service.interfaces;



public interface SecurityService {
    boolean isAuthenticated();
    void autoLogin(String username, String password);
    public String getCurrentUserDetailsUserName();
}

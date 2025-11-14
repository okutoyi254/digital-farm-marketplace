package com.farmplace.digitalmarket.utils;

import com.farmplace.digitalmarket.security.securitymodel.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;

public class LoggedInCustomer {

    @Autowired
    static UserPrincipal principal;
    public static String getUsername(){

        return principal.getUsername();
    }
}

package com.farmplace.digitalmarket.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.time.Duration;

public class CookieUtil {

    public static void addCookie(HttpServletResponse response, ResponseCookie cookie){
        response.addHeader(HttpHeaders.SET_COOKIE,cookie.toString());
    }

    public static ResponseCookie createAccessTokenCookie(String token){
        return ResponseCookie.from("accessToken",token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMinutes(30))
                .sameSite("Strict")
                .build();
    }

    public static ResponseCookie createRefreshTokenCookie(String token){
        return ResponseCookie.from("refreshToken",token)
                .httpOnly(true)
                .secure(true)
                .path("/api/refresh")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();
    }

    public static ResponseCookie clearCookie(String name,String path){
        return ResponseCookie.from(name,"")
                .httpOnly(true)
                .secure(true)
                .path(path)
                .maxAge(0)
                .build();
    }
}

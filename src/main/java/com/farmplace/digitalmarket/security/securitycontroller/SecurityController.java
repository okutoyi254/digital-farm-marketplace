package com.farmplace.digitalmarket.security.securitycontroller;

import com.farmplace.digitalmarket.security.dto.LoginRequest;
import com.farmplace.digitalmarket.security.securityservice.JwtService;
import com.farmplace.digitalmarket.security.securityservice.UserService;
import com.farmplace.digitalmarket.utils.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SecurityController {

    @Autowired private UserService userService;
    @Autowired
    AuthenticationManager manager;
    @Autowired private JwtService jwtService;

    @GetMapping("/hello")
    public String helloRequest(){
        return "<html><h1>Hello,welcome to digital farm marketplace</h1></html>";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){

        Authentication authentication=manager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(),loginRequest.password()));
        String accessToken= jwtService.generateToken(authentication.getName());
        String refreshToken= jwtService.generateToken(authentication.getName());

        CookieUtil.addCookie(response,CookieUtil.createAccessTokenCookie(accessToken));
        CookieUtil.addCookie(response,CookieUtil.createRefreshTokenCookie(refreshToken));

        return ResponseEntity.ok(Map.of("message","Login successful"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?>refresh(@CookieValue(value = "refreshToken",required = false) String refreshToken,HttpServletResponse response){

        if(refreshToken == null || !jwtService.validateToken(refreshToken)){
            return ResponseEntity.status(401).body(Map.of("error","Invalid or missing refreshToken"));
        }

        String username=jwtService.extractUsername(refreshToken);
        String newAccess= jwtService.generateToken(username);
        String newRefresh=jwtService.generateToken(username);

        CookieUtil.addCookie(response,CookieUtil.createAccessTokenCookie(newAccess));
        CookieUtil.addCookie(response,CookieUtil.createRefreshTokenCookie(newRefresh));

        return ResponseEntity.ok(Map.of("message","Token refreshed"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?>logout(HttpServletResponse response){
        CookieUtil.addCookie(response,CookieUtil.clearCookie("accessToken","/"));
        CookieUtil.addCookie(response,CookieUtil.clearCookie("refreshToken","api/refresh"));

        return ResponseEntity.ok(Map.of("message","Logged out successfully"));
    }



}

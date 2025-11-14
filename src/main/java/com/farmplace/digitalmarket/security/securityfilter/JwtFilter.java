package com.farmplace.digitalmarket.security.securityfilter;

import com.farmplace.digitalmarket.security.securityservice.CustomUserDetailsService;
import com.farmplace.digitalmarket.security.securityservice.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ApplicationContext context;

    public JwtFilter(JwtService jwtService, ApplicationContext context) {
        this.jwtService = jwtService;
        this.context = context;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token=extractJwtFromRequest(request);
        String username=null;

       if(token!=null){
           try {
               username=jwtService.extractUsername(token);
           }
             catch (Exception exception){
               logger.error("Invalid JWT "+exception.getMessage());
             }
           username=jwtService.extractUsername(token);
       }

       if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null){

           UserDetails userDetails=context.getBean(CustomUserDetailsService.class).loadUserByUsername(username);
           if(jwtService.validateToken(token,userDetails)){
               UsernamePasswordAuthenticationToken authToken=new
                       UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
               authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(authToken);
           }
       }
       filterChain.doFilter(request,response);
    }

    private String extractJwtFromRequest(HttpServletRequest request){

        String authHeader=request.getHeader("Authorization");
        if(authHeader !=null && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }

        if(request.getCookies() !=null){
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "accessToken".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}

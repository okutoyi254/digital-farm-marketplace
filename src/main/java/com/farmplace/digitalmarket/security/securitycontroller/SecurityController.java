package com.farmplace.digitalmarket.security.securitycontroller;

import com.farmplace.digitalmarket.security.dto.LoginRequest;
import com.farmplace.digitalmarket.security.securityservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SecurityController {

    @Autowired private UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request){return userService.verify(request);}

}

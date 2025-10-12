package com.farmplace.digitalmarket.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/farm")
public class ApiController {

    @GetMapping("/test")
    public String test(){
        return "Test dispatcher servlet,working......";
    }
}

package com.farmplace.digitalmarket.controllers;

import com.farmplace.digitalmarket.DTO.*;
import com.farmplace.digitalmarket.service.serviceInterface.CustomerService;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/create/account")
    public ResponseEntity<ApiResponse<CreateAccountDto>>createAccount(CustomerRegister customerRegister){
        CreateAccountDto createAccountDto=customerService.createAccount(customerRegister);

        ApiResponse<CreateAccountDto>response=new ApiResponse<>(LocalDateTime.now(),
                "Account created successfully",true,createAccountDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PostMapping("/addproduct")
    public ResponseEntity<ApiResponse<AddProductToCartResponse>>addProductToCart(AddProductToCart productToCart){

       AddProductToCartResponse addProduct=customerService.addProductToCart(productToCart);

       ApiResponse<AddProductToCartResponse>response=new ApiResponse<>(LocalDateTime.now(),"Added successfully",true,addProduct);

       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

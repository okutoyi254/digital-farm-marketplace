package com.farmplace.digitalmarket.controllers;

import com.farmplace.digitalmarket.DTO.*;
import com.farmplace.digitalmarket.Model.Product;
import com.farmplace.digitalmarket.service.serviceInterface.FarmerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/farmer")
public class FarmerController {

    private final FarmerService farmerService;

    public FarmerController(FarmerService farmerService) {
        this.farmerService = farmerService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<CreateAccountDto>>createAccount(FarmerRegister farmerRegister){

        CreateAccountDto createAccountDto=farmerService.register(farmerRegister);

        ApiResponse<CreateAccountDto> response=new ApiResponse<>(LocalDateTime.now(),"Account created successfully",true,createAccountDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/addproduct")
    public ResponseEntity<ApiResponse<PostProductResponse>> addProduct(PostProductRequest postProduct){

        PostProductResponse postResponse=farmerService.postProduct(postProduct);

        ApiResponse<PostProductResponse>response=new ApiResponse<>(LocalDateTime.now(),"Product added successfully",true,postResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);



    }
}

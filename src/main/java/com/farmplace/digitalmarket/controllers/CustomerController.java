package com.farmplace.digitalmarket.controllers;

import com.farmplace.digitalmarket.DTO.*;
import com.farmplace.digitalmarket.Model.ProductsCategory;
import com.farmplace.digitalmarket.repository.ProductCategoryRepository;
import com.farmplace.digitalmarket.repository.ProductRepository;
import com.farmplace.digitalmarket.service.serviceInterface.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;


@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final ProductRepository productRepository;
    private  final ProductCategoryRepository categoryRepository;

    public CustomerController(CustomerService customerService, ProductRepository productRepository, ProductCategoryRepository categoryRepository) {
        this.customerService = customerService;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/create/account")
    public ResponseEntity<ApiResponse<CreateAccountDto>>createAccount(CustomerRegister customerRegister){
        CreateAccountDto createAccountDto=customerService.createAccount(customerRegister);

        ApiResponse<CreateAccountDto>response=new ApiResponse<>(LocalDateTime.now(),
                "Account created successfully",true,createAccountDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PostMapping("/add-product")
    public ResponseEntity<ApiResponse<AddProductToCartResponse>>addProductToCart(AddProductToCart productToCart){

       AddProductToCartResponse addProduct=customerService.addProductToCart(productToCart);

       ApiResponse<AddProductToCartResponse>response=new ApiResponse<>(LocalDateTime.now(),"Added successfully",true,addProduct);

       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/all-products")
    public ResponseEntity<String> getAllProducts() throws JsonProcessingException {
        // Check if there are any products
        if (productRepository.findAll().isEmpty()) {
            String noProductsJson = new ObjectMapper()
                    .writeValueAsString(Map.of("message", "No available products"));
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(noProductsJson);
        }

        // get JSON string from repository
        String rawJson = productRepository.getAllProductsJSON();

        //pretty-print
        ObjectMapper mapper = new ObjectMapper();
        Object parsedJson = mapper.readValue(rawJson, Object.class);
        String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parsedJson);

        //return JSON
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(prettyJson);
    }

    }





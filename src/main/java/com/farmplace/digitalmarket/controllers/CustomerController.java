package com.farmplace.digitalmarket.controllers;

import com.farmplace.digitalmarket.DTO.*;
import com.farmplace.digitalmarket.repository.ProductCategoryRepository;
import com.farmplace.digitalmarket.repository.ProductRepository;
import com.farmplace.digitalmarket.service.serviceInterface.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import java.util.Map;



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

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(prettyJson);
    }

    @GetMapping("/getProductByName")
    public ResponseEntity<?> findProductByName(@RequestParam String productName) {
        String rawJson = productRepository.getProductByName(productName);

        // Handle empty or null result from DB
        if (rawJson == null || rawJson.isBlank() || rawJson.equalsIgnoreCase("null")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No available matches");
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            ProductResponse result = mapper.readValue(rawJson, ProductResponse.class);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result);

        } catch (JsonProcessingException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing product data");
        }
    }


    }




